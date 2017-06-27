package com.app.printClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.dbIO.DBConnection;
import com.app.enumPackage.Rassen;
import com.app.enumPackage.VeranstaltungsStufen;
import com.app.service.TemporaryFileDownloadResource;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class Urkunde extends CustomComponent {
	private PdfReader reader;

	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/Urkunde allgemein_FORMULAR1.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Urkunde.pdf";

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	public Urkunde(Item veranstaltung, Item veranstaltungsStufe) {
		q3 = new TableQuery("veranstaltungs_teilnehmer",
				DBConnection.INSTANCE.getConnectionPool());
		q3.setVersionColumn("version");

		q4 = new TableQuery("person", DBConnection.INSTANCE.getConnectionPool());
		q4.setVersionColumn("version");

		q5 = new TableQuery("hund", DBConnection.INSTANCE.getConnectionPool());
		q5.setVersionColumn("version");

		try {

			personContainer = new SQLContainer(q4);
			hundContainer = new SQLContainer(q5);
			teilnehmerContainer = new SQLContainer(q3);

			teilnehmerContainer
					.addContainerFilter(new Equal("id_stufe",
							veranstaltungsStufe.getItemProperty("id_stufe")
									.getValue()));

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			fos = new FileOutputStream(RESULT);
			PdfCopyFields copy = new PdfCopyFields(fos);
			copy.open();

			for (Object id : teilnehmerContainer.getItemIds()) {
				PdfReader zwReader = new PdfReader(bauPdf(veranstaltung,
						veranstaltungsStufe, teilnehmerContainer.getItem(id)));
				copy.addDocument(zwReader);
				zwReader.close();
			}

			copy.close();

			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT,
						"application/pdf", new File(RESULT));
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("BGHBewertungsblatt", s);
			mainLayout.addComponent(e);

			// e.addDetachListener(mainLayout.getp);

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private byte[] bauPdf(Item veranstaltung, Item veranstaltungsStufe,
			Item teilnehmerItem) throws Exception {
		PdfReader reader = new PdfReader(DATASHEET);
		// fos = new FileOutputStream(RESULT);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, baos);
		stamper.setFormFlattening(true);
		AcroFields fields = stamper.getAcroFields();

		BaseFont unicode = BaseFont.createFont(FONT, BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED);
		fields.addSubstitutionFont(unicode);

		hundContainer.addContainerFilter(new Equal("idhund", teilnehmerItem
				.getItemProperty("id_hund").getValue()));

		personContainer.addContainerFilter(new Equal("idperson", teilnehmerItem
				.getItemProperty("id_person").getValue()));

		if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null
				&& !teilnehmerItem.getItemProperty("hundefuehrer").getValue()
						.toString().isEmpty()
				&& teilnehmerItem.getItemProperty("hundefuehrer").getValue()
						.toString().length() > 0) {

			fields.setField("HUNDEFÜHRER/IN",
					teilnehmerItem.getItemProperty("hundefuehrer").getValue()
							.toString());
		} else {
			fields.setField(
					"HUNDEFÜHRER/IN",
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("nachname").getValue().toString()
							+ " "
							+ personContainer
									.getItem(personContainer.firstItemId())
									.getItemProperty("vorname").getValue()
									.toString()

			);
		}

		fields.setField("CHIP-NR",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("chipnummer").getValue().toString());

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
		fields.setField(
				"GEWORFEN AM",
				dateFormat1.format(hundContainer
						.getItem(hundContainer.firstItemId())
						.getItemProperty("wurfdatum").getValue()));
		if (!(hundContainer.getItem(hundContainer.firstItemId())
				.getItemProperty("zuchtbuchnummer").getValue() == null)) {

			fields.setField("ÖHZB-NR",
					hundContainer.getItem(hundContainer.firstItemId())
							.getItemProperty("zuchtbuchnummer").getValue()
							.toString());
		}

		if (!(hundContainer.getItem(hundContainer.firstItemId())
				.getItemProperty("geschlecht").getValue() == null)) {
			if (new String("R").equals(hundContainer
					.getItem(hundContainer.firstItemId())
					.getItemProperty("geschlecht").getValue().toString())) {
				fields.setField("HÜNDIN", " ");
				fields.setField("RÜDE", "Ja");

			} else {
				fields.setField("RÜDE", " ");
				fields.setField("HÜNDIN", "Ja");

			}
		}

		fields.setField(
				"RASSE",
				Rassen.getUrkundenBezeichnungFuerKurzBezeichnung(hundContainer
						.getItem(hundContainer.firstItemId())
						.getItemProperty("rasse").getValue().toString()));

		fields.setField("NAME DES HUNDES",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("zwingername").getValue().toString());

		fields.setField(
				"ORT/DATUM",
				veranstaltung.getItemProperty("veranstaltungsort").getValue()
						.toString()
						+ " "
						+ dateFormat1.format(veranstaltung.getItemProperty(
								"datum").getValue()));

		fields.setField("VERANSTALTER/AUSBILDUNGSSTÄTTE", veranstaltung
				.getItemProperty("veranstalter").getValue().toString());
		VeranstaltungsStufen defStufe = VeranstaltungsStufen
				.getBezeichnungForId(new Integer(veranstaltungsStufe
						.getItemProperty("stufen_typ").getValue().toString()));

		fields.setField("PRÜFUNG", defStufe.getLangBezeichnung());

		fields.setField("ZEILE 3", "");
		fields.setField("ZEILE 1", defStufe.getLangBezeichnung());

		if (teilnehmerItem.getItemProperty("bestanden").getValue() != null) {

			if ("N".equals(teilnehmerItem.getItemProperty("bestanden")
					.getValue().toString())) {
				fields.setField("ZEILE 2", "leider nicht bestanden");
			} else if (defStufe == VeranstaltungsStufen.STUFE_BH) {
				fields.setField("ZEILE 2", "erfolgreich bestanden");

			} else if (defStufe == VeranstaltungsStufen.STUFE_BGH1
					|| defStufe == VeranstaltungsStufen.STUFE_BGH2
					|| defStufe == VeranstaltungsStufen.STUFE_BGH3
					|| defStufe == VeranstaltungsStufen.STUFE_RBP4_O_WASSER
					|| defStufe == VeranstaltungsStufen.STUFE_RBP4_M_WASSER
					|| defStufe == VeranstaltungsStufen.STUFE_RBP3
					|| defStufe == VeranstaltungsStufen.STUFE_RBP2
					|| defStufe == VeranstaltungsStufen.STUFE_RBP1
					|| defStufe == VeranstaltungsStufen.STUFE_GAP1
					|| defStufe == VeranstaltungsStufen.STUFE_GAP2
					|| defStufe == VeranstaltungsStufen.STUFE_GAP3

					|| defStufe == VeranstaltungsStufen.STUFE_RBP2_2017
					|| defStufe == VeranstaltungsStufen.STUFE_RBP3_2017
			
			) {
				fields.setField(
						"ZEILE 2",
						"erfolgreich mit "
								+ teilnehmerItem.getItemProperty("ges_punkte")
										.getValue().toString()
								+ " Punkten und "
								+ defStufe.getBewertung(new Integer(
										teilnehmerItem
												.getItemProperty("ges_punkte")
												.getValue().toString())));
				fields.setField("ZEILE 3", "bestanden");

			}
		}

		hundContainer.removeAllContainerFilters();
		personContainer.removeAllContainerFilters();

		stamper.close();
		reader.close();
		return baos.toByteArray();

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
