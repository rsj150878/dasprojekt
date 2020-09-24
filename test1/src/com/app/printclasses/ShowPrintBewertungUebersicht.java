package com.app.printclasses;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import com.app.dbio.DBShowNeu;
import com.app.enumdatatypes.Rassen;
import com.app.enumdatatypes.ShowKlassen;
import com.app.service.TemporaryFileDownloadResource;
import com.app.showdata.Show;
import com.app.showdata.ShowHund;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

public class ShowPrintBewertungUebersicht extends CustomComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6116930051112975615L;

	// private PdfReader reader;

	// private FileOutputStream fos;
	/** The original PDF file. */
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Ergebnisse.pdf";

	private AbsoluteLayout mainLayout;

	static float hundTextFontSize = 12;
	DBShowNeu db;

	public ShowPrintBewertungUebersicht(Show show, Rassen rasse) {

		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		setCompositionRoot(mainLayout);

		db = new DBShowNeu();

		try {

			String fileResultName = show.getSchaubezeichnung() + " "
					+ new SimpleDateFormat("dd.MM.yyyy").format(show.getSchauDate()) + "  "
					+ rasse.getRassenLangBezeichnung() + ".pdf";
			fileResultName = fileResultName.replaceAll(" ", "_");

			PdfDocument pdf = new PdfDocument(new PdfWriter(fileResultName));

			// Initialize document
			Document document = new Document(pdf, PageSize.A4);
			PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			PdfFont standard = PdfFontFactory.createFont(FontConstants.HELVETICA);

			// BaseColor.RED);
			Text schauBezeichnung = new Text(show.getSchaubezeichnung()).setFont(bold).setFontSize(25)
					.setHorizontalAlignment(HorizontalAlignment.CENTER);
			Paragraph paragraph1 = new Paragraph(schauBezeichnung).setTextAlignment(TextAlignment.CENTER);
			document.add(paragraph1);

			Text rassenText = new Text(rasse.getRassenLangBezeichnung()).setFont(bold).setFontSize(16)
					.setFontColor(ColorConstants.RED);
			paragraph1 = new Paragraph(rassenText).setTextAlignment(TextAlignment.CENTER);
			document.add(paragraph1);

			List<String> richter = db.getRichterForRasse(show, rasse);
			StringJoiner joiner = new StringJoiner(",", "", "");

			richter.forEach(joiner::add);
			Text richterText = new Text("   Richter: " + joiner.toString()).setFont(bold).setFontSize(16);
			paragraph1 = new Paragraph(richterText);
			document.add(paragraph1);

			for (ShowKlassen x : ShowKlassen.values()) {
				List<ShowHund> hundDerKlassen = db.getHundeForKlasse(show, x, rasse, "R");

				printKlasse(show, document, x, hundDerKlassen, "R");

			}

			for (ShowKlassen x : ShowKlassen.values()) {
				List<ShowHund> hundDerKlassen = db.getHundeForKlasse(show, x, rasse, "H");

				printKlasse(show, document, x, hundDerKlassen, "H");

			}

			document.close();

			TemporaryFileDownloadResource s = null;
			s = new TemporaryFileDownloadResource(fileResultName, "application/pdf", new File(fileResultName));

			BrowserFrame e = new BrowserFrame("ShowBewertungsBlatt", s);
			mainLayout.addComponent(e);

		} catch (final Exception e) {
			e.printStackTrace();

		}

		// e.addDetachListener(mainLayout.getp);

	}

	private void printKlasse(Show show, Document document, ShowKlassen x, List<ShowHund> hundDerKlassen,
			String geschlecht) throws Exception {

		Paragraph paragraph1;

		PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
		PdfFont standard = PdfFontFactory.createFont(FontConstants.HELVETICA);

		if (hundDerKlassen.size() > 0) {

			Text geschlechtText = new Text(
					(geschlecht.equals("R") ? "Rüden - " : "Hündinnen - ") + x.getShowKlasseLangBezeichnung())
							.setFont(bold).setFontSize(14).setUnderline();
			paragraph1 = new Paragraph(geschlechtText);
			paragraph1.setKeepWithNext(true);
			document.add(paragraph1);

			for (ShowHund hund : hundDerKlassen) {

				Div div = new Div().setKeepTogether(true).setPaddingLeft(50).setSpacingRatio(0.5f).setPaddingBottom(20);

				Text hundText = new Text(String.join("",
						Collections.nCopies((hund.getKatalognumer() + " - " + hund.getShowHundName()).length(), "x")))
								.setFont(bold).setFontSize(14);

				paragraph1 = new Paragraph(hundText);
				paragraph1.setMarginBottom(0);
				if (!hund.getVeroeffentlichen()) {
					hundText.setBackgroundColor(ColorConstants.BLACK);
				} else {
					hundText.setText(hund.getKatalognumer() + " - " + hund.getShowHundName());
				}
				div.add(paragraph1);

				Text besitzerText = new Text("Besitzer: ").setFont(bold).setFontSize(hundTextFontSize);
				paragraph1 = new Paragraph(besitzerText);
				besitzerText = new Text(String.join("", Collections.nCopies(hund.getBesitzershow().length(), "x")))
						.setFont(standard).setFontSize(hundTextFontSize);
				if (!hund.getVeroeffentlichen()) {
					besitzerText.setBackgroundColor(ColorConstants.BLACK);
				} else {
					besitzerText.setText(hund.getBesitzershow());
				}

				paragraph1.add(besitzerText);
				paragraph1.setMarginBottom(0);
				paragraph1.setMarginTop(0);
				div.add(paragraph1);

				Text vaterText = new Text("Vater: ").setFontSize(hundTextFontSize).setFont(bold);
				paragraph1 = new Paragraph(vaterText);

				vaterText = new Text(String.join("", Collections.nCopies(hund.getVater().length(), "x")))
						.setFontSize(hundTextFontSize).setFont(standard);
				if (!hund.getVeroeffentlichen()) {
					vaterText.setBackgroundColor(ColorConstants.BLACK);
				} else {
					vaterText.setText(hund.getVater());
				}
				paragraph1.add((vaterText));
				paragraph1.setMarginBottom(0);
				paragraph1.setMarginTop(0);
				div.add(paragraph1);

				Text mutterText = new Text("Mutter: ").setFontSize(hundTextFontSize).setFont(bold);
				paragraph1 = new Paragraph(mutterText);

				mutterText = new Text(String.join("", Collections.nCopies(hund.getMutter().length(), "x")))
						.setFontSize(hundTextFontSize).setFont(standard);
				if (!hund.getVeroeffentlichen()) {
					mutterText.setBackgroundColor(ColorConstants.BLACK);
				} else {
					mutterText.setText(hund.getMutter());
				}
				paragraph1.add(mutterText);
				paragraph1.setMarginBottom(0);
				paragraph1.setMarginTop(0);
				div.add(paragraph1);

				Text zbnrText = new Text("Zuchtbuchnummer: ").setFont(bold).setFontSize(hundTextFontSize);
				paragraph1 = new Paragraph(zbnrText);
				zbnrText = new Text(String.join("", Collections.nCopies(hund.getZuchtbuchnummer().length(), "x")))
						.setFont(standard).setFontSize(hundTextFontSize);
				if (!hund.getVeroeffentlichen()) {
					zbnrText.setBackgroundColor(ColorConstants.BLACK);
				} else {
					zbnrText.setText(hund.getZuchtbuchnummer());
				}
				paragraph1.add(zbnrText);
				paragraph1.add(new Tab());
				paragraph1.addTabStops(new TabStop(1000, TabAlignment.RIGHT));

				Text wurfTagText = new Text("Wurftag: ").setFontSize(hundTextFontSize).setFont(bold);
				paragraph1.add(wurfTagText);
				wurfTagText = new Text(String.join("", Collections.nCopies(10, "x"))).setFontSize(hundTextFontSize)
						.setFont(standard);
				if (!hund.getVeroeffentlichen()) {
					wurfTagText.setBackgroundColor(ColorConstants.BLACK);
				} else {
					wurfTagText.setText(new SimpleDateFormat("dd.MM.yyyy").format(hund.getWurftag()));
				}
				paragraph1.add(wurfTagText);
				paragraph1.setMarginBottom(0);
				paragraph1.setMarginTop(0);
				div.add(paragraph1);

				Text bewertungText = new Text("\n")
						.setFontSize(hundTextFontSize).setFont(standard);
				paragraph1 = new Paragraph(bewertungText);

				if (!hund.getVeroeffentlichen()) {
					bewertungText.setBackgroundColor(ColorConstants.BLACK);
				} else {
					bewertungText.setText(
							hund.getBewertung() == null || hund.getBewertung().isEmpty() ? "\n" : hund.getBewertung());

				}
				paragraph1.setBorder(new SolidBorder(.5f));
				paragraph1.setMarginBottom(0);
				paragraph1.setMarginTop(0);
				div.add(paragraph1);

				Text formWertText = new Text("Bewertung: ").setFontSize(14).setFont(bold)
						.setFontColor(ColorConstants.RED);
				paragraph1 = new Paragraph(formWertText);

				formWertText = new Text(String.join("", Collections.nCopies(20, "x"))).setFontSize(14).setFont(bold)
						.setFontColor(ColorConstants.RED);
				paragraph1 = paragraph1.add(formWertText);
				if (!hund.getVeroeffentlichen()) {
					formWertText.setBackgroundColor(ColorConstants.RED);
				} else {
					formWertText.setText(getFormwertText(show,hund));
				}
				paragraph1.setMarginBottom(0);
				paragraph1.setMarginTop(0);
				div.add(paragraph1);

				document.add(div);
				// paragraph1.setAlignment(Element.ALIGN_LEFT);
				// document.add(paragraph1);
			}
		}
	}

	// Name Hundeführer#
	// Klasse#
	// Rang/Punkte#
	// Name Hund#
	//

	public static String getFormwertText(Show show, ShowHund hund) throws Exception {

		StringBuilder sb = new StringBuilder();
		if (!(hund.getFormwert() == null)) {
			switch (hund.getFormwert()) {
			case "v":
				sb.append("V");
				break;
			case "sg":
				sb.append("SG");
				break;
			case "g":
				sb.append("GUT");
				break;
			case "ge":
				sb.append("GENÜGEND");
				break;
			case "d":
				sb.append("Disqu.");
				break;
			case "ob":
				sb.append("OHNE BEWERTUNG");
				break;
			case "V":
				sb.append("VERSPRECHEND");
				break;
			case "VV":
				sb.append("VIELVERSPRECHEND");
				break;
			case "NV":
				sb.append("NICHT VERSPRECHEND");
				break;
			default:

			}
		}

		try {
			if (!(hund.getPlatzierung() == null) && new Integer(hund.getPlatzierung()).intValue() > 0) {
				sb.append(" " + hund.getPlatzierung());
			}
		} catch (NumberFormatException e) {

		}

		if (!(hund.getCACA() == null) && !hund.getCACA().equals("K")) {
			sb.append(", ");
			switch (hund.getCACA()) {
			case "J":
				sb.append("JB");
				break;
			case "C":
				sb.append("CACA");
				break;
			case "V":
				sb.append("VS");
				break;
			// case "K":
			// fields.setField("KEIN TITEL", "On");
			// break;
			case "R":
				sb.append("Res. CACA");
				break;
			case "W":
				sb.append("Res. VS");
				break;
			default:

			}
		}

		if (!(hund.getHundfehlt() == null) && hund.getHundfehlt().equals("J")) {
			sb.append("HUND FEHLT");
		}

		if (!(hund.getCACIB() == null)) {
			sb.append(", ");
			switch (hund.getCACIB()) {
			case "C":
				sb.append("CACIB");
				break;
			case "R":
				sb.append("Res. CACIB");
				break;
			default:
			}
		}

		if (!(hund.getVBOB() == null)) {
			sb.append(", ");
			switch (hund.getVBOB()) {
			case "J":
				sb.append("VBOB");
				break;
			default:
			}
		}
		
		if (!(hund.getJBOB() == null)) {
			sb.append(", ");
			switch (hund.getJBOB()) {
			case "J":
				sb.append("JBOB");
				break;
			default:
			}
		}
	
		
		if (!(hund.getBOB() == null)) {
			sb.append(", ");
			switch (hund.getBOB()) {
			case "B":
				sb.append("BOB");
				break;
			case "O":
				sb.append("BOS");
				break;
			default:
			}
		}

		if (!(hund.getClubsieger() == null)) {
			sb.append(", ");
			switch (hund.getClubsieger()) {
			case "J":
				sb.append("KLUBJUGENDSIEGER");
				break;
			case "C":
				sb.append("KLUBSIEGER");
				break;
			case "V":
				sb.append("KLUBVETERANENSIEGER");
				break;
			default:
			}

		}

		return sb.toString();
	}

}
