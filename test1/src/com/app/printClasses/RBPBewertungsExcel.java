package com.app.printClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;

import com.app.dbIO.DBConnection;
import com.app.enumPackage.VeranstaltungsStufen;
import com.app.service.TemporaryFileDownloadResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class RBPBewertungsExcel extends CustomComponent {

	private AbsoluteLayout mainLayout;
	private TableQuery q3;
	private TableQuery q4;
	private TableQuery q5;

	private SQLContainer personContainer;
	private SQLContainer hundContainer;
	private SQLContainer teilnehmerContainer;

	private static String RESULT = "RBPExcelBewerungsListe.xls";

	public RBPBewertungsExcel(Item veranstaltung, Item veranstaltungsStufe) {

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

			System.out.println("stufe: "
					+ veranstaltungsStufe.getItemProperty("id_stufe")
							.getValue().toString());
			teilnehmerContainer
					.addContainerFilter(new Equal("id_stufe",
							veranstaltungsStufe.getItemProperty("id_stufe")
									.getValue()));

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			File outputFile = new File(RESULT);

			WritableWorkbook workbook = Workbook.createWorkbook(outputFile);

			WritableSheet sheet = workbook.createSheet("Bewertungsliste", 0);

			int i = 0;
			for (Object zw : teilnehmerContainer.getItemIds()) {

				VeranstaltungsStufen ob = VeranstaltungsStufen
						.getBezeichnungForId(Integer
								.valueOf(veranstaltungsStufe
										.getItemProperty("stufen_typ")
										.getValue().toString()));
				Label label = new Label(0, i, ob.getBezeichnung());
				sheet.addCell(label);

				System.out.println(teilnehmerContainer.getItem(zw)
						.getItemProperty("id_hund").getValue().toString());

				hundContainer.addContainerFilter(new Equal("idhund",
						teilnehmerContainer.getItem(zw)
								.getItemProperty("id_hund").getValue()));

				personContainer.addContainerFilter(new Equal("idperson",
						teilnehmerContainer.getItem(zw)
								.getItemProperty("id_person").getValue()));

				Label hundeName = new Label(1, i, hundContainer
						.getItem(hundContainer.firstItemId())
						.getItemProperty("zwingername").getValue().toString());
				sheet.addCell(hundeName);

								
				DateFormat df = new DateFormat("dd.MM.yyyy");
				WritableCellFormat cf1 = new WritableCellFormat(df);
				 
				DateTime dt = new DateTime(2, i, new SimpleDateFormat("yyyy-MM-dd").parse(hundContainer
									.getItem(hundContainer.firstItemId())
									.getItemProperty("wurfdatum").getValue().toString()), cf1, DateTime.GMT);
				       
				sheet.addCell(dt);

				Label rasse = new Label(3, i, hundContainer
						.getItem(hundContainer.firstItemId())
						.getItemProperty("rasse").getValue().toString());
				sheet.addCell(rasse);
		
				NumberFormat chipNrFormat = new NumberFormat("000000000000000");
				WritableCellFormat chipCell = new WritableCellFormat(chipNrFormat);
				Number n = new Number(4,i,Float.valueOf(hundContainer.getItem(hundContainer.firstItemId())
						.getItemProperty("chipnummer").getValue().toString()), chipCell);
				sheet.addCell(n);

				String hundeFuehrer = "";
				if (teilnehmerContainer.getItem(zw)
						.getItemProperty("hundefuehrer").getValue() != null) {

					hundeFuehrer = teilnehmerContainer.getItem(zw)
							.getItemProperty("hundefuehrer").getValue()
							.toString();
				} else {
					hundeFuehrer = personContainer
							.getItem(personContainer.firstItemId())
							.getItemProperty("nachname").getValue().toString()
							+ " "
							+ personContainer
									.getItem(personContainer.firstItemId())
									.getItemProperty("vorname").getValue()
									.toString();
				}
				Label hundeFuehrerCell = new Label(5, i, hundeFuehrer);
				sheet.addCell(hundeFuehrerCell);

				NumberFormat punktFormat = new NumberFormat("##0");
				WritableCellFormat punktCell = new WritableCellFormat(punktFormat);
				
				Number punkte = null;
				if (teilnehmerContainer.getItem(zw)
						.getItemProperty("ges_punkte").getValue() != null) {
					punkte = new Number(6, i,
							Integer.valueOf(teilnehmerContainer.getItem(zw)
									.getItemProperty("ges_punkte").getValue()
									.toString()), punktCell);
				} else {
					punkte = new Number(6, i, 0, punktCell);
							
				}

				sheet.addCell(punkte);

				hundContainer.removeAllContainerFilters();
				personContainer.removeAllContainerFilters();
				i++;

			}

			workbook.write();
			workbook.close();
			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT,
						"application/vnd.ms-excel", outputFile);
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("BGHBewertungsblatt", s);
			mainLayout.addComponent(e);
		} catch (Exception ee) {
			ee.printStackTrace();
		}

	}

}
