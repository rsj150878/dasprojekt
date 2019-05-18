package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.service.TemporaryFileDownloadResource;
import com.app.showdata.Show;
import com.app.showdata.ShowHund;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

public class ShowBewertungsBlatt extends CustomComponent {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8769572414405768707L;
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

			bauPdf(show, hund);

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

	private void bauPdf(Show show, ShowHund... hund) throws Exception {

		String vorlage = "";
		if (show.getSchauTyp().equals("C")) {
			vorlage = DATASHEET;
		} else if (show.getSchauTyp().equals("W")) {
			vorlage = DATASHEET_WESENSTEST;
		} else {
			vorlage = DATASHEET_IHA;
		}

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(RESULT));
		pdfDoc.initializeOutlines();

		ByteArrayOutputStream baos;
		PdfDocument pdfInnerDoc;
		Map<String, PdfFormField> fields;
		PdfAcroForm form;


		for (int i = 0; i < hund.length; i++) {
			baos = new ByteArrayOutputStream();
			pdfInnerDoc = new PdfDocument(new PdfReader(vorlage), new PdfWriter(baos));
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();

			fields.get("RASSE").setValue(hund[i].getRasse().getRassenLangBezeichnung());

			if (show.getSchauTyp().equals("W")) {
				fields.get("CHIP NR").setValue(hund[i].getChipnummer());
			} else {
				fields.get("KATALOG NR").setValue(hund[i].getKatalognumer());
				fields.get("RING NR").setValue(hund[i].getRingNummer());
			}

			fields.get("NAME DES HUNDES").setValue(hund[i].getShowHundName());
			fields.get("GESCHLECHT").setValue(hund[i].getGeschlecht());
			fields.get("ZUCHTBUCHNUMMER").setValue(hund[i].getZuchtbuchnummer());
			fields.get("WURFDATUM").setValue(new SimpleDateFormat("dd.MM.yyyy").format(hund[i].getWurftag()));
			fields.get("KLASSE").setValue(hund[i].getKlasse().getShowKlassenKurzBezeichnung());
			fields.get("BESITZER").setValue(hund[i].getBesitzershow());
			fields.get("DATUM").setValue(new SimpleDateFormat("dd.MM.yyyy").format(show.getSchauDate()));
			fields.get("BEWERTUNGTEXT").setValue(hund[i].getBewertung() == null ? " " :hund[i].getBewertung());

			if (!(hund[i].getFormwert() == null)) {
				switch (hund[i].getFormwert()) {
				case "v":
					fields.get("VORZÜGLICH").setValue("On");
					break;
				case "sg":
					fields.get("SEHR GUT").setValue("On");
					break;
				case "g":
					fields.get("GUT").setValue("On");
					break;
				case "ge":
					fields.get("GENÜGEND").setValue("On");
					break;
				case "d":
					fields.get("DISQUALIFIZIERT").setValue("On");
					break;
				case "ob":
					fields.get("OHNE BEWERTUNG").setValue("On");
					break;
				case "V":
					fields.get("VERSPRECHEND").setValue("On");
					break;
				case "VV":
					fields.get("VIELVERSPRECHEND").setValue("On");
					break;
				case "NV":
					fields.get("NICHTVERSPRECHEND").setValue("On");
					break;
				default:

				}
			}

			if (!(hund[i].getPlatzierung() == null )) {
			
				fields.get(hund[i].getPlatzierung()).setValue("On");
			}

			if (!(hund[i].getCACA() == null)) {
				switch (hund[i].getCACA()) {
				case "J":
					fields.get("JUGENDBESTER").setValue("On");
					break;
				case "C":
					fields.get("CACA").setValue("On");
					break;
				case "V":
					fields.get("VETERANENSIEGER").setValue("On");
					break;
				case "K":
					fields.get("OHNE TITEL").setValue("On");
					break;
				case "R":
					fields.get("RESERVE CACA").setValue("On");
					break;
				case "W":
					break;
				default:

				}
			}

			if (!(hund[i].getHundfehlt() == null) && hund[i].getHundfehlt().equals("J")) {
				fields.get("HUND FEHLT").setValue("On");
			}

			if (!(hund[i].getBOB() == null)) {
				switch (hund[i].getBOB()) {
				case "B":
					fields.get("BOB").setValue("On");
					break;
				case "O":
					fields.get("BOS").setValue("On");
					break;
				default:
				}
			}

			if (!(hund[i].getClubsieger() == null)) {
				switch (hund[i].getClubsieger()) {
				case "J":
					fields.get("KLUBJUGENDSIEGER").setValue("On");
					break;
				case "C":
					fields.get("KLUBSIEGER").setValue("On");
					break;
				case "V":
					fields.get("KLUBVETERANENSIEGER").setValue("On");
					break;
				default:
				}

			}

			if (!(hund[i].getCACIB() == null)) {
				switch (hund[i].getCACIB()) {
				case "C":
					fields.get("CACIB").setValue("On");
					break;
				case "R":
					fields.get("RESERVE CACIB").setValue("On");
					break;
				default:
				}
			}

			fields.get("NAME DES RICHTERS").setValue(hund[i].getRichter());

			fields.get("TITEL").setValue(show.getSchaubezeichnung());

			form.flattenFields();
			pdfInnerDoc.close();

			pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
			pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
			pdfInnerDoc.close();
		}
		pdfDoc.close();

	}
}
