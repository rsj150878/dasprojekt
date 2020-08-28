package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

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


public class JungHundePruefung2017 extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 29409964073332357L;

	private AbsoluteLayout mainLayout;

	private static String RESULT = "Urkunde.pdf";
	
	public static final String DATASHEET_KURZ = "files/URKUNDE JUHU_PFG_2020-Alfred.pdf";
	public static final String DATASHEET_LANG = "files/URKUNDE JUHU_PFG_2017-Alfred+Birgit.pdf";

	private DBVeranstaltung dbVa;
	private DBHund dbHund;
	private DBPerson dbPerson;

	public JungHundePruefung2017(Veranstaltung veranstaltung,
			 VeranstaltungsStufe veranstaltungsStufe) {

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
		
		String vorlage =  DATASHEET_KURZ;

		ByteArrayOutputStream baos;
		PdfDocument pdfInnerDoc;
		Map<String, PdfFormField> fields;
		PdfAcroForm form;
		
		
		List<VeranstaltungsTeilnehmer> teilnehmer = dbVa.getAlleTeilnehmerZuStufe(veranstaltungsStufe.getIdStufe());

		for (VeranstaltungsTeilnehmer zw : teilnehmer) {

			// create a PDF in memory
			baos = new ByteArrayOutputStream();
			pdfInnerDoc = new PdfDocument(new PdfReader(vorlage), new PdfWriter(baos));
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();

			Hund hund = dbHund.getHundForHundId(zw.getIdHund());
			Person person = dbPerson.getPersonForId(zw.getIdPerson());
	
			fields.get("hund").setValue(hund.getZwingername());

			if (zw.getHundefuehrer() != null && !zw.getHundefuehrer().isEmpty() && zw.getHundefuehrer().length() > 0) {
				
				
				fields.get("hundeFuehrer")
						.setValue(zw.getHundefuehrer());
			} else {
				fields.get("hundeFuehrer")
						.setValue(person.getLastName() + " "
								+ person.getFirstName()

				);
			}

					form.flattenFields();
			pdfInnerDoc.close();

			pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
			pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
			pdfInnerDoc.close();
		}

		pdfDoc.close();
	}

	
}
