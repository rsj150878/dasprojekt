package com.app.printClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.dbIO.DBConnection;
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

public class BewertungsListeBGH extends CustomComponent {

	private PdfReader reader;
	private PdfStamper stamper;
	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/Bewertungsliste_ISA.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Bewertungslisteprint.pdf";

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	public BewertungsListeBGH(Item veranstaltung, Item veranstaltungsStufe) {

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

			for (int i = 0; i < teilnehmerContainer.size(); i = i + 8) {
				List<Object> list = teilnehmerContainer.getItemIds(i, 8);
				PdfReader zwReader = new PdfReader(bauPdf(veranstaltung,
						veranstaltungsStufe, list));
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
			List<Object> listTeilnehmer) throws Exception {
		PdfReader reader = new PdfReader(DATASHEET);
		// fos = new FileOutputStream(RESULT);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stamper = new PdfStamper(reader, baos);
		AcroFields fields = stamper.getAcroFields();

		BaseFont unicode = BaseFont.createFont(FONT, BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED);
		fields.addSubstitutionFont(unicode);

		fields.setField("Prüfungsordnung", "OEPO");
		fields.setField("Verbandskörperschaft", "Österreichischer Retrieverclub");
		fields.setField(
				"Prüfungsstufe",
				VeranstaltungsStufen.getBezeichnungForId(
						new Integer(veranstaltungsStufe
								.getItemProperty("stufen_typ").getValue()
								.toString())).getBezeichnung());
		fields.setField("Art der Prüfung", "P");
		fields.setField("Ortsgruppe",
				veranstaltung.getItemProperty("veranstalter").getValue()
						.toString());
		fields.setField("Leistungsrichter B",
				veranstaltung.getItemProperty("richter").getValue().toString());
		DateFormat dateFormat1 = new SimpleDateFormat("dd");
		fields.setField("Tag 1", dateFormat1.format(veranstaltung
				.getItemProperty("datum").getValue()));
		fields.setField("Tag 2", dateFormat1.format(veranstaltung
				.getItemProperty("datum").getValue()));

		dateFormat1 = new SimpleDateFormat("MM");
		fields.setField("Monat", dateFormat1.format(veranstaltung
				.getItemProperty("datum").getValue()));

		dateFormat1 = new SimpleDateFormat("yyyy");
		fields.setField("Jahr", dateFormat1.format(veranstaltung
				.getItemProperty("datum").getValue()));

		fields.setField("Name Prüfungsleiter",
				veranstaltung.getItemProperty("veranstaltungsleiter")
						.getValue().toString());

		fields.setField("Wohnort Prüfungsleiter", veranstaltung
				.getItemProperty("ort_leiter").getValue().toString()
				+ ", "
				+ veranstaltung.getItemProperty("strasse_leiter").getValue()
						.toString());

		fields.setField("Postleizahl Prüfungsleiter", veranstaltung
				.getItemProperty("plz_leiter").getValue().toString());

		fields.setField("Telefonnummer Prüfungsleiter", veranstaltung
				.getItemProperty("tel_nr_leiter").getValue().toString());

		for (int i = 1; i <= 8; i++) {
			fields.setField("Rüde " + i, "xx");
			fields.setField("Chip " + i, "xxxxxxxxxxxxxxxx");
			fields.setField("WT " + i, "xxxxxxxxxxxxxxx");

			fields.setField("Name " + i, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			fields.setField("Gesamtpunktezahl " + i, "xxxx");
			fields.setField("Gesamtnote " + i, "xxxxx");

			fields.setField("SZ " + i, "xxxxxx");
			fields.setField("Punktezahl C " + i, "xxx");
			fields.setField("OG-Nummer " + i, "xxxxxx");
			fields.setField("Punktezahl A " + i, "xxx");
			fields.setField("Hund " + i, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			fields.setField("Punktezahl B " + i, "xxx");

			fields.setField("Wesensprobe bestanden Ja/nein", "N");
			fields.setField("Hündin " + i, "xx");
			fields.setField("Punktezahl TSB " + i, "xxxx");

			fields.setField("Postleitzahl " + i, "xxxxxxx");
			fields.setField("Adresse " + i,
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			fields.setField("Ort " + i,
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		}

		Iterator it = listTeilnehmer.iterator();
		int i = 1;
		System.out.println(listTeilnehmer.size());
		while (it.hasNext()) {
			Object id = it.next();
			System.out.println(id);
			hundContainer.addContainerFilter(new Equal("idhund",
					teilnehmerContainer.getItemUnfiltered(id)
							.getItemProperty("id_hund").getValue()));

			personContainer.addContainerFilter(new Equal("idperson",
					teilnehmerContainer.getItemUnfiltered(id).getItemProperty(
							"id_person").getValue()));

			fields.setField("Hund " + i,
					hundContainer.getItem(hundContainer.firstItemId())
							.getItemProperty("zwingername").getValue()
							.toString());
			fields.setField("Chip " + i,
					hundContainer.getItem(hundContainer.firstItemId())
							.getItemProperty("chipnummer").getValue()
							.toString());

			if ("R".equals(hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("geschlecht").getValue().toString())) {
				fields.setField("Rüde " + i, "R");
				fields.setField("Hündin " + i, "");
			} else {
				fields.setField("Rüde " + i, "");
				fields.setField("Hündin " + i, "H");
			}

			dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
			fields.setField(
					"WT " + i,
					dateFormat1.format(hundContainer
							.getItem(hundContainer.firstItemId())
							.getItemProperty("wurfdatum").getValue()));

			if (hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("zuchtbuchnummer").getValue() != null) {
				fields.setField("SZ " + i,
						hundContainer.getItem(hundContainer.firstItemId())
								.getItemProperty("zuchtbuchnummer").getValue()
								.toString());
			} else {
				fields.setField("SZ " + i, "");
			}

			fields.setField("Rasse " + i,
					hundContainer.getItem(hundContainer.firstItemId())
							.getItemProperty("rasse").getValue().toString());

			fields.setField("Punktezahl TSB " + i, "");
			fields.setField("Punktezahl A " + i, "");
			fields.setField("Punktezahl B " + i, "");
			fields.setField("Punktezahl C " + i, "");
			fields.setField("Gesamtpunktezahl " + i, "");
			fields.setField("Gesamtnote " + i, "");
			fields.setField("OG-Nummer " + i, "");

			if (teilnehmerContainer.getItemUnfiltered(id)
					.getItemProperty("hundefuehrer").getValue() != null) {

				fields.setField("Name " + i, teilnehmerContainer
						.getItemUnfiltered(id).getItemProperty("hundefuehrer")
						.getValue().toString());
			} else {
				fields.setField(
						"Name " + i,
						personContainer.getItem(personContainer.firstItemId())
								.getItemProperty("nachname").getValue()
								.toString()
								+ " "
								+ personContainer
										.getItem(personContainer.firstItemId())
										.getItemProperty("vorname").getValue()
										.toString()

				);
			}
			fields.setField("Postleitzahl " + i,
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("plz").getValue().toString());
			fields.setField(
					"Adresse " + i,
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("strasse").getValue().toString()
							+ " "
							+ personContainer
									.getItem(personContainer.firstItemId())
									.getItemProperty("hausnummer").getValue()
									.toString());
			
			fields.setField("Ort " + i,
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("ort").getValue().toString());

			
			hundContainer.removeAllContainerFilters();
			personContainer.removeAllContainerFilters();
			i++;
		}
		stamper.close();
		reader.close();
		return baos.toByteArray();
	}
	// Punktezahl TSB 1..8
	// Rüde 1..8
	// Rüde
	// Chip 1..8
	// WT 1..8
	// Ort 1..8
	// Name 1..8
	// Gesamtpunktezahl 1..8
	// Mitgliedsnummer 1..8
	// Gesamtnote 1..8
	// Adresse 1..8
	// Rasse 1..8
	// SZ 1..8
	// Punktezahl C 1..8
	// OG-Nummer 1..8
	// Punktezahl A 1..8
	// Hund 1..8
	// Punktezahl B 1..8
	// Postleitzahl 1..8
	// Wesensprobe bestanden Ja/nein 1..8
	// Hündin 1..8

	// LR-C-Nummer
	// LR-B-Nummer
	// Ortsgruppe, erl
	// Tag 1, erl
	// Tag 2, erl
	// Art der Prüfung, erl.
	// Jahr, erl
	// Wohnort Prüfungsleiter
	// a
	// Nummer
	// Prüfungsstufe, erl.
	// Prüfungsordnung, erl
	// Leistungsrichter C
	// Leistungsrichter A
	// Leistungsrichter B, erl
	// Postleizahl Prüfungsleiter, erl.
	// Monat, erl
	// LR-A-Nummer
	// Telefonnummer Prüfungsleiter
	// Name Prüfungsleiter, erl

}
