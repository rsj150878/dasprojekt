package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.dbio.DBConnection;
import com.app.service.TemporaryFileDownloadResource;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class BGH1RichterBlatt extends CustomComponent {

	private PdfReader reader;
	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/BGH1-NEU_FORMULAR.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "RichterBlatt.pdf";

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	public BGH1RichterBlatt(Item veranstaltung, Item veranstaltungsStufe) {

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

			teilnehmerContainer.addOrderBy(new OrderBy("startnr", true));

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			bauPdf(veranstaltung, veranstaltungsStufe);

			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT, "application/pdf", new File(RESULT));
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("BGHBewertungsblatt", s);
			mainLayout.addComponent(e);

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private void bauPdf(Item veranstaltung, Item veranstaltungsStufe) throws Exception {

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(RESULT));
		pdfDoc.initializeOutlines();

		ByteArrayOutputStream baos;
		PdfDocument pdfInnerDoc;
		Map<String, PdfFormField> fields;
		PdfAcroForm form;
		for (Object id : teilnehmerContainer.getItemIds()) {

			Item teilnehmerItem = teilnehmerContainer.getItem(id);
			// create a PDF in memory
			baos = new ByteArrayOutputStream();
			pdfInnerDoc = new PdfDocument(new PdfReader(DATASHEET), new PdfWriter(baos));
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();
			hundContainer.addContainerFilter(new Equal("idhund", teilnehmerItem.getItemProperty("id_hund").getValue()));

			personContainer
					.addContainerFilter(new Equal("idperson", teilnehmerItem.getItemProperty("id_person").getValue()));

			fields.get("Prüfungsleiter")
					.setValue(veranstaltung.getItemProperty("veranstaltungsleiter").getValue().toString());

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
			fields.get("Prüfung am").setValue(dateFormat1.format(veranstaltung.getItemProperty("datum").getValue()));
			fields.get("Veranstaltung am")
					.setValue(dateFormat1.format(veranstaltung.getItemProperty("datum").getValue()));

			fields.get("Rasse").setValue(
					hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("rasse").getValue().toString());

			if (!(hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("zuchtbuchnummer")
					.getValue() == null)) {
				fields.get("Zuchtbuch / Reg.-Nr").setValue(hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("zuchtbuchnummer").getValue().toString());
			}
			fields.get("Name des Hundes").setValue(hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("zwingername").getValue().toString());

			fields.get("Name des Hundes_2").setValue(hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("zwingername").getValue().toString());

			fields.get("Wurftag").setValue(dateFormat1.format(
					hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("wurfdatum").getValue()));

			fields.get("Mikrochip").setValue(hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("chipnummer").getValue().toString());
			fields.get("Mikrochip_2").setValue(hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("chipnummer").getValue().toString());

			fields.get("Kat.Nr").setValue(teilnehmerItem.getItemProperty("startnr").getValue().toString());
			fields.get("Kat.Nr_2").setValue(teilnehmerItem.getItemProperty("startnr").getValue().toString());

			if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null) {

				fields.get("Name des Hundeführers")
						.setValue(teilnehmerItem.getItemProperty("hundefuehrer").getValue().toString());
				fields.get("Name des Hundeführers_2")
						.setValue(teilnehmerItem.getItemProperty("hundefuehrer").getValue().toString());
			} else {
				fields.get("Name des Hundeführers")
						.setValue(personContainer.getItem(personContainer.firstItemId()).getItemProperty("nachname")
								.getValue().toString() + " "
								+ personContainer.getItem(personContainer.firstItemId()).getItemProperty("vorname")
										.getValue().toString()

				);
				fields.get("Name des Hundeführers_2")
						.setValue(personContainer.getItem(personContainer.firstItemId()).getItemProperty("nachname")
								.getValue().toString() + " "
								+ personContainer.getItem(personContainer.firstItemId()).getItemProperty("vorname")
										.getValue().toString()

				);
			}

			fields.get("Geschlecht").setValue(hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("geschlecht").getValue().toString());

			fields.get("Veranstalter").setValue(veranstaltung.getItemProperty("veranstalter").getValue().toString());
			fields.get("Leistungsrichter  B").setValue(veranstaltung.getItemProperty("richter").getValue().toString());

			fields.get("Prüfung in").setValue(veranstaltung.getItemProperty("veranstaltungsort").getValue().toString());
			fields.get("Veranstaltung in_2")
					.setValue(veranstaltung.getItemProperty("veranstaltungsort").getValue().toString());

			hundContainer.removeAllContainerFilters();
			personContainer.removeAllContainerFilters();

			hundContainer.removeAllContainerFilters();
			personContainer.removeAllContainerFilters();

			form.flattenFields();
			pdfInnerDoc.close();

			pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
			pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
			pdfInnerDoc.close();
		}

		pdfDoc.close();
	}

}
