package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.app.dbio.DBConnection;
import com.app.enumdatatypes.VeranstaltungsStufen;
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

public class UrkundeTrainingsWorkingtest extends CustomComponent {

	private FileOutputStream fos;
	/** The original PDF file. */
	public static final String DATASHEET = "files/URKUNDE_NOESTACH_4.pdf";
	public static final String FONT = "files/arialuni.ttf";

	public static final String RESULT = "Urkunde.pdf";

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	private Integer rang = 0;
	private Integer altePunkte = 0;

	public UrkundeTrainingsWorkingtest(Item veranstaltung, Item veranstaltungsStufe) {

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

			teilnehmerContainer.addOrderBy(new OrderBy("platzierung", false));

			teilnehmerContainer.addContainerFilter(
					new Equal("id_stufe", veranstaltungsStufe.getItemProperty("id_stufe").getValue()));

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			bauPdf(veranstaltung, veranstaltungsStufe);
			TemporaryFileDownloadResource s = null;
			s = new TemporaryFileDownloadResource(RESULT, "application/pdf", new File(RESULT));

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

	private void bauPdf(Item veranstaltung, Item veranstaltungsStufe) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(RESULT));
		pdfDoc.initializeOutlines();

		ByteArrayOutputStream baos;
		PdfDocument pdfInnerDoc;
		Map<String, PdfFormField> fields;
		PdfAcroForm form;
		for (Object id : teilnehmerContainer.getItemIds()) {
			baos = new ByteArrayOutputStream();
			pdfInnerDoc = new PdfDocument(new PdfReader(DATASHEET), new PdfWriter(baos));
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();

			Item teilnehmerItem = teilnehmerContainer.getItem(id);
			

			hundContainer.addContainerFilter(new Equal("idhund", teilnehmerItem
					.getItemProperty("id_hund").getValue()));

			personContainer.addContainerFilter(new Equal("idperson", teilnehmerItem
					.getItemProperty("id_person").getValue()));


			if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null) {

				fields.get("hundefuehrer")
						.setValue(teilnehmerItem.getItemProperty("hundefuehrer").getValue().toString());
			} else {
				fields.get("hundefuehrer")
						.setValue(personContainer.getItem(personContainer.firstItemId()).getItemProperty("nachname")
								.getValue().toString() + " "
								+ personContainer.getItem(personContainer.firstItemId()).getItemProperty("vorname")
										.getValue().toString()

				);
			}

			fields.get("hund").setValue(hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("zwingername").getValue().toString());

			VeranstaltungsStufen defStufe = VeranstaltungsStufen.getBezeichnungForId(
					new Integer(veranstaltungsStufe.getItemProperty("stufen_typ").getValue().toString()));

			fields.get("klasse").setValue(defStufe.getLangBezeichnung());

			if (!(teilnehmerItem.getItemProperty("ges_punkte").getValue() == null)) {

				if (teilnehmerItem.getItemProperty("ges_punkte").getValue().toString().equals("0")) {

				} else {
					String text = teilnehmerItem.getItemProperty("ges_punkte").getValue().toString() + " Punkte ";
					PdfFormField punkteField = fields.get("punkte");
				    //punkteField.setJustification(PdfFormField.ALIGN_CENTER);
					punkteField.setValue(text);
				
					
					text = teilnehmerItem.getItemProperty("platzierung").getValue().toString() + ". Platz";

					if (!(teilnehmerItem.getItemProperty("sonderwertung").getValue() == null)) {
						text = text + " mit Judges Choice";
					}
					
					PdfFormField rangField = fields.get("rang");
				    //punkteField.setJustification(PdfFormField.ALIGN_CENTER);
					rangField.setValue(text);
				
				    
				}
				
			}
			// fields.setField("ZEIL_finalE 3", "bestanden");

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
