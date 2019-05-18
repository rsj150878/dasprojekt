
package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.dbio.DBHund;
import com.app.dbio.DBPerson;
import com.app.dbio.DBVeranstaltung;
import com.app.service.TemporaryFileDownloadResource;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsStufe;
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

public class RBP3RichterBlatt extends CustomComponent {

		/** The original PDF file. */
	public static final String DATASHEET = "files/RBP_Richterblatt_RBP3_FORMULAR.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "RichterBlatt.pdf";

	private AbsoluteLayout mainLayout;
	
	private DBVeranstaltung dbVa;
	private DBHund dbHund;
	private DBPerson dbPerson;
	
	public RBP3RichterBlatt(Veranstaltung veranstaltung, VeranstaltungsStufe veranstaltungsStufe) {

		dbVa = new DBVeranstaltung();
		dbHund = new DBHund();
		dbPerson = new DBPerson();
	
		try {

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

			BrowserFrame e = new BrowserFrame("RBP1Bewertungsblatt", s);
			mainLayout.addComponent(e);

		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	private void bauPdf(Veranstaltung veranstaltung, VeranstaltungsStufe veranstaltungsStufe) throws Exception {

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

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");
			fields.get("Datum").setValue( dateFormat1.format(veranstaltung.getItemProperty("datum").getValue()));

			fields.get("Hund").setValue( hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("zwingername")
					.getValue().toString());

			fields.get("Wurftag").setValue( dateFormat1.format(
					hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("wurfdatum").getValue()));

			fields.get("Chip- oder Tätonr.:").setValue( hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("chipnummer").getValue().toString());

			if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null) {

				fields.get("HF").setValue( teilnehmerItem.getItemProperty("hundefuehrer").getValue().toString());
			} else {
				fields.get("HF").setValue(
						personContainer.getItem(personContainer.firstItemId()).getItemProperty("nachname").getValue()
								.toString() + " "
								+ personContainer.getItem(personContainer.firstItemId()).getItemProperty("vorname")
										.getValue().toString()

				);
			}

			fields.get("Geschlecht").setValue( hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("geschlecht").getValue().toString());

			fields.get("Ort").setValue( veranstaltung.getItemProperty("veranstaltungsort").getValue().toString());

			fields.get("startnr").setValue(teilnehmerItem.getItemProperty("startnr").getValue().toString());

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
