package com.app.printClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.dbIO.DBConnection;
import com.app.enumPackage.Rassen;
import com.app.service.TemporaryFileDownloadResource;
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

public class WesentestFormwert extends CustomComponent {

	private PdfReader reader;
	private PdfStamper stamper;
	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/Formwertblatt_WT_FORM-neu.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "FormWertBlatt.pdf";

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	public WesentestFormwert(Item veranstaltung, Item veranstaltungsStufe) {

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

		q3 = new TableQuery("veranstaltungs_teilnehmer", DBConnection.INSTANCE.getConnectionPool());
		q3.setVersionColumn("version");

		q4 = new TableQuery("person", DBConnection.INSTANCE.getConnectionPool());
		q4.setVersionColumn("version");

		q5 = new TableQuery("hund", DBConnection.INSTANCE.getConnectionPool());
		q5.setVersionColumn("version");

		try {

			personContainer = new SQLContainer(q4);
			hundContainer = new SQLContainer(q5);
			teilnehmerContainer = new SQLContainer(q3);

			teilnehmerContainer.addContainerFilter(
					new Equal("id_stufe", veranstaltungsStufe.getItemProperty("id_stufe").getValue()));

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			fos = new FileOutputStream(RESULT);
			PdfCopyFields copy = new PdfCopyFields(fos);
			copy.open();

			for (Object id : teilnehmerContainer.getItemIds()) {
				PdfReader zwReader = new PdfReader(
						bauPdf(veranstaltung, veranstaltungsStufe, teilnehmerContainer.getItem(id)));
				copy.addDocument(zwReader);
				zwReader.close();
			}

			copy.close();

			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT, "application/pdf", new File(RESULT));
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("WesenstestFormwert", s);
			mainLayout.addComponent(e);

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private byte[] bauPdf(Item veranstaltung, Item veranstaltungsStufe, Item teilnehmerItem) throws Exception {

		PdfReader reader = new PdfReader(DATASHEET);
		// fos = new FileOutputStream(RESULT);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		stamper = new PdfStamper(reader, baos);
		stamper.setFormFlattening(true);
		
		AcroFields fields = stamper.getAcroFields();

		BaseFont unicode = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		fields.addSubstitutionFont(unicode);

		hundContainer.addContainerFilter(new Equal("idhund", teilnehmerItem.getItemProperty("id_hund").getValue()));

		personContainer
				.addContainerFilter(new Equal("idperson", teilnehmerItem.getItemProperty("id_person").getValue()));

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
		fields.setField("ort-datum", veranstaltung.getItemProperty("veranstaltungsort").getValue().toString() + ", "
				+ dateFormat1.format(veranstaltung.getItemProperty("datum").getValue()));

		Date currentDate = new Date();

		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar wurfDateCal = new GregorianCalendar();
		wurfDateCal.setTime(dateFormat2.parse(
				hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("wurfdatum").getValue().toString()));

		GregorianCalendar currentDateCalender = new GregorianCalendar();

		int yearDiff = currentDateCalender.get(Calendar.YEAR) - wurfDateCal.get(Calendar.YEAR);
		int monthDiff = currentDateCalender.get(Calendar.MONTH) - wurfDateCal.get(Calendar.MONTH);
		Integer alter = Integer.valueOf(yearDiff * 12 + monthDiff);

		fields.setField("alter", alter.toString() + " Monate");

		fields.setField("chipnummer",
				hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("chipnummer").getValue().toString());

		fields.setField("geschlecht",
				hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("geschlecht").getValue().toString());

		fields.setField("zbnr",
				hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("zuchtbuchnummer").getValue().toString());

		
		if (!(hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("farbe").getValue() == null)) {
			fields.setField("farbe",
					hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("farbe").getValue().toString());
		}

		if (!(hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("rasse").getValue() == null)) {
			System.out.println(" beginn der rassen");
			fields.setField("rasse", Rassen.getRasseForKurzBezeichnung(
					hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("rasse").getValue().toString())
					.getRassenLangBezeichnung());

		}
		hundContainer.removeAllContainerFilters();
		personContainer.removeAllContainerFilters();

		stamper.close();
		reader.close();
		return baos.toByteArray();
	}

	// HF
	// Chip- oder Tätonr.:
	// Geschlecht
	// Ort
	// Hund
	// Datum
	// Wurftag

	// PdfReader reader = new PdfReader(DATASHEET);
	// // Get the fields from the reader (read-only!!!)
	// AcroFields form = reader.getAcroFields();
	// // Loop over the fields and get info about them
	// Set<String> fields = form.getFields().keySet();
	// for (String key : fields) {
	// System.out.println(key);
	// }

}
