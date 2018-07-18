package com.app.printclasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import com.app.dbio.DBConnection;
import com.app.enumdatatypes.VeranstaltungsStufen;
import com.app.enumdatatypes.VeranstaltungsTypen;
import com.app.service.TemporaryFileDownloadResource;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

public class StarterListe extends CustomComponent {

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;
	private TableQuery q1;

	private SQLContainer veranstaltungsStufenContainer;
	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	private static String RESULT = "Starterliste.pdf";

	public StarterListe(Item veranstaltung, boolean isInternListe) {

		q3 = new TableQuery("veranstaltungs_teilnehmer", DBConnection.INSTANCE.getConnectionPool());
		q3.setVersionColumn("version");

		q4 = new TableQuery("person", DBConnection.INSTANCE.getConnectionPool());
		q4.setVersionColumn("version");

		q5 = new TableQuery("hund", DBConnection.INSTANCE.getConnectionPool());
		q5.setVersionColumn("version");

		q1 = new TableQuery("veranstaltungs_stufe", DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		try {

			personContainer = new SQLContainer(q4);
			hundContainer = new SQLContainer(q5);
			teilnehmerContainer = new SQLContainer(q3);
			veranstaltungsStufenContainer = new SQLContainer(q1);

			teilnehmerContainer.addContainerFilter(
					new Equal("id_veranstaltung", veranstaltung.getItemProperty("id_veranstaltung").getValue()));

			teilnehmerContainer.addOrderBy(new OrderBy("startnr", true));

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			PdfDocument pdf = new PdfDocument(new PdfWriter(RESULT));

			// Initialize document
			Document document = new Document(pdf, PageSize.A4.rotate());

			PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");

			String titleString = "Starterliste "
					+ VeranstaltungsTypen
							.getVeranstaltungsTypForId(
									Integer.valueOf(veranstaltung.getItemProperty("typ").getValue().toString()))
							.getVeranstaltungsTypBezeichnung()
					+ " " + dateFormat1.format(veranstaltung.getItemProperty("datum").getValue());

			Text title = new Text(titleString).setFont(bold);
			Paragraph p = new Paragraph().add(title).setFontSize(18);
			document.add(p).setHorizontalAlignment(HorizontalAlignment.CENTER);
			
			p = new Paragraph().add("").setFontSize(18);
			document.add(p);
			
			Table table;
			if (isInternListe) {
				table = new Table(UnitValue.createPercentArray(new float[] { 3, 2, 14, 9, 2 })).useAllAvailableWidth();
			} else {
				table = new Table(UnitValue.createPercentArray(new float[] { 3, 2, 14, 9 })).useAllAvailableWidth();
			}

			table.addHeaderCell("Startnr").addHeaderCell("Stufe").addHeaderCell("Hundeführer").addHeaderCell("Hund");

			if (isInternListe) {
				table.addHeaderCell("bez");
			}

			table.getHeader().setBold();

			for (Object zw : teilnehmerContainer.getItemIds()) {

				table.addCell(teilnehmerContainer.getItem(zw).getItemProperty("startnr").getValue().toString());

				veranstaltungsStufenContainer.addContainerFilter(
						new Equal("id_stufe", teilnehmerContainer.getItem(zw).getItemProperty("id_stufe").getValue()));

				VeranstaltungsStufen ob = VeranstaltungsStufen.getBezeichnungForId(Integer
						.valueOf(veranstaltungsStufenContainer.getItem(veranstaltungsStufenContainer.getIdByIndex(0))
								.getItemProperty("stufen_typ").getValue().toString()));
				veranstaltungsStufenContainer.removeAllContainerFilters();
				table.addCell(ob.getBezeichnung());

				System.out.println(teilnehmerContainer.getItem(zw).getItemProperty("id_hund").getValue().toString());

				hundContainer.addContainerFilter(
						new Equal("idhund", teilnehmerContainer.getItem(zw).getItemProperty("id_hund").getValue()));

				personContainer.addContainerFilter(
						new Equal("idperson", teilnehmerContainer.getItem(zw).getItemProperty("id_person").getValue()));

				String hundeFuehrer = "";
				if (!(teilnehmerContainer.getItem(zw).getItemProperty("hundefuehrer").getValue() == null)) {

					hundeFuehrer = teilnehmerContainer.getItem(zw).getItemProperty("hundefuehrer").getValue()
							.toString();
				} else {
					hundeFuehrer = personContainer.getItem(personContainer.firstItemId()).getItemProperty("nachname")
							.getValue().toString() + " "
							+ personContainer.getItem(personContainer.firstItemId()).getItemProperty("vorname")
									.getValue().toString();
				}
				table.addCell(hundeFuehrer);

				table.addCell(hundContainer.getItem(hundContainer.firstItemId()).getItemProperty("zwingername")
						.getValue().toString());

				if (isInternListe) {

					Cell cell = new Cell().add(new Paragraph(""));
					cell.setNextRenderer(new BezahltRenderer(cell,
							teilnehmerContainer.getItem(zw).getItemProperty("bezahlt").getValue().toString()));
					table.addCell(cell);
				}
				hundContainer.removeAllContainerFilters();
				personContainer.removeAllContainerFilters();

			}

			document.add(table);
			document.close();

			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT, "application/pdf", new File(RESULT));
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("GAPBewertungsblatt", s);
			mainLayout.addComponent(e);
		} catch (Exception ee) {
			ee.printStackTrace();
		}

	}

	private class BezahltRenderer extends CellRenderer {
		private String bezahlt;

		public BezahltRenderer(Cell modelElement, String bezahlt) {
			super(modelElement);
			this.bezahlt = bezahlt;
		}

//		@Override
//		public CellRenderer getNextRenderer() {
//			return new BezahltRenderer(getModelElement(), bezahlt);
//		}

		@Override
		public void drawBackground(DrawContext drawContext) {
			PdfCanvas canvas = drawContext.getCanvas();
			canvas.saveState();
			if (bezahlt.equals("J")) {
				canvas.setFillColor(ColorConstants.GREEN);
			} else {
				canvas.setFillColor(ColorConstants.RED);
			}

			Rectangle rect = getOccupiedAreaBBox();
			canvas.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
			canvas.fill();
			canvas.restoreState();
			super.drawBackground(drawContext);
		}
	}

}
