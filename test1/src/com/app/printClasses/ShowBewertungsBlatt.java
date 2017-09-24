package com.app.printClasses;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.service.TemporaryFileDownloadResource;
import com.app.showData.Show;
import com.app.showData.ShowHund;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

public class ShowBewertungsBlatt extends CustomComponent {
	private PdfReader reader;

	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/BEWERTUNGSBLATT_FORM.pdf";
	public static final String DATASHEET_IHA = "files/BEWERTUNGSBLATT_IHA.pdf";
	public static final String DATASHEET_WESENSTEST = "files/BEWERTUNGSBLATT_WT.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Urkunde.pdf";

	private AbsoluteLayout mainLayout;

	public ShowBewertungsBlatt(Show show, ShowHund... hund) {

		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		setCompositionRoot(mainLayout);

		try {
			fos = new FileOutputStream(RESULT);
			PdfCopyFields copy = new PdfCopyFields(fos);
			copy.open();

			for (int i = 0; i < hund.length; i++) {
				PdfReader zwReader = new PdfReader(bauPdf(show, hund[i]));
				copy.addDocument(zwReader);
				zwReader.close();
			}

			copy.close();

			TemporaryFileDownloadResource s = null;
			s = new TemporaryFileDownloadResource(RESULT, "application/pdf", new File(RESULT));

			BrowserFrame e = new BrowserFrame("ShowBewertungsBlatt", s);
			mainLayout.addComponent(e);

		} catch (final Exception e) {
			e.printStackTrace();

		}

		// e.addDetachListener(mainLayout.getp);

	}

	// Name Hundeführer#
	// Klasse#
	// Rang/Punkte#
	// Name Hund#
	//

	private byte[] bauPdf(Show show, ShowHund hund) throws Exception {

		String vorlage = "";
		if (show.getSchauTyp().equals("C")) {
			vorlage = DATASHEET;
		} else if (show.getSchauTyp().equals("W")) {
			vorlage = DATASHEET_WESENSTEST;
		} else {
			vorlage = DATASHEET_IHA;
		}
		PdfReader reader = new PdfReader(vorlage);
		// fos = new FileOutputStream(RESULT);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, baos);
		stamper.setFormFlattening(true);
		AcroFields fields = stamper.getAcroFields();

		BaseFont unicode = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		fields.addSubstitutionFont(unicode);

		fields.setField("RASSE", hund.getRasse().getRassenLangBezeichnung());
		
		if (show.getSchauTyp().equals("W")) {
			fields.setField("CHIP NR", hund.getChipnummer());
		} else {
			fields.setField("KATALOG NR", hund.getKatalognumer());
			fields.setField("RING NR", hund.getRingNummer());
		}
		
		fields.setField("NAME DES HUNDES", hund.getShowHundName());
		fields.setField("GESCHLECHT", hund.getGeschlecht());
		fields.setField("ZUCHTBUCHNUMMER", hund.getZuchtbuchnummer());
		fields.setField("WURFDATUM", new SimpleDateFormat("dd.MM.yyyy").format(hund.getWurftag()));
		fields.setField("KLASSE", hund.getKlasse().getShowKlassenKurzBezeichnung());
		fields.setField("BESITZER", hund.getBesitzershow());
		fields.setField("DATUM", new SimpleDateFormat("dd.MM.yyyy").format(show.getSchauDate()));
		fields.setField("BEWERTUNGTEXT", hund.getBewertung());

		if (!(hund.getFormwert() == null)) {
			switch (hund.getFormwert()) {
			case "v":
				fields.setField("VORZÜGLICH", "On");
				break;
			case "sg":
				fields.setField("SEHR GUT", "On");
				break;
			case "g":
				fields.setField("GUT", "On");
				break;
			case "ge":
				fields.setField("GENÜGEND", "On");
				break;
			case "d":
				fields.setField("DISQUALIFIZIERT", "On");
				break;
			case "ob":
				fields.setField("OHNE BEWERTUNG", "On");
				break;
			case "V":
				fields.setField("VERSPRECHEND", "On");
				break;
			case "VV":
				fields.setField("VIELVERSPRECHEND", "On");
				break;
			case "NV":
				fields.setField("NICHTVERSPRECHEND", "On");
				break;
			default:

			}
		}

		fields.setField(hund.getPlatzierung(), "On");

		if (!(hund.getCACA() == null)) {
			switch (hund.getCACA()) {
			case "J":
				fields.setField("JUGENDBESTER", "On");
				break;
			case "C":
				fields.setField("CACA", "On");
				break;
			case "V":
				fields.setField("VETERANENSIEGER", "On");
				break;
			case "K":
				fields.setField("KEIN TITEL", "On");
				break;
			case "R":
				fields.setField("RESERVE CACA", "On");
				break;
			case "W":
				break;
			default:

			}
		}

		if (!(hund.getHundfehlt() == null) && hund.getHundfehlt().equals("J")) {
			fields.setField("HUND FEHLT", "On");
		}

		if (!(hund.getBOB() == null)) {
			switch (hund.getBOB()) {
			case "B":
				fields.setField("BOB", "On");
				break;
			case "O":
				fields.setField("BOS", "On");
				break;
			default:
			}
		}

		if (!(hund.getClubsieger() == null)) {
			switch (hund.getClubsieger()) {
			case "J":
				fields.setField("KLUBJUGENDSIEGER", "On");
				break;
			case "C":
				fields.setField("KLUBSIEGER", "On");
				break;
			case "V":
				fields.setField("KLUBVETERANENSIEGER", "On");
				break;
			default:
			}

		}

		if (!(hund.getCACIB() == null)) {
			switch (hund.getCACIB()) {
			case "C":
				fields.setField("CACIB", "On");
				break;
			case "R":
				fields.setField("RESERVE CACIB", "On");
				break;
			default:
			}
		}

		fields.setField("NAME DES RICHTERS", hund.getRichter());

		fields.setField("TITEL", show.getSchaubezeichnung());

		stamper.close();
		reader.close();
		return baos.toByteArray();

	}
}
