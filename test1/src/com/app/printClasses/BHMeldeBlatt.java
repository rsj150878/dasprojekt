package com.app.printClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.bean.RassenBean;
import com.app.dbIO.DBConnection;
import com.app.enumPackage.Rassen;
import com.app.enumPackage.VeranstaltungsStufen;
import com.app.service.TemporaryFileDownloadResource;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

public class BHMeldeBlatt extends CustomComponent {

	private PdfReader reader;
	private PdfStamper stamper;
	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/BH_BGH_Formular.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "RichterBlatt.pdf";

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	public BHMeldeBlatt(Item veranstaltung, Item veranstaltungsStufe) {

		try {
			reader = new PdfReader(DATASHEET);
			// Get the fields from the reader (read-only!!!)
			AcroFields form = reader.getAcroFields();
			// Loop over the fields and get info about them
			Set<String> fields = form.getFields().keySet();
			for (String key : fields) {
				System.out.println(key);

			}

		} catch (Exception ee) {

		}

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
			mainLayout.setImmediate(false);
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

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private byte[] bauPdf(Item veranstaltung, Item veranstaltungsStufe,
			Item teilnehmerItem) throws Exception {

		PdfReader reader = new PdfReader(DATASHEET);
		// fos = new FileOutputStream(RESULT);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stamper = new PdfStamper(reader, baos);
		AcroFields fields = stamper.getAcroFields();

		BaseFont unicode = BaseFont.createFont(FONT, BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED);
		fields.addSubstitutionFont(unicode);

		hundContainer.addContainerFilter(new Equal("idhund", teilnehmerItem
				.getItemProperty("id_hund").getValue()));

		personContainer.addContainerFilter(new Equal("idperson", teilnehmerItem
				.getItemProperty("id_person").getValue()));

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");

		if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null) {

			fields.setField("Name Hundeführer",
					teilnehmerItem.getItemProperty("hundefuehrer").getValue()
							.toString());
		}

		fields.setField(
				"Name",
				personContainer.getItem(personContainer.firstItemId())
						.getItemProperty("nachname").getValue().toString()
						+ " "
						+ personContainer
								.getItem(personContainer.firstItemId())
								.getItemProperty("vorname").getValue()
								.toString()

		);

		if (!(personContainer.getItem(personContainer.firstItemId())
				.getItemProperty("geb_dat").getValue() == null)) {
			fields.setField(
					"Geburtsdatum",
					dateFormat1.format(personContainer
							.getItem(personContainer.firstItemId())
							.getItemProperty("geb_dat").getValue()));
		}

		if (!(personContainer.getItem(personContainer.firstItemId())
				.getItemProperty("oerc_mitgliedsnummer").getValue() == null)) {
			fields.setField("ÖRC-Mitgliedsnummer",
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("oerc_mitgliedsnummer").getValue()
							.toString());
			fields.setField("ÖRC-Mitglied JA", "Ja");
		} else {
			fields.setField("ÖRC-Mitglied NEIN", "Ja");
		}

		fields.setField(
				"Adresse",
				personContainer.getItem(personContainer.firstItemId())
						.getItemProperty("strasse").getValue()
						+ " "
						+ personContainer
								.getItem(personContainer.firstItemId())
								.getItemProperty("hausnummer").getValue()
						+ ", "
						+ personContainer
								.getItem(personContainer.firstItemId())
								.getItemProperty("plz").getValue()
						+ " "
						+ personContainer
								.getItem(personContainer.firstItemId())
								.getItemProperty("ort").getValue());

		if (!(personContainer.getItem(personContainer.firstItemId())
				.getItemProperty("telnr").getValue() == null)) {
			fields.setField("Telefonnummer",
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("telnr").getValue().toString());
		}

		if (!(personContainer.getItem(personContainer.firstItemId())
				.getItemProperty("mobnr").getValue() == null)) {
			fields.setField("Mobiltelefonnummer",
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("mobnr").getValue().toString());
		}

		if (!(personContainer.getItem(personContainer.firstItemId())
				.getItemProperty("email").getValue() == null)) {
			fields.setField("E-Mail-Adresse",
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("email").getValue().toString());

		}

		for (Rassen o : Rassen.values()) {
			RassenBean addObject = new RassenBean(o.getRassenKurzBezeichnung(),
					o.getRassenLangBezeichnung());

			if (hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("rasse").getValue() != null
					&& o.getRassenKurzBezeichnung().equals(
							hundContainer.getItem(hundContainer.firstItemId())
									.getItemProperty("rasse").getValue()
									.toString())) {
				fields.setField("Rasse", o.getRassenLangBezeichnung());
			}

		}

		if (hundContainer.getItem(hundContainer.firstItemId())
				.getItemProperty("farbe").getValue() != null) {
			fields.setField("Farbe",
					hundContainer.getItem(hundContainer.firstItemId())
							.getItemProperty("farbe").getValue().toString());
		}
		fields.setField("Zuchtbuchnummer",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("zuchtbuchnummer").getValue()
						.toString());
		fields.setField("Name des Hundes lt. Ahnentafel",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("zwingername").getValue().toString());

		fields.setField("Rufname",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("rufname").getValue().toString());

		fields.setField(
				"Wurfdatum",
				dateFormat1.format(hundContainer
						.getItem(hundContainer.firstItemId())
						.getItemProperty("wurfdatum").getValue()));

		fields.setField("Chipnummer",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("chipnummer").getValue().toString());

		if (hundContainer.getItem(hundContainer.firstItemId())
				.getItemProperty("geschlecht").getValue().toString()
				.equals("R")) {
			fields.setField("Geschlecht", "Rüde");
		} else {
			fields.setField("Geschlecht", "Hündin");
		}

		if (!(hundContainer.getItem(hundContainer.firstItemId())
				.getItemProperty("zuechter").getValue() == null)) {
			fields.setField("Züchter",
					hundContainer.getItem(hundContainer.firstItemId())
							.getItemProperty("zuechter").getValue().toString());
		}

		VeranstaltungsStufen defStufe = VeranstaltungsStufen
				.getBezeichnungForId(Integer.valueOf(veranstaltungsStufe
						.getItemProperty("stufen_typ").getValue().toString()));

		if (defStufe == VeranstaltungsStufen.STUFE_BH) {
			fields.setField("Anmeldung BH", "Ja");

		} else if (defStufe == VeranstaltungsStufen.STUFE_BGH1) {
			fields.setField("Anmeldung BGH-1", "Ja");

			if (hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("bhdatum").getValue() != null) {
				fields.setField(
						"Datum der bestandenen BH-Prüfung (BGH-1)",
						dateFormat1.format(hundContainer
								.getItem(hundContainer.firstItemId())
								.getItemProperty("bhdatum").getValue()));
			}
		} else if (defStufe == VeranstaltungsStufen.STUFE_BGH2) {
			fields.setField("Anmeldung BGH-2", "Ja");

			if (hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("bhdatum").getValue() != null) {

				fields.setField(
						"Datum der bestandenen BH-Prüfung (BGH-2)",
						dateFormat1.format(hundContainer
								.getItem(hundContainer.firstItemId())
								.getItemProperty("bhdatum").getValue()));
			}
		} else if (defStufe == VeranstaltungsStufen.STUFE_BGH3) {
			fields.setField("Anmeldung BGH-3", "Ja");

			if (hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("bhdatum").getValue() != null) {

				fields.setField(
						"Datum der bestandenen BH-Prüfung (BGH-3)",
						dateFormat1.format(hundContainer
								.getItem(hundContainer.firstItemId())
								.getItemProperty("bhdatum").getValue()));
			}
		}

		hundContainer.removeAllContainerFilters();
		personContainer.removeAllContainerFilters();

		stamper.close();
		reader.close();
		return baos.toByteArray();
	}

	// Ort
	// Datum
	// Leistungsheft JA
	// Leistungsheft NEIN
	// E-Mail-Adresse Hundeführer
	// Adresse Hundeführer
	// Telefonnummer Hundeführer

}
