package com.app.Components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.app.DashBoard.Component.VeranstaltungsTeilnehmerGrid;
import com.app.DashBoard.View.VeranstaltungsDetailViewNeu.AnmeldungsPanel;
import com.app.dbIO.DBConnection;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.RowId;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.v7.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.EmptyCell;

public class WesensTestImporter
		implements Receiver, SucceededListener, FailedListener, QueryDelegate.RowIdChangeListener {
	public File file;
	public String filename;
	private RowId personId;
	private Component meldeComponent;

	public void setComponentForMeldung(Component meldeComponent) {
		this.meldeComponent = meldeComponent;
	}

	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		FileOutputStream fos = null; // Stream to write to

		try {
			// Open the file for writing.
			file = new File(filename);
			fos = new FileOutputStream(file);
		} catch (final java.io.FileNotFoundException e) {
			new Notification("Could not open file<br/>", e.getMessage(), Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());
			return null;
		}
		return fos; // Return the output stream to write to
	}

	public void uploadSucceeded(SucceededEvent event) {
		// Show the uploaded file in the image viewer
		TableQuery q1 = new TableQuery("person", DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		TableQuery q2 = new TableQuery("hund", DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");

		try {
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet("Steuerdatei");
			SQLContainer hundContainer = new SQLContainer(q2);
			hundContainer.setAutoCommit(false);
			hundContainer.addRowIdChangeListener(this);

			SQLContainer personContainer = new SQLContainer(q1);
			personContainer.setAutoCommit(false);
			personContainer.addRowIdChangeListener(this);

			for (int i = 1; i < sheet.getRows(); i++) {

				System.out.println("verarbeite zeile: " + i);
				NumberCell personCell = (NumberCell) sheet.getCell(28, i);

				Integer personNr = new Integer(personCell.getContents());

				LabelCell familienName = (LabelCell) sheet.getCell(35, i);
				LabelCell vorName = (LabelCell) sheet.getCell(34, i);
				LabelCell strasse = (LabelCell) sheet.getCell(37, i);
				LabelCell ort = (LabelCell) sheet.getCell(41, i);
				LabelCell email = (LabelCell) sheet.getCell(45, i);

				LabelCell land = (LabelCell) sheet.getCell(38, i);

				String landString = land.getContents();

				if (landString.equals("A")) {
					landString = "AT";
				}

				String plz = "";
				if (sheet.getCell(40, i) instanceof LabelCell) {
					LabelCell plzCell = (LabelCell) sheet.getCell(40, i);
					plz = plzCell.getContents();
				} else if (sheet.getCell(40, i) instanceof NumberCell) {
					NumberCell plzCell = (NumberCell) sheet.getCell(40, i);
					plz = plzCell.getContents();
				}

				if (personNr.equals(0))

				{

					personContainer.addContainerFilter(new Equal("nachname", familienName.getContents()));
					personContainer.addContainerFilter(new Equal("vorname", vorName.getContents()));
					personContainer.addContainerFilter(new Equal("strasse", strasse.getContents()));
					personContainer.addContainerFilter(new Equal("ort", ort.getContents()));
					personContainer.addContainerFilter(new Equal("plz", plz));
					personContainer.addContainerFilter(new Equal("land", landString));
				} else {
					personContainer.addContainerFilter(new Equal("oerc_mitgliedsnummer", personNr));
				}

				Item personItem;
				Object id;
				if (personContainer.size() == 0) {
					id = personContainer.addItem();
					personItem = personContainer.getItemUnfiltered(id);
				} else {
					id = personContainer.firstItemId();
					personItem = personContainer.getItem(id);
				}

				personContainer.removeAllContainerFilters();

				personItem.getItemProperty("nachname").setValue(familienName.getContents());
				personItem.getItemProperty("vorname").setValue(vorName.getContents());
				personItem.getItemProperty("land").setValue(landString);
				personItem.getItemProperty("plz").setValue(plz);
				personItem.getItemProperty("strasse").setValue(strasse.getContents());
				personItem.getItemProperty("ort").setValue(ort.getContents());
				personItem.getItemProperty("email").setValue(email.getContents());

				if (sheet.getCell(33, i) instanceof EmptyCell) {

				} else {
					LabelCell titelCell = (LabelCell) sheet.getCell(33, i);
					personItem.getItemProperty("titel").setValue(titelCell.getContents());
				}

				personItem.getItemProperty("oerc_mitgliedsnummer").setValue(personNr.toString());
				personItem.getItemProperty("newsletter").setValue("N");
				personItem.getItemProperty("hausnummer").setValue("");

				personContainer.commit();
				personContainer.refresh();

				Integer internPersonId = null;
				if (personItem.getItemProperty("idperson").getValue() != null) {
					internPersonId = new Integer(personItem.getItemProperty("idperson").getValue().toString());
				} else {
					internPersonId = new Integer(personId.getId()[0].toString());

				}

				String chipString = "";
				if (sheet.getCell(22, i) instanceof LabelCell) {
					LabelCell chipNr = (LabelCell) sheet.getCell(22, i);
					chipString = chipNr.getContents();
				} else if (sheet.getCell(22, i) instanceof NumberCell) {
					NumberCell chipNr = (NumberCell) sheet.getCell(22, i);
					chipString = chipNr.getContents();
				}

				hundContainer.addContainerFilter(new Equal("idperson", internPersonId));
				hundContainer.addContainerFilter(new Equal("chipnummer", chipString));

				Item hundItem;
				Object hundId;
				if (hundContainer.size() == 0) {
					hundId = hundContainer.addItem();
					hundItem = hundContainer.getItemUnfiltered(hundId);
				} else {
					hundId = hundContainer.firstItemId();
					hundItem = hundContainer.getItem(hundId);
				}

				hundContainer.removeAllContainerFilters();

				hundItem.getItemProperty("idperson").setValue(internPersonId);
				hundItem.getItemProperty("chipnummer").setValue(chipString);
				if (sheet.getCell(16, i) instanceof EmptyCell) {

				} else {
					NumberCell hundeNr = (NumberCell) sheet.getCell(16, i);
					hundItem.getItemProperty("hundenr").setValue(new Integer(hundeNr.getContents()));

				}

				LabelCell nameCell = (LabelCell) sheet.getCell(17, i);
				hundItem.getItemProperty("zwingername").setValue(nameCell.getContents());

				if (sheet.getCell(18, i) instanceof EmptyCell) {
					hundItem.getItemProperty("rufname").setValue("");

				} else {
					LabelCell rufname = (LabelCell) sheet.getCell(18, i);
					hundItem.getItemProperty("rufname").setValue(rufname.getContents());
				}
				LabelCell idRasse = (LabelCell) sheet.getCell(10, i);
				switch (idRasse.getContents()) {
				case "CBRET":
					hundItem.getItemProperty("rasse").setValue("CBR");
					break;
				case "FRET":
					hundItem.getItemProperty("rasse").setValue("FCR");
					break;
				case "GRET":
					hundItem.getItemProperty("rasse").setValue("GR");
					break;
				case "CCRET":
					hundItem.getItemProperty("rasse").setValue("CCR");
					break;
				case "DRET":
					hundItem.getItemProperty("rasse").setValue("DTR");
					break;
				case "LRET":
					hundItem.getItemProperty("rasse").setValue("LR");
					break;

				}

				LabelCell geschlecht = (LabelCell) sheet.getCell(11, i);
				if (geschlecht.getContents().contains("dog")) {
					hundItem.getItemProperty("geschlecht").setValue("R");
				} else {
					hundItem.getItemProperty("geschlecht").setValue("H");
				}

				LabelCell zbnr = (LabelCell) sheet.getCell(21, i);
				hundItem.getItemProperty("zuchtbuchnummer").setValue(zbnr.getContents());

				Double gruppe = new Double(0);
				if (sheet.getCell(63, i) instanceof EmptyCell) {

				} else {
					NumberFormulaCell gruppenCell = (NumberFormulaCell) sheet.getCell(63, i);
					gruppe = gruppenCell.getValue();
				}

				Double formWert = new Double(0);
				if (sheet.getCell(64, i) instanceof EmptyCell) {
					
				} else {
					NumberFormulaCell formWertCell = (NumberFormulaCell) sheet.getCell(64, i);
					formWert = formWertCell.getValue();
				}

				DateCell wurfCell = (DateCell) sheet.getCell(19, i);
				hundItem.getItemProperty("wurfdatum").setValue(wurfCell.getDate());

				LabelCell farbe = (LabelCell) sheet.getCell(20,i);
				hundItem.getItemProperty("farbe").setValue(farbe.getString());
				hundContainer.commit();
				hundContainer.refresh();

				if (this.meldeComponent instanceof AnmeldungsPanel) {

					Integer internHundId = null;
					if (hundItem.getItemProperty("idhund").getValue() != null) {
						internHundId = new Integer(hundItem.getItemProperty("idhund").getValue().toString());
					} else {
						internHundId = new Integer(personId.getId()[0].toString());
					}

					Component zw = ((AnmeldungsPanel) meldeComponent).getMeldeComponent();
					((VeranstaltungsTeilnehmerGrid) zw).meldeHundId(internHundId, gruppe.intValue(), formWert.toString());
				}

			}

		} catch (Exception e) {
			new Notification("Fehler beim Verarbeiten der Datei", e.getMessage(), Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());
			e.printStackTrace();
		}

	}

	@Override
	public void uploadFailed(FailedEvent event) {
		// TODO Auto-generated method stub

		new Notification("Fehler beim Upload der Datei", Notification.Type.ERROR_MESSAGE).show(Page.getCurrent());

	}

	@Override
	public void rowIdChange(RowIdChangeEvent event) {
		personId = event.getNewRowId();

	}
}
