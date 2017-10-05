package com.app.printClasses;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringJoiner;

import com.app.dbIO.DBShowNeu;
import com.app.enumPackage.Rassen;
import com.app.enumPackage.ShowKlassen;
import com.app.service.TemporaryFileDownloadResource;
import com.app.showData.Show;
import com.app.showData.ShowHund;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

public class ShowPrintBewertungUebersicht extends CustomComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6116930051112975615L;

	//private PdfReader reader;

	//private FileOutputStream fos;
	/** The original PDF file. */
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Ergebnisse.pdf";

	private AbsoluteLayout mainLayout;
	DBShowNeu db;

	public ShowPrintBewertungUebersicht(Show show, Rassen rasse) {

		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		setCompositionRoot(mainLayout);

		db = new DBShowNeu();

		try {

			File result = new File(RESULT);
			Document document = new Document();

			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
			ParagraphBorder border = new ParagraphBorder();
			writer.setPageEvent(border);
			document.open();

			Font f = new Font(FontFamily.HELVETICA, 25.0f, Font.BOLD); // ,
																		// BaseColor.RED);
			Chunk c = new Chunk(show.getSchaubezeichnung(), f);
			Paragraph paragraph1 = new Paragraph(c);
			paragraph1.setAlignment(Element.ALIGN_CENTER);
			paragraph1.setSpacingAfter(10);
			document.add(paragraph1);

			f = new Font(FontFamily.HELVETICA, 16.0f, Font.BOLD, BaseColor.RED);
			c = new Chunk(rasse.getRassenLangBezeichnung(), f);

			paragraph1 = new Paragraph(c);
			paragraph1.setAlignment(Element.ALIGN_LEFT);
			paragraph1.setSpacingAfter(10);
			document.add(paragraph1);

			List<String> richter = db.getRichterForRasse(show, rasse);
			StringJoiner joiner = new StringJoiner(",", "", "");

			richter.forEach(joiner::add);
			c = new Chunk("   Richter: " + joiner.toString(), f);
			paragraph1 = new Paragraph(c);
			paragraph1.setAlignment(Element.ALIGN_LEFT);
			paragraph1.setSpacingAfter(10);
			document.add(paragraph1);

			for (ShowKlassen x : ShowKlassen.values()) {
				List<ShowHund> hundDerKlassen = db.getHundeForKlasse(show, x, rasse, "R");

				printKlasse(show, document, border, x, hundDerKlassen, "R");

			}

			for (ShowKlassen x : ShowKlassen.values()) {
				List<ShowHund> hundDerKlassen = db.getHundeForKlasse(show, x, rasse, "H");

				printKlasse(show, document, border, x, hundDerKlassen, "H");

			}

			document.close();

			TemporaryFileDownloadResource s = null;
			s = new TemporaryFileDownloadResource(RESULT, "application/pdf", result);

			BrowserFrame e = new BrowserFrame("ShowBewertungsBlatt", s);
			mainLayout.addComponent(e);

		} catch (final Exception e) {
			e.printStackTrace();

		}

		// e.addDetachListener(mainLayout.getp);

	}

	private void printKlasse(Show show, Document document, ParagraphBorder border, ShowKlassen x,
			List<ShowHund> hundDerKlassen, String geschlecht) throws DocumentException, Exception {
		Font f;
		Chunk c;
		Paragraph paragraph1;
		if (hundDerKlassen.size() > 0) {
			f = new Font(FontFamily.HELVETICA, 16.0f, Font.BOLD);
			c = new Chunk((geschlecht.equals("R") ? "Rüden - " : "Hündinnen - ") + x.getShowKlasseLangBezeichnung(), f);
			c.setUnderline(0.1f, -2f);

			paragraph1 = new Paragraph(c);
			paragraph1.setAlignment(Element.ALIGN_LEFT);
			document.add(paragraph1);
			
			

			for (ShowHund hund : hundDerKlassen) {
				
				f = new Font(FontFamily.HELVETICA, 16.0f, Font.BOLD);
				c = new Chunk(hund.getKatalognumer() + " - " + hund.getShowHundName(), f);

				paragraph1 = new Paragraph(c);
				paragraph1.setAlignment(Element.ALIGN_LEFT);
				paragraph1.setIndentationLeft(50);
				document.add(paragraph1);

				f = new Font(FontFamily.HELVETICA, 12.0f, Font.NORMAL);
				c = new Chunk("Besitzer: " + hund.getBesitzershow(), f);
				paragraph1 = new Paragraph(c);
				paragraph1.setAlignment(Element.ALIGN_LEFT);
				paragraph1.setIndentationLeft(50);
				document.add(paragraph1);

				c = new Chunk("Vater: " + hund.getVater(), f);
				paragraph1 = new Paragraph(c);
				paragraph1.setAlignment(Element.ALIGN_LEFT);
				paragraph1.setIndentationLeft(50);
				document.add(paragraph1);

				c = new Chunk("Mutter: " + hund.getMutter(), f);
				paragraph1 = new Paragraph(c);
				paragraph1.setAlignment(Element.ALIGN_LEFT);
				paragraph1.setIndentationLeft(50);
				document.add(paragraph1);

				c = new Chunk("Zuchtbuchnummer: " + hund.getZuchtbuchnummer(), f);
				paragraph1 = new Paragraph(c);
				paragraph1.setAlignment(Element.ALIGN_LEFT);
				paragraph1.setIndentationLeft(50);
				document.add(paragraph1);

				c = new Chunk("Wurftag: " + new SimpleDateFormat("dd.MM.yyyy").format(hund.getWurftag()), f);
				paragraph1 = new Paragraph(c);
				paragraph1.setAlignment(Element.ALIGN_LEFT);
				paragraph1.setIndentationLeft(50);
				document.add(paragraph1);

				border.setActive(true);
				c = new Chunk(hund.getBewertung() == null ? " " : hund.getBewertung(), f);
				paragraph1 = new Paragraph(c);
				paragraph1.setAlignment(Element.ALIGN_LEFT);
				paragraph1.setIndentationLeft(50);
				document.add(paragraph1);
				border.setActive(false);

				f = new Font(FontFamily.HELVETICA, 14.0f, Font.BOLD, BaseColor.RED);
				c = new Chunk("Bewertung: " + getFormwertText(show, hund), f);
				paragraph1 = new Paragraph(c);
				paragraph1.setAlignment(Element.ALIGN_LEFT);
				paragraph1.setIndentationLeft(50);
				paragraph1.setSpacingAfter(25);
				document.add(paragraph1);

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

	private String getFormwertText(Show show, ShowHund hund) throws Exception {

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
				sb.append(hund.getPlatzierung());
			}
		} catch (NumberFormatException e) {

		}

		if (!(hund.getCACA() == null)) {
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

	class ParagraphBorder extends PdfPageEventHelper {
		public boolean active = false;

		public void setActive(boolean active) {
			this.active = active;
		}

		public float offset = 5;
		public float startPosition;

		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			startPosition = document.top();
		}

		@Override
		public void onParagraph(PdfWriter writer, Document document, float paragraphPosition) {
			this.startPosition = paragraphPosition;
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			if (active) {
				PdfContentByte cb = writer.getDirectContentUnder();
				cb.rectangle(document.left() + 50, document.bottom() - offset, document.right() - document.left() - 50,
						startPosition - document.bottom());
				cb.stroke();
			}
		}

		@Override
		public void onParagraphEnd(PdfWriter writer, Document document, float paragraphPosition) {
			if (active) {
				PdfContentByte cb = writer.getDirectContentUnder();
				cb.rectangle(document.left() + 50, paragraphPosition - offset, document.right() - document.left() - 50,
						startPosition - paragraphPosition);
				cb.stroke();
			}
		}
	}
}
