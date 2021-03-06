package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.auth.Hund;
import com.app.auth.Person;
import com.app.dbio.DBHund;
import com.app.dbio.DBPerson;
import com.app.dbio.DBVeranstaltung;
import com.app.enumdatatypes.Rassen;
import com.app.service.TemporaryFileDownloadResource;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsStufe;
import com.app.veranstaltung.VeranstaltungsTeilnehmer;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

public class VtBestaetigungsBlatt extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5610861175293387273L;

	/** The original PDF file. */
	public static final String DATASHEET = "files/BH-VT_Formular.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "VtBestätigungsBlatt.pdf";

	private AbsoluteLayout mainLayout;

	private DBVeranstaltung dbVa;
	private DBHund dbHund;
	private DBPerson dbPerson;

	public VtBestaetigungsBlatt(Veranstaltung veranstaltung, VeranstaltungsStufe veranstaltungsStufe) {
		dbVa = new DBVeranstaltung();
		dbHund = new DBHund();
		dbPerson = new DBPerson();

		try {

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			bauPdf(veranstaltung, veranstaltungsStufe);

			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT, "application/pdf", new File(RESULT));
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("GAP1Bewertungsblatt", s);
			mainLayout.addComponent(e);

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private void bauPdf(Veranstaltung veranstaltung, VeranstaltungsStufe veranstaltungsStufe) throws Exception {

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(RESULT));
		pdfDoc.initializeOutlines();

		ByteArrayOutputStream baos;
		PdfDocument pdfInnerDoc;
		Map<String, PdfFormField> fields;
		PdfAcroForm form;
		List<VeranstaltungsTeilnehmer> teilnehmer = dbVa.getAlleTeilnehmerZuStufe(veranstaltungsStufe.getIdStufe());

		
		
		for (VeranstaltungsTeilnehmer zw : teilnehmer) {

			// create a PDF in memory
			baos = new ByteArrayOutputStream();
			pdfInnerDoc = new PdfDocument(new PdfReader(DATASHEET), new PdfWriter(baos));
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();

			Hund hund = dbHund.getHundForHundId(zw.getIdHund());
			Person person = dbPerson.getPersonForId(zw.getIdPerson());

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");

			fields.get("OrtDatum").setValue(dateFormat1.format(veranstaltung.getDatum()));
		
			fields.get("AusbildungsvereinPlatz").setValue(veranstaltung.getVeranstaltungsort());
		
			fields.get("Veranstalter").setValue(veranstaltung.getVeranstalter());
			
			fields.get("richter").setValue(veranstaltung.getRichter());
			
			fields.get("hundename").setValue(hund.getZwingername());

			fields.get("Wurfdatum").setValue(dateFormat1.format(hund.getWurfdatum()));

			fields.get("Chipnummer").setValue(hund.getChipnummer());
		
			fields.get("zbnr").setValue(hund.getZuchtbuchnummer());

			if (zw.getHundefuehrer() != null && !zw.getHundefuehrer().isEmpty() && zw.getHundefuehrer().length() > 0) {

				fields.get("Hundefuehrer").setValue(zw.getHundefuehrer());
			} else {
				fields.get("Hundefuehrer").setValue(person.getLastName() + " " + person.getFirstName());
			}
		
			fields.get("name_besitzer").setValue(person.getLastName() + " " + person.getFirstName());
			fields.get("adresse_besitzer").setValue(person.getStrasse() + " " + person.getHausnummer() + ", " + person.getPlz() + " "+ person.getOrt());
			fields.get("adresse_hundefuehrer").setValue(person.getStrasse() + " " + person.getHausnummer() + ", " + person.getPlz() + " "+ person.getOrt());
			fields.get("Pruefung").setValue(veranstaltung.getName());
			
			form.flattenFields(); 
			pdfInnerDoc.close();

			pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
			pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
		
			pdfInnerDoc.close();
		}

		pdfDoc.close();
	}
	
	
}
