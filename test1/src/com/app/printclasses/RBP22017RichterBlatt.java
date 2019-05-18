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

public class RBP22017RichterBlatt extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3422095654946747403L;
	/** The original PDF file. */
	public static final String DATASHEET = "files/RBP_Richterblatt_RBP2_2017_FORMULAR.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "RichterBlatt.pdf";

	private AbsoluteLayout mainLayout;
	private DBVeranstaltung dbVa;
	private DBHund dbHund;
	private DBPerson dbPerson;
	
	public RBP22017RichterBlatt(Veranstaltung veranstaltung, VeranstaltungsStufe veranstaltungsStufe) {

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

			BrowserFrame e = new BrowserFrame("RBP1Bewertungsblatt", s);
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
			fields.get("Datum").setValue(dateFormat1.format(veranstaltung.getDatum()));

			fields.get("Hund").setValue(hund.getZwingername());

			fields.get("Wurftag").setValue(dateFormat1.format(
					hund.getWurfdatum()));

			fields.get("Chip- oder Tätonr.:").setValue(hund.getChipnummer());

			if (zw.getHundefuehrer() != null && !zw.getHundefuehrer().isEmpty() && zw.getHundefuehrer().length() > 0) {
				
				fields.get("HF").setValue(zw.getHundefuehrer());
			} else {
				fields.get("HF")
						.setValue(person.getLastName() + " "
								+ person.getFirstName()

				);
			}

			fields.get("Geschlecht").setValue(hund.getGeschlecht());

			fields.get("Ort").setValue(veranstaltung.getVeranstaltungsort());
			
			fields.get("startnr").setValue(zw.getStartnr().toString());

			form.flattenFields();
			pdfInnerDoc.close();

			pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
			pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
			pdfInnerDoc.close();
		}

		pdfDoc.close();
	}

	// HF
	// Chip- oder Tätonr.:
	// Geschlecht
	// Ort
	// Hund
	// Datum
	// Wurftag

	// PdfReader reader = new PdfReader(DATASHEET);
	// // Get the fields from the reader (read-only!!!)
	// AcroFields form = reader.getAcroFields();
	// // Loop over the fields and get info about them
	// Set<String> fields = form.getFields().keySet();
	// for (String key : fields) {
	// System.out.println(key);
	// }

}
