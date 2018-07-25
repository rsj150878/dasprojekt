package com.app.printclasses;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
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

public class JungHundePruefung2017 extends CustomComponent {

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;
	private TableQuery q1;

	private SQLContainer veranstaltungsStufenContainer;
	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	private static String RESULT = "Urkunde.pdf";
	
	public static final String DATASHEET_KURZ = "files/URKUNDE JUHU_PFG_2018-Alfred.pdf";
	public static final String DATASHEET_LANG = "files/URKUNDE JUHU_PFG_2017-Alfred+Birgit.pdf";



	public JungHundePruefung2017(Item veranstaltung, boolean kurzeUrkunde) {


		
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
					new Equal("id_veranstaltung", veranstaltung.getItemProperty("id_veranstaltung").getValue()));
			teilnehmerContainer.addOrderBy(new OrderBy("startnr", true));


			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

						bauPdf(veranstaltung,kurzeUrkunde);
		
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
	
	private void bauPdf(Item veranstaltung, boolean kurzeUrkunde) throws Exception {

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(RESULT));
		pdfDoc.initializeOutlines();
		
		String vorlage =  kurzeUrkunde ? DATASHEET_KURZ:DATASHEET_LANG;

		ByteArrayOutputStream baos;
		PdfDocument pdfInnerDoc;
		Map<String, PdfFormField> fields;
		PdfAcroForm form;
		for (Object id : teilnehmerContainer.getItemIds()) {

			Item teilnehmerItem = teilnehmerContainer.getItem(id);
			// create a PDF in memory
			baos = new ByteArrayOutputStream();
			pdfInnerDoc = new PdfDocument(new PdfReader(vorlage), new PdfWriter(baos));
			form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
			fields = form.getFormFields();

			hundContainer.addContainerFilter(new Equal("idhund", teilnehmerItem.getItemProperty("id_hund").getValue()));

			personContainer
					.addContainerFilter(new Equal("idperson", teilnehmerItem.getItemProperty("id_person").getValue()));

			fields.get("hund").setValue(hundContainer.getItem(hundContainer.firstItemId())
					.getItemProperty("zwingername").getValue().toString());

			if (teilnehmerItem.getItemProperty("hundefuehrer").getValue() != null) {

				fields.get("hundeFuehrer")
						.setValue(teilnehmerItem.getItemProperty("hundefuehrer").getValue().toString());
			} else {
				fields.get("hundeFuehrer")
						.setValue(personContainer.getItem(personContainer.firstItemId()).getItemProperty("nachname")
								.getValue().toString() + " "
								+ personContainer.getItem(personContainer.firstItemId()).getItemProperty("vorname")
										.getValue().toString()

				);
			}

		

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
