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
import com.app.service.TemporaryFileDownloadResource;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsStufe;
import com.app.veranstaltung.VeranstaltungsTeilnehmer;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;



//LINK AUFHEBN: https://kb.itextpdf.com/home
//LINK AUFHEBN: https://kb.itextpdf.com/home
//LINK AUFHEBN: https://kb.itextpdf.com/home
//LINK AUFHEBN: https://kb.itextpdf.com/home




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

			PageXofY pageXofY = new PageXofY(pdf);
			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, pageXofY);

			PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy");

			String titleString = "Starterliste " + veranstaltung.getTyp().getVeranstaltungsTypBezeichnung() + " "
					+ dateFormat1.format(veranstaltung.getDatum());

			Text title = new Text(titleString).setFont(bold);
			Paragraph p = new Paragraph().add(title).setFontSize(18);
			document.add(p).setHorizontalAlignment(HorizontalAlignment.CENTER);

			p = new Paragraph().add("").setFontSize(18);
			document.add(p);

			List<VeranstaltungsTeilnehmer> teilnehmer = dbVa
					.getAlleTeilnehmerZuVeranstaltung(veranstaltung.getId_veranstaltung());

			List<VeranstaltungsStufe> stufen = dbVa.getStufenZuVaId(veranstaltung.getId_veranstaltung());

			for (VeranstaltungsStufe zwStufe : stufen) {

				List<VeranstaltungsTeilnehmer> stufenTeilnehmer = dbVa.getAlleTeilnehmerZuStufe(zwStufe.getIdStufe());

				Text stufenTitel = new Text(zwStufe.getStufenTyp().getBezeichnung()).setFont(bold);
				Paragraph ps = new Paragraph().add(stufenTitel).setFontSize(14);
				ps.setKeepWithNext(true);
				document.add(ps).setHorizontalAlignment(HorizontalAlignment.CENTER);

				Text anzahl = new Text("Teilnehmer: " + stufenTeilnehmer.size());
				Paragraph pAnz = new Paragraph().add(anzahl).setFontSize(10);
				pAnz.setKeepWithNext(true);
				document.add(pAnz).setHorizontalAlignment(HorizontalAlignment.CENTER);

				Table table;
				if (isInternListe) {
					table = new Table(UnitValue.createPercentArray(new float[] { 3, 14, 11, 2 }))
							.useAllAvailableWidth();
				} else {
					table = new Table(UnitValue.createPercentArray(new float[] { 3, 14, 11 })).useAllAvailableWidth();
				}

				table.addHeaderCell("Startnr").addHeaderCell("Hundeführer").addHeaderCell("Hund");

				if (isInternListe) {
					table.addHeaderCell("bez");
				}

				table.getHeader().setBold();

				for (VeranstaltungsTeilnehmer zw : stufenTeilnehmer) {
					Hund hund = dbHund.getHundForHundId(zw.getIdHund());
					Person person = dbPerson.getPersonForId(zw.getIdPerson());

					table.addCell(zw.getStartnr().toString());

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

				table.setKeepWithNext(true);

				if (stufenTeilnehmer.size() > 0)
					document.add(table);

				Paragraph pleer = new Paragraph().add("\n").setFontSize(14);
				document.add(pleer);
			}

			Text anzTeilnehmer = new Text("Gesamtteilnehmer: " + teilnehmer.size());
			Paragraph pTeil = new Paragraph().add(anzTeilnehmer).setFontSize(10);
			document.add(pTeil).setHorizontalAlignment(HorizontalAlignment.CENTER);

			pageXofY.writeTotal(pdf);
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

	protected class PageXofY implements IEventHandler {

		protected PdfFormXObject placeholder;
		protected float side = 20;
		protected float x = 600;
		protected float y = 25;
		protected float space = 4.5f;
		protected float descent = 3;

		public PageXofY(PdfDocument pdf) {
			placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
		}

		@Override
		public void handleEvent(com.itextpdf.kernel.events.Event event) {
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfDocument pdf = docEvent.getDocument();
			PdfPage page = docEvent.getPage();
			int pageNumber = pdf.getPageNumber(page);
			Rectangle pageSize = page.getPageSize();
			PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdf);
			Canvas canvas = new Canvas(page, pageSize);
			Paragraph p = new Paragraph().add("Seite ").add(String.valueOf(pageNumber)).add(" von");
			canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
			pdfCanvas.addXObject(placeholder, x + space, y - descent);
			pdfCanvas.release();
			canvas.close();
		}

		public void writeTotal(PdfDocument pdf) {
			Canvas canvas = new Canvas(placeholder, pdf);
			canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()), 0, descent, TextAlignment.LEFT);
			canvas.close();
		}

	}

}
