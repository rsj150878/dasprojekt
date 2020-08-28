package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
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

	public static final String ZERTIFIKAT_VORLAGE = "files/URKUNDE_CSS_2020.pdf";

	public static final String RESULT = "Bewertung.pdf";
	public static final String ZERTIFIKAT = "Zertifikat.pdf";

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

	public ShowBewertungsBlatt(Show show, String dokument, ShowHund... hund) {

		mainLayout = new AbsoluteLayout();
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		setCompositionRoot(mainLayout);

		try {

			TemporaryFileDownloadResource s = null;

			if (dokument.equals("BW")) {
				bauPdf(show, hund);
				s = new TemporaryFileDownloadResource(RESULT, "application/pdf", new File(RESULT));

			} else {
				bauPdfZertifikat(show, hund);
				s = new TemporaryFileDownloadResource(ZERTIFIKAT, "application/pdf", new File(ZERTIFIKAT));

			}

			BrowserFrame e = new BrowserFrame("ShowBewertungsBlatt", s);
			mainLayout.addComponent(e);

		} catch (final Exception e) {
			e.printStackTrace();

		}

		// e.addDetachListener(mainLayout.getp);

	}

	public void sendBewertungAsEmail(Show show, ShowHund... hund) throws Exception {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		//prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "mail.mymagenta.business");
		prop.put("mail.smtp.port", "587");
		//prop.put("mail.smtp.ssl.trust", "mail.mymagenta.business");
		
//		prop.put("mail.smtp.auth", true);
//		prop.put("mail.smtp.starttls.enable", "true");
//		prop.put("mail.smtp.host", "smtp.world4you.com");
//		prop.put("mail.smtp.port", "587");
//		prop.put("mail.smtp.ssl.trust", "smtp.world4you.com");

		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("show@retrieverclub.at", "?Show66");
			}
		}); // show@retrieverclub.at ?Show66

		for (int i = 0; i < hund.length; i++) {

			System.out.println("mail an " + hund[i].getBesitzerEmail() + " vorbereitet");
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("show@retrieverclub.at"));

			if (hund[i].getBesitzerEmail() == null || hund[i].getBesitzerEmail().isEmpty()) {
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse("stefan@retrieverebreichsdorf.at"));
			} else if (!(hund[i].getHundfehlt() == null) && hund[i].getHundfehlt().equals("N")) {
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse("stefan@retrieverebreichsdorf.at"));
			} else {
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(hund[i].getBesitzerEmail()));
			}

			message.setSubject("Bewertungsblatt und Urkunde");

			StringBuilder msg = new StringBuilder();
			msg.append("Werter Aussteller!");
			msg.append("<br>");
			msg.append("Im Anhang erhalten Sie das Bewertungsblatt und die Urkunde Ihres Hundes "
					+ hund[i].getShowHundName() + " ");
			msg.append("der Ausstellung " + show.getSchaubezeichnung());
			msg.append("<br>");
			msg.append("<br>");
			msg.append(
					"Wir danken für Ihren Besuch!<br>");
			msg.append("<br>");
			msg.append("Besuchen Sie doch auch das Retriever Festival am 25.09.2020!<br>");
			msg.append("Nutzen Sie die Möglichkeit, bis zum 31. August 2020 noch vergünstigt zu melden!<br>");
			msg.append("Meldungen sind unter <a href=\"www.hundeausstellungen.at\">www.hundeausstellungen.at</a> möglich!");
			msg.append("<br>");
			msg.append("<br>");
			msg.append("mit freundlichen Grüßen<br>");
			msg.append("<br>");
			msg.append("Das ÖRC Show Referat");

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(msg.toString(), "text/html; charset=UTF-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			bauPdf(show, hund[i]);
			attachmentBodyPart.attachFile(new File(RESULT));
			multipart.addBodyPart(attachmentBodyPart);

			attachmentBodyPart = new MimeBodyPart();
			bauPdfZertifikat(show, hund[i]);
			attachmentBodyPart.attachFile(new File(ZERTIFIKAT));
			multipart.addBodyPart(attachmentBodyPart);

			message.setContent(multipart);

			Transport.send(message);
			

			System.out.println("mail verschickt");
		}
	}

	// Name Hundeführer#
	// Klasse#
	// Rang/Punkte#
	// Name Hund#
	//

	private void bauPdfZertifikat(Show show, ShowHund... hund) throws Exception {

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(ZERTIFIKAT));
		pdfDoc.initializeOutlines();

		ByteArrayOutputStream baos;
		PdfDocument pdfInnerDoc;
		Map<String, PdfFormField> fields;
		PdfAcroForm form;

		for (int i = 0; i < hund.length; i++) {
			baos = new ByteArrayOutputStream();
			pdfInnerDoc = new PdfDocument(new PdfReader(ZERTIFIKAT_VORLAGE), new PdfWriter(baos));
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();

			fields.get("rasse").setValue(hund[i].getRasse().getRassenLangBezeichnung());

			fields.get("katalognr").setValue("Kat.-Nr.: " + hund[i].getKatalognumer());

			fields.get("hundename").setValue(hund[i].getShowHundName().trim());
			fields.get("klasse").setValue(hund[i].getKlasse().getShowKlasseLangBezeichnung());
			fields.get("besitzer").setValue(hund[i].getBesitzershow().trim());
			Locale locale = new Locale("de", "DE");
			fields.get("va-datum").setValue(new SimpleDateFormat("dd. MMMM yyyy", locale).format(show.getSchauDate()));

			fields.get("formwert").setValue("Formwert: " + ShowPrintBewertungUebersicht.getFormwertText(show, hund[i]));
			form.flattenFields();
			pdfInnerDoc.close();

			pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
			pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
			pdfInnerDoc.close();
		}
		pdfDoc.close();

	}

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

			fields.get("NAME DES HUNDES").setValue(hund[i].getShowHundName().trim());
			fields.get("GESCHLECHT").setValue(hund[i].getGeschlecht());
			fields.get("ZUCHTBUCHNUMMER").setValue(hund[i].getZuchtbuchnummer());
			fields.get("WURFDATUM").setValue(new SimpleDateFormat("dd.MM.yyyy").format(hund[i].getWurftag()));
			fields.get("KLASSE").setValue(hund[i].getKlasse().getShowKlassenKurzBezeichnung());
			fields.get("BESITZER").setValue(hund[i].getBesitzershow().trim());
			fields.get("DATUM").setValue(new SimpleDateFormat("dd.MM.yyyy").format(show.getSchauDate()));
			fields.get("BEWERTUNGTEXT").setValue(hund[i].getBewertung() == null ? " " : hund[i].getBewertung());

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

			if (!(hund[i].getPlatzierung() == null)) {

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
