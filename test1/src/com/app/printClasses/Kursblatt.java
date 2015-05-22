package com.app.printClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.datetime.DateFormatter;

import com.app.bean.RassenBean;
import com.app.enumPackage.Rassen;
import com.app.service.TemporaryFileDownloadResource;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.vaadin.data.Item;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

public class Kursblatt extends CustomComponent {

	private AbsoluteLayout mainLayout;

	// Check Box JuHu
	// Check Box EFA
	// Check Box Welpen 2
	// Adresse_2
	// Check Box Welpen
	// Check Box Bezahlt
	// Check Box Sonntag
	// Check Box Apportieren
	// Name_2
	// Leistungsheft
	// Check Box Mittwoch
	// Betrag
	// Name
	// am
	// Check Box NEIN
	// E-Mail-Adresse_2
	// Telefonnummer_2

	private PdfReader reader;
	private PdfStamper stamper;
	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/Kursblatt NEU_Formular.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "kursblattprint1.pdf";

	public Kursblatt(Item besitzer, Item hund) {
		try {
			mainLayout = new AbsoluteLayout();
			mainLayout.setImmediate(false);
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			reader = new PdfReader(DATASHEET);
			fos = new FileOutputStream(RESULT);
			stamper = new PdfStamper(reader, fos);
			AcroFields fields = stamper.getAcroFields();

			BaseFont unicode = BaseFont.createFont(FONT, BaseFont.IDENTITY_H,
					BaseFont.EMBEDDED);
			fields.addSubstitutionFont(unicode);
			// besitzer-block

			if (!(besitzer.getItemProperty("email").getValue() == null)) {
				fields.setField("E-Mail-Adresse",
						besitzer.getItemProperty("email").getValue().toString());

			}
			fields.setField("Adresse", besitzer.getItemProperty("strasse")
					.getValue()
					+ " "
					+ besitzer.getItemProperty("hausnummer").getValue()
					+ ", "
					+ besitzer.getItemProperty("plz").getValue()
					+ " "
					+ besitzer.getItemProperty("ort").getValue());

			String titel = "";

			if (!(besitzer.getItemProperty("titel").getValue() == null)) {
				titel = besitzer.getItemProperty("titel").getValue() + " ";
			}

			fields.setField("Name", titel
					+ besitzer.getItemProperty("vorname").getValue() + " "
					+ besitzer.getItemProperty("nachname").getValue());

			Date currentDate = new Date();
			DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");

			if (!(besitzer.getItemProperty("geb_dat").getValue() == null)) {
				fields.setField("Geburtsdatum", dateFormat1.format(besitzer
						.getItemProperty("geb_dat").getValue()));
			}

			if (!(besitzer.getItemProperty("oerc_mitgliedsnummer").getValue() == null)) {
				fields.setField("ÖRC-Mitgliedsnummer", besitzer
						.getItemProperty("oerc_mitgliedsnummer").getValue()
						.toString());
			}

			if (besitzer.getItemProperty("newsletter").getValue().toString()
					.equals("J")) {
				fields.setField("Check Box JA", "Ja");
			} else {
				fields.setField("Check Box NEIN", "Ja");
			}

			if (!(besitzer.getItemProperty("mobnr").getValue() == null)) {
				fields.setField("Mobiltelefonnummer",
						besitzer.getItemProperty("mobnr").getValue().toString());
			}

			if (!(besitzer.getItemProperty("telnr").getValue() == null)) {
				fields.setField("Telefonnummer",
						besitzer.getItemProperty("telnr").getValue().toString());
			}

			fields.setField("Schranawand am", dateFormat1.format(currentDate));

			// ende besitzer

			// beginn hund

			fields.setField("Wurfdatum", dateFormat1.format(hund
					.getItemProperty("wurfdatum").getValue()));

			if (hund.getItemProperty("geschlecht").getValue().toString()
					.equals("R")) {
				fields.setField("Geschlecht", "Rüde");
			} else {
				fields.setField("Geschlecht", "Hündin");
			}

			if (!(hund.getItemProperty("zuchtbuchnummer").getValue() == null)) {
				fields.setField("Zuchtbuchnummer",
						hund.getItemProperty("zuchtbuchnummer").getValue()
								.toString());
			}

			fields.setField("Chipnummer", hund.getItemProperty("chipnummer")
					.getValue().toString());

			if (!(hund.getItemProperty("zuechter").getValue() == null)) {
				fields.setField("Züchter", hund.getItemProperty("zuechter")
						.getValue().toString());
			}

			if (!(hund.getItemProperty("zwingername").getValue() == null)) {
				fields.setField("Name des Hundes lt. Ahnentafel", hund
						.getItemProperty("zwingername").getValue().toString());
			}

			fields.setField("Rufname", hund.getItemProperty("rufname")
					.getValue().toString());

			if (hund.getItemProperty("farbe").getValue() != null) {
				fields.setField("Farbe", hund.getItemProperty("farbe")
						.getValue().toString());
			}

			for (Rassen o : Rassen.values()) {
				RassenBean addObject = new RassenBean(
						o.getRassenKurzBezeichnung(),
						o.getRassenLangBezeichnung());

				if (hund.getItemProperty("rasse").getValue() != null
						&& o.getRassenKurzBezeichnung().equals(
								hund.getItemProperty("rasse").getValue()
										.toString())) {
					fields.setField("Rasse", o.getRassenLangBezeichnung());
				}

			}

			// ende hund

			stamper.close();
			reader.close();

			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT,
						"application/pdf", new File(RESULT));
			} catch (final FileNotFoundException e) {

			}

			// StreamSource s = new StreamResource.StreamSource() {
			//
			// @Override
			// public InputStream getStream() {
			// try {
			// File f = new File(RESULT);
			// FileInputStream fis = new FileInputStream(f);
			// return fis;
			// } catch (Exception e) {
			// e.printStackTrace();
			// return null;
			// }
			// }
			// };

			// StreamResource r = new StreamResource(s, RESULT);

			BrowserFrame e = new BrowserFrame("kursblatt", s);
			mainLayout.addComponent(e);

		} catch (Exception ee) {
			ee.printStackTrace();
		}

	}
}
