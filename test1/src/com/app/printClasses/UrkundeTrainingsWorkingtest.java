package com.app.printClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.dbIO.DBConnection;
import com.app.enumPackage.Rassen;
import com.app.enumPackage.VeranstaltungsStufen;
import com.app.service.TemporaryFileDownloadResource;
import com.itextpdf.text.Rectangle;
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

public class UrkundeTrainingsWorkingtest extends CustomComponent {
	private PdfReader reader;

	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/URKUNDE_NOESTACH_final_F.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Urkunde.pdf";

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	public UrkundeTrainingsWorkingtest(Item veranstaltung,
			Item veranstaltungsStufe) {
		try {
			reader = new PdfReader(DATASHEET);
			// Get the fields from the reader (read-only!!!)
			AcroFields form = reader.getAcroFields();
			// Loop over the fields and get info about them
			Set<String> fields = form.getFields().keySet();
			for (String key : fields) {
				System.out.println("#" + key + "#");
				System.out.println(form.getFieldType(key));
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

			// e.addDetachListener(mainLayout.getp);

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	// Name Hundeführer#
	// Klasse#
	// Rang/Punkte#
	// Name Hund#
	//

	private byte[] bauPdf(Item veranstaltung, Item veranstaltungsStufe,
			Item teilnehmerItem) throws Exception {
		PdfReader reader = new PdfReader(DATASHEET);
		// fos = new FileOutputStream(RESULT);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfStamper stamper = new PdfStamper(reader, baos);
		stamper.setFormFlattening(true);
		AcroFields fields = stamper.getAcroFields();

		BaseFont unicode = BaseFont.createFont(FONT, BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED);
		fields.addSubstitutionFont(unicode);

		hundContainer.addContainerFilter(new Equal("idhund", teilnehmerItem
				.getItemProperty("id_hund").getValue()));

		personContainer.addContainerFilter(new Equal("idperson", teilnehmerItem
				.getItemProperty("id_person").getValue()));

		if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null) {

			fields.setField("Name Hundeführer",
					teilnehmerItem.getItemProperty("hundefuehrer").getValue()
							.toString());
		} else {
			fields.setField(
					"Name Hundeführer",
					personContainer.getItem(personContainer.firstItemId())
							.getItemProperty("nachname").getValue().toString()
							+ " "
							+ personContainer
									.getItem(personContainer.firstItemId())
									.getItemProperty("vorname").getValue()
									.toString()

			);
		}

		fields.setField("Name Hund",
				hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("zwingername").getValue().toString());

		VeranstaltungsStufen defStufe = VeranstaltungsStufen
				.getBezeichnungForId(new Integer(veranstaltungsStufe
						.getItemProperty("stufen_typ").getValue().toString()));

		fields.setField("Klasse", defStufe.getLangBezeichnung());

		if (!(teilnehmerItem.getItemProperty("ges_punkte").getValue() == null)) {

			if (teilnehmerItem.getItemProperty("ges_punkte").getValue().toString()
					.equals("0")) {

			} else {
				String text = teilnehmerItem.getItemProperty("ges_punkte")
						.getValue().toString()
						+ " Punkte";

				if (!(teilnehmerItem.getItemProperty("sonderwertung")
						.getValue() == null)) {
					text = text + " mit Judges Choice";
				}
				fields.setField("Rang/Punkte", text);
			}
		}
		// fields.setField("ZEILE 3", "bestanden");

		hundContainer.removeAllContainerFilters();
		personContainer.removeAllContainerFilters();

		stamper.close();
		reader.close();
		return baos.toByteArray();

	}
	// HUNDEFÜHRERIN
	// CHIP-NR
	// GEWORFEN AM
	// ÖHZB-NR
	// RÜDE
	// RASSE
	// Ort Datum
	// NAME DES HUNDES
	// HÜNDIN
	// ZEILE 3
	// PRÜFUNGSZEILE
	// ZEILE 1
	// ZEILE 2

}
