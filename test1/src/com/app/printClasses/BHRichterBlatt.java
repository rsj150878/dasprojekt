package com.app.printClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.dbIO.DBConnection;
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

public class BHRichterBlatt extends CustomComponent {

	private PdfReader reader;
	private PdfStamper stamper;
	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/BH_Formular.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "RichterBlatt.pdf";

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	public BHRichterBlatt(Item veranstaltung, Item veranstaltungsStufe) {

		

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

		fields.setField("Prüfungsleiter",
				veranstaltung.getItemProperty("veranstaltungsleiter")
						.getValue().toString());
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
		fields.setField("Prüfung am", dateFormat1.format(veranstaltung
				.getItemProperty("datum").getValue()));

		if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null) {

			fields.setField("Name des Führers",
					teilnehmerItem.getItemProperty("hundefuehrer").getValue()
							.toString());
		} else {
			fields.setField(
					"Name des Führers",
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("nachname").getValue().toString()
							+ " "
							+ personContainer
									.getItem(personContainer.firstItemId())
									.getItemProperty("vorname").getValue()
									.toString()

			);
		}

		fields.setField("Rasse",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("rasse").getValue().toString());
		fields.setField("Verbandsverein", "Österreichischer Retrieverclub");
		fields.setField("Zuchtbuchnummer/RegNr",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("zuchtbuchnummer").getValue()
						.toString());
		fields.setField("Name des Hundes",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("zwingername").getValue().toString());

		fields.setField(
				"Wurftag",
				dateFormat1.format(hundContainer
						.getItem(hundContainer.firstItemId())
						.getItemProperty("wurfdatum").getValue()));

		fields.setField("Tätonummer/Chip Nummer",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("chipnummer").getValue().toString());

		if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null) {

			fields.setField("Name des Hundeführers", teilnehmerItem
					.getItemProperty("hundefuehrer").getValue().toString());
		} else {
			fields.setField(
					"Name des Hundeführers",
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("nachname").getValue().toString()
							+ " "
							+ personContainer
									.getItem(personContainer.firstItemId())
									.getItemProperty("vorname").getValue()
									.toString()

			);
		}

		fields.setField("Geschlecht",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("geschlecht").getValue().toString());

		fields.setField("Ortsgruppe", "Retriever in Ebreichsdorf");
		fields.setField("Veranstalter",
				veranstaltung.getItemProperty("veranstalter").getValue()
						.toString());
		fields.setField("Leistungsrichter",
				veranstaltung.getItemProperty("richter").getValue().toString());
		fields.setField("Prüfung in",
				veranstaltung.getItemProperty("veranstaltungsort").getValue()
						.toString());

		hundContainer.removeAllContainerFilters();
		personContainer.removeAllContainerFilters();

		stamper.close();
		reader.close();
		return baos.toByteArray();
	}
	// Prüfungsleiter
	// Prüfung am
	// Name des Führers
	// Rasse
	// Verbandsverein
	// Zuchtbuchnummer/RegNr
	// Name des Hundes
	// Wurftag
	// Tätonummer/Chip Nummer
	// Name des Hundeführers
	// Geschlecht
	// Ortsgruppe
	// Veranstalter
	// Leistungsrichter
	// Prüfung in

	// PdfReader reader = new PdfReader(DATASHEET);
	// // Get the fields from the reader (read-only!!!)
	// AcroFields form = reader.getAcroFields();
	// // Loop over the fields and get info about them
	// Set<String> fields = form.getFields().keySet();
	// for (String key : fields) {
	// System.out.println(key);
	// }

}
