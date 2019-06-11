package com.app.printclasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.app.auth.Hund;
import com.app.auth.Person;
import com.app.dbio.DBHund;
import com.app.dbio.DBPerson;
import com.app.dbio.DBVeranstaltung;
import com.app.enumdatatypes.VeranstaltungsStufen;
import com.app.service.TemporaryFileDownloadResource;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsStufe;
import com.app.veranstaltung.VeranstaltungsTeilnehmer;
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
import com.vaadin.v7.data.util.filter.Compare.Equal;

public class StarterListe extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -712524145795603983L;

	private AbsoluteLayout mainLayout;

	private static String RESULT = "Starterliste.pdf";

	private DBVeranstaltung dbVa;
	private DBHund dbHund;
	private DBPerson dbPerson;

	public StarterListe(Veranstaltung veranstaltung, boolean isInternListe) {

		dbVa = new DBVeranstaltung();
		dbHund = new DBHund();
		dbPerson = new DBPerson();

		try {

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			PdfDocument pdf = new PdfDocument(new PdfWriter(RESULT));

			// Initialize document
			Document document = new Document(pdf, PageSize.A4.rotate());

			PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");

			String titleString = "Starterliste " + veranstaltung.getTyp().getVeranstaltungsTypBezeichnung() + " "
					+ dateFormat1.format(veranstaltung.getDatum());

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

			List<VeranstaltungsTeilnehmer> teilnehmer = dbVa
					.getAlleTeilnehmerZuVeranstaltung(veranstaltung.getId_veranstaltung());

			for (VeranstaltungsTeilnehmer zw : teilnehmer) {
				Hund hund = dbHund.getHundForHundId(zw.getIdHund());
				Person person = dbPerson.getPersonForId(zw.getIdPerson());
				VeranstaltungsStufe stufe = dbVa.getStufeZuId(zw.getIdStufe());

				table.addCell(zw.getStartnr().toString());

				table.addCell(stufe.getStufenTyp().getBezeichnung());

				String hundeFuehrer = "";
				if (!(zw.getHundefuehrer() == null)) {

					hundeFuehrer = zw.getHundefuehrer();
				} else {
					hundeFuehrer = person.getLastName() + " " + person.getFirstName();
				}
				table.addCell(hundeFuehrer);

				table.addCell(hund.getZwingername());

				if (isInternListe) {

					Cell cell = new Cell().add(new Paragraph(""));
					cell.setNextRenderer(new BezahltRenderer(cell, zw.getBezahlt()));
					table.addCell(cell);
				}

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

		// @Override
		// public CellRenderer getNextRenderer() {
		// return new BezahltRenderer(getModelElement(), bezahlt);
		// }

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
