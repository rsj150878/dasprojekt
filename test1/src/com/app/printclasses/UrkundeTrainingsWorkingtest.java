package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.auth.Hund;
import com.app.auth.Person;
import com.app.dbio.DBHund;
import com.app.dbio.DBPerson;
import com.app.dbio.DBVeranstaltung;
import com.app.enumdatatypes.VeranstaltungsStufen;
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

public class UrkundeTrainingsWorkingtest extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1734597059594829717L;
	/** The original PDF file. */
	public static final String DATASHEET = "files/URKUNDE_NOESTACH_4.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Urkunde.pdf";

	private AbsoluteLayout mainLayout;

	private DBVeranstaltung dbVa;
	private DBHund dbHund;
	private DBPerson dbPerson;

	public UrkundeTrainingsWorkingtest(Veranstaltung veranstaltung, VeranstaltungsStufe veranstaltungsStufe) {

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
			s = new TemporaryFileDownloadResource(RESULT, "application/pdf", new File(RESULT));

			BrowserFrame e = new BrowserFrame("BGHBewertungsblatt", s);
			mainLayout.addComponent(e);

			// e.addDetachListener(mainLayout.getp);

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	// Name Hundeführer#
	// Klasse#
	// Rang/Punkte#
	// Name Hund#
	//

	private void bauPdf(Veranstaltung veranstaltung, VeranstaltungsStufe veranstaltungsStufe) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(RESULT));
		pdfDoc.initializeOutlines();

		ByteArrayOutputStream baos;
		PdfDocument pdfInnerDoc;
		Map<String, PdfFormField> fields;
		PdfAcroForm form;
		List<VeranstaltungsTeilnehmer> teilnehmer = dbVa.getAlleTeilnehmerZuStufe(veranstaltungsStufe.getIdStufe());

		for (VeranstaltungsTeilnehmer zw : teilnehmer) {

			baos = new ByteArrayOutputStream();
			pdfInnerDoc = new PdfDocument(new PdfReader(DATASHEET), new PdfWriter(baos));
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();

			Hund hund = dbHund.getHundForHundId(zw.getIdHund());
			Person person = dbPerson.getPersonForId(zw.getIdPerson());

			if (zw.getHundefuehrer() != null && !zw.getHundefuehrer().isEmpty() && zw.getHundefuehrer().length() > 0) {

				fields.get("hundefuehrer").setValue(zw.getHundefuehrer());
			} else {
				fields.get("hundefuehrer").setValue(person.getLastName() + " " + person.getFirstName()	);
			}

			fields.get("hund").setValue(hund.getZwingername());

			VeranstaltungsStufen defStufe = veranstaltungsStufe.getStufenTyp();

			fields.get("klasse").setValue(defStufe.getLangBezeichnung());

			if (!(zw.getGesPunkte() == null)) {

				if (zw.getGesPunkte().equals(Integer.valueOf(0))) {

				} else {
					String text = zw.getGesPunkte() + " Punkte ";
					PdfFormField punkteField = fields.get("punkte");
					// punkteField.setJustification(PdfFormField.ALIGN_CENTER);
					punkteField.setValue(text);

					text = zw.getPlatzierung() + ". Platz";

					if (!(zw.getSonderWertung() == null)) {
						text = text + " mit Judges Choice";
					}

					PdfFormField rangField = fields.get("rang");
					// punkteField.setJustification(PdfFormField.ALIGN_CENTER);
					rangField.setValue(text);

				}

			}
			// fields.setField("ZEIL_finalE 3", "bestanden");

			
			form.flattenFields();
			pdfInnerDoc.close();

			pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
			pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
			pdfInnerDoc.close();
		}

		pdfDoc.close();

	}
	// HUNDEFÜHRERIN
	// CHIP-NR
	// GEWORFEN AM
	// ÖHZB-NR
	// RÜDE
	// RASSE
	// Ort Datum
	// NAME DES HUNDES
	// HÜNDIN
	// ZEILE 3
	// PRÜFUNGSZEILE
	// ZEILE 1
	// ZEILE 2

}
