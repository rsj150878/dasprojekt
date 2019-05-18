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
import com.app.dbio.DBHundDokumente;
import com.app.dbio.DBPerson;
import com.app.dbio.DBVeranstaltung;
import com.app.enumdatatypes.DokumentGehoertZuType;
import com.app.enumdatatypes.Rassen;
import com.app.enumdatatypes.VeranstaltungsStufen;
import com.app.filestorage.HundeDokumente;
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

public class Urkunde extends CustomComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8250020065563827808L;
	/** The original PDF file. */
	public static final String DATASHEET = "files/Urkunde allgemein_FORMULAR1.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Urkunde.pdf";

	private AbsoluteLayout mainLayout;
	
	private DBVeranstaltung dbVa;
	private DBHund dbHund;
	private DBPerson dbPerson;

	public Urkunde(Veranstaltung veranstaltung, VeranstaltungsStufe veranstaltungsStufe) {
		
		
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

			BrowserFrame e = new BrowserFrame("BGHBewertungsblatt", s);
			mainLayout.addComponent(e);

			// e.addDetachListener(mainLayout.getp);

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
		
		
		HundeDokumente doc = new HundeDokumente();
		
		DBHundDokumente dbHundDoc = new DBHundDokumente();
		doc = dbHundDoc.getHundDokumentForId(DokumentGehoertZuType.VERANSTALTUNG,veranstaltung.getId_veranstaltung());
		
		List<VeranstaltungsTeilnehmer> teilnehmer = dbVa.getAlleTeilnehmerZuStufe(veranstaltungsStufe.getIdStufe());

		for (VeranstaltungsTeilnehmer zw:teilnehmer) {

			// create a PDF in memory
			baos = new ByteArrayOutputStream();

			Hund hund = dbHund.getHundForHundId(zw.getIdHund());
			Person person = dbPerson.getPersonForId(zw.getIdPerson());
			
			if (doc == null ) {
				pdfInnerDoc = new PdfDocument(new PdfReader(DATASHEET), new PdfWriter(baos));
					
			} else {
				pdfInnerDoc = new PdfDocument(new PdfReader(doc.getHundDokument()), new PdfWriter(baos));
			}
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();
		

			if (zw.getHundefuehrer() != null
					&& !zw.getHundefuehrer().isEmpty()
					&& zw.getHundefuehrer().length() > 0) {

				fields.get("HUNDEFÜHRER/IN")
						.setValue(zw.getHundefuehrer());
			} else {
				fields.get("HUNDEFÜHRER/IN")
						.setValue(person.getLastName() + " " + person.getFirstName());

				
			}

			fields.get("CHIP-NR").setValue(hund.getChipnummer());

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
			fields.get("GEWORFEN AM").setValue(dateFormat1.format(hund.getWurfdatum()));
			
			if (!(hund.getZuchtbuchnummer() == null)) {

				fields.get("ÖHZB-NR").setValue(hund.getZuchtbuchnummer());
			}

			if (!(hund.getGeschlecht() == null)) {
				if (new String("R").equals(hund.getGeschlecht())) {
					fields.get("RÜDE").setValue("Ja");

				} else {
					fields.get("HÜNDIN").setValue("Ja");

				}
			}

			fields.get("RASSE").setValue(Rassen.getUrkundenBezeichnungFuerKurzBezeichnung(
					hund.getRasse()));

			fields.get("NAME DES HUNDES").setValue(hund.getZwingername());

			fields.get("ORT/DATUM").setValue(veranstaltung.getVeranstaltungsort()
					+ " " + dateFormat1.format(veranstaltung.getDatum()));

			fields.get("VERANSTALTER/AUSBILDUNGSSTÄTTE")
					.setValue(veranstaltung.getVeranstalter());
			VeranstaltungsStufen defStufe = veranstaltungsStufe.getStufenTyp();

			fields.get("PRÜFUNG").setValue(defStufe.getLangBezeichnung());

			fields.get("ZEILE 3").setValue("");
			fields.get("ZEILE 1").setValue(defStufe.getLangBezeichnung());

			if (zw.getBestanden() != null) {

				if ("N".equals(zw.getBestanden())) {
					fields.get("ZEILE 2").setValue("leider nicht bestanden");
				} else if (defStufe == VeranstaltungsStufen.STUFE_BH) {
					fields.get("ZEILE 2").setValue("erfolgreich bestanden");

				} else if (defStufe == VeranstaltungsStufen.STUFE_BGH1 || defStufe == VeranstaltungsStufen.STUFE_BGH2
						|| defStufe == VeranstaltungsStufen.STUFE_BGH3
						|| defStufe == VeranstaltungsStufen.STUFE_RBP4_O_WASSER
						|| defStufe == VeranstaltungsStufen.STUFE_RBP4_M_WASSER
						|| defStufe == VeranstaltungsStufen.STUFE_RBP3 || defStufe == VeranstaltungsStufen.STUFE_RBP2
						|| defStufe == VeranstaltungsStufen.STUFE_RBP1 || defStufe == VeranstaltungsStufen.STUFE_GAP1
						|| defStufe == VeranstaltungsStufen.STUFE_GAP2 || defStufe == VeranstaltungsStufen.STUFE_GAP3

						|| defStufe == VeranstaltungsStufen.STUFE_RBP2_2017
						|| defStufe == VeranstaltungsStufen.STUFE_RBP3_2017

				) {
					fields.get("ZEILE 2").setValue("erfolgreich mit "
							+ zw.getGesPunkte().toString() + " Punkten und "
							+ defStufe.getBewertung(
									zw.getGesPunkte()));
					fields.get("ZEILE 3").setValue("bestanden");

				}
			}

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
