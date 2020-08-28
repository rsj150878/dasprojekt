package com.app.printclasses;

import java.io.File;
import java.io.FileNotFoundException;
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
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.CustomComponent;

import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class BewertungsListeNeu extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8416619752436456154L;

	private AbsoluteLayout mainLayout;

	private DBVeranstaltung dbVa;
	private DBPerson dbPerson;
	private DBHund dbHund;

	private static String RESULT = "BewertungsListeOERC.xls";

	public BewertungsListeNeu(Veranstaltung veranstaltung) {

	
		try {

			dbVa = new DBVeranstaltung();
			dbPerson = new DBPerson();
			dbHund = new DBHund();

			List<VeranstaltungsStufe> stufen = dbVa.getStufenZuVaId(veranstaltung.getId_veranstaltung());

			mainLayout = new AbsoluteLayout();
			mainLayout.setWidth("100%");
			mainLayout.setHeight("100%");
			setCompositionRoot(mainLayout);

			File outputFile = new File(RESULT);

			WritableWorkbook workbook = Workbook.createWorkbook(outputFile);

			WritableSheet sheet = workbook.createSheet("Bewertungsliste", 0);

			int i = 0;
			for (VeranstaltungsStufe zw : stufen) {

				List<VeranstaltungsTeilnehmer> teilnehmer = dbVa.getAlleTeilnehmerZuStufe(zw.getIdStufe());

				for (VeranstaltungsTeilnehmer zwi : teilnehmer) {
					System.out.println(zw);

					Person person = dbPerson.getPersonForId(zwi.getIdPerson());
					Hund hund = dbHund.getHundForHundId(zwi.getIdHund());

					Label label = new Label(0, i, zw.getStufenTyp().getBezeichnung());
					sheet.addCell(label);

					Label hundeName = new Label(1, i, hund.getZwingername());
					sheet.addCell(hundeName);

					DateFormat df = new DateFormat("dd.MM.yyyy");
					WritableCellFormat cf1 = new WritableCellFormat(df);

					DateTime dt = new DateTime(2, i, hund.getWurfdatum(), cf1);

					sheet.addCell(dt);

					Label rasse = new Label(3, i, hund.getRasse());
					sheet.addCell(rasse);

					Label geschlecht = new Label(4, i, hund.getGeschlecht());

					sheet.addCell(geschlecht);

					NumberFormat chipNrFormat = new NumberFormat("000 000 000 000 000");
					WritableCellFormat chipCell = new WritableCellFormat(chipNrFormat);

					Number n = new Number(5, i, Double.valueOf(hund.getChipnummer()), chipCell);
					sheet.addCell(n);

					String hundeFuehrer = "";
					if (!(zwi.getHundefuehrer() == null) && !(zwi.getHundefuehrer().isEmpty())) {

						hundeFuehrer = zwi.getHundefuehrer();
					} else {
						hundeFuehrer = person.getLastName() + " " + person.getFirstName();
					}
					Label hundeFuehrerCell = new Label(6, i, hundeFuehrer);
					sheet.addCell(hundeFuehrerCell);

					NumberFormat punktFormat = new NumberFormat("##0");
					WritableCellFormat punktCell = new WritableCellFormat(punktFormat);

					Label unbefangenheitsprobe = new Label(7, i, (zwi.getBestanden() != null && zwi.getBestanden().equals("J")) ? "B" : "NB");
					sheet.addCell(unbefangenheitsprobe);

					if (zw.getStufenTyp().equals(VeranstaltungsStufen.STUFE_BH)
							|| zw.getStufenTyp().equals(VeranstaltungsStufen.STUFE_BH_VT_ONLY)) {

						String bestandenString = "";
						if (zwi.getBestanden() != null && zwi.getBestanden()
								.equals(("J"))) {
							bestandenString = "best.";

						} else {
							bestandenString = "n.best.";
						}

						Label bestanden = new Label(8, i, bestandenString);
						sheet.addCell(bestanden);

					} else {
						Number punkte = null;
						if (zwi.getGesPunkte() != null) {
							punkte = new Number(8, i, zwi.getGesPunkte(), punktCell);
						} else {
							punkte = new Number(8, i, 0, punktCell);

						}
						sheet.addCell(punkte);

					}

					Label email = new Label(9, i, person.getEmail());

					sheet.addCell(email);

					i++;
				}
			}

			workbook.write();
			workbook.close();
			TemporaryFileDownloadResource s = null;
			try {
				s = new TemporaryFileDownloadResource(RESULT, "application/vnd.ms-excel", outputFile);
			} catch (final FileNotFoundException e) {

			}

			BrowserFrame e = new BrowserFrame("GAPBewertungsblatt", s);
			mainLayout.addComponent(e);
		} catch (Exception ee) {
			ee.printStackTrace();
		}

	}

}
