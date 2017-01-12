package com.app.Components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.EmptyCell;
import jxl.read.biff.BlankCell;

import com.app.dbIO.DBConnection;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class HundeImport extends CustomComponent implements View{

	private SQLContainer hundContainer;
	private TableQuery q1;

	private VerticalLayout mainLayout;

	public HundeImport() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

	}

	private void buildMainLayout() {
		mainLayout = new VerticalLayout();

		FileUploader receiver = new FileUploader();

		// Create the upload with a caption and set receiver later
		Upload upload = new Upload("starte Datenupload hier", receiver);
		upload.setButtonCaption("Start Upload");
		upload.addSucceededListener(receiver);
		upload.addFailedListener(receiver);
		upload.setId("upload");

		mainLayout.addComponent(upload);

	}

	// Implement both receiver that saves upload in a file and
	// listener for successful upload
	private class FileUploader implements Receiver, SucceededListener,
			FailedListener {
		public File file;
		public String filename;

		public OutputStream receiveUpload(String filename, String mimeType) {
			// Create upload stream
			FileOutputStream fos = null; // Stream to write to
			try {
				// Open the file for writing.
				file = new File(filename);
				fos = new FileOutputStream(file);
			} catch (final java.io.FileNotFoundException e) {
				new Notification("Could not open file<br/>", e.getMessage(),
						Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
				return null;
			}
			return fos; // Return the output stream to write to
		}

		public void uploadSucceeded(SucceededEvent event) {
			// Show the uploaded file in the image viewer
			q1 = new TableQuery("oerc_hund",
					DBConnection.INSTANCE.getConnectionPool());
			q1.setVersionColumn("version");

			try {
				Workbook workbook = Workbook.getWorkbook(file);
				Sheet sheet = workbook.getSheet(0);
				hundContainer = new SQLContainer(q1);
				hundContainer.setAutoCommit(false);

				for (int i = 1; i < sheet.getRows(); i++) {

					System.out.println("verarbeite zeile: " + i);
					NumberCell hundeCell = (NumberCell) sheet.getCell(0, i);

					Integer hundeNr = new Integer(hundeCell.getContents());

					hundContainer.addContainerFilter(new Equal("hundenr",
							hundeNr));

					Item hundItem;
					Object id;
					if (hundContainer.size() == 0) {
						id = hundContainer.addItem();
						hundItem = hundContainer.getItemUnfiltered(id);
					} else {
						id = hundContainer.firstItemId();
						hundItem = hundContainer.getItem(id);
					}
					
					hundContainer.removeAllContainerFilters();
					
					LabelCell nameCell = (LabelCell) sheet.getCell(1, i);
					
					if (sheet.getCell(3,i) instanceof BlankCell) {
						
					} else {
						DateCell wurfDatum = (DateCell) sheet.getCell(3, i);
						hundItem.getItemProperty("wurfdatum").setValue(
								wurfDatum.getDate());
					}
					
					NumberCell idRasse = (NumberCell) sheet.getCell(4, i);

					if (sheet.getCell(11, i) instanceof EmptyCell) {

					} else {
						LabelCell chipNr = (LabelCell) sheet.getCell(11, i);
						String chipString = chipNr.getContents().replaceAll("[a-zA-Z]", "");
						chipString = chipString.replace("-","");
						chipString = chipString.replace(".", "");
						chipString = chipString.replace("� ","");
						hundItem.getItemProperty("chipnummer").setValue(chipString);
						
					}

					LabelCell geschlecht = (LabelCell) sheet.getCell(6, i);

					if (sheet.getCell(14, i) instanceof EmptyCell) {

					} else {
						LabelCell titel = (LabelCell) sheet.getCell(14, i);
						hundItem.getItemProperty("titel").setValue(
								titel.getContents());
					}

					LabelCell zbnr = (LabelCell) sheet.getCell(19, i);

					if (sheet.getCell(24, i) instanceof EmptyCell) {
					} else {
						NumberCell mitgliedNr = (NumberCell) sheet.getCell(24, i);
						LabelCell mitgliedNachName = (LabelCell) sheet.getCell(
								25, i);
						LabelCell mitgliedVorname = (LabelCell) sheet.getCell(
								26, i);
						
						hundItem.getItemProperty("mitgliednr").setValue(
								Integer.valueOf(mitgliedNr.getContents()));
						hundItem.getItemProperty("mitglied_nachname").setValue(
								mitgliedNachName.getContents());
						hundItem.getItemProperty("mitglied_vorname").setValue(
								mitgliedVorname.getContents());
					}

					hundItem.getItemProperty("hundenr").setValue(hundeNr);
					hundItem.getItemProperty("name").setValue(
							nameCell.getContents());
					
					switch (idRasse.getContents()) {
					case "4":
						hundItem.getItemProperty("rasse").setValue("CBR");
						break;
					case "3":
						hundItem.getItemProperty("rasse").setValue("FCR");
						break;
					case "1":
						hundItem.getItemProperty("rasse").setValue("GR");
						break;
					case "6":
						hundItem.getItemProperty("rasse").setValue("CCR");
						break;
					case "5":
						hundItem.getItemProperty("rasse").setValue("DTR");
						break;
					case "2":
						hundItem.getItemProperty("rasse").setValue("LR");
						break;

					}

					if (geschlecht.getContents().contains("dog")) {
						hundItem.getItemProperty("geschlecht").setValue("R");
					} else {
						hundItem.getItemProperty("geschlecht").setValue("H");
					}

					hundItem.getItemProperty("zbnr").setValue(
							zbnr.getContents());
					
					
				}
				
				hundContainer.commit();
				
			} catch (Exception e) {
				new Notification("Fehler beim Verarbeiten der Datei",
						e.getMessage(), Notification.Type.ERROR_MESSAGE)
						.show(Page.getCurrent());
				e.printStackTrace();
			}

		}

		@Override
		public void uploadFailed(FailedEvent event) {
			// TODO Auto-generated method stub
			
			new Notification("Fehler beim Upload der Datei",
					 Notification.Type.ERROR_MESSAGE)
					.show(Page.getCurrent());

		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	};
}
