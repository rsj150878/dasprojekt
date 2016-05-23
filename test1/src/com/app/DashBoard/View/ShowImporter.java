package com.app.DashBoard.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.app.enumPackage.Rassen;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeListener;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ShowImporter extends VerticalLayout implements View {

	OptionGroup showType;

	public ShowImporter() {
		DashBoardEventBus.register(this);

		setSizeFull();
		addStyleName("myhundeview");

		addComponent(buildToolbar());
		addComponent(buildWorkingArea());

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("MeineHunde");
		title.setSizeUndefined();

		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);

		header.addComponent(title);

		return header;
	}

	private Component buildWorkingArea() {
		VerticalLayout mainLayout = new VerticalLayout();

		FileUploader receiver = new FileUploader();

		showType = new OptionGroup("Schautyp");
		showType.addItem("C");
		showType.setItemCaption("C", "Clubschau");
		showType.addItem("I");
		showType.setItemCaption("I", "Internationale Ausstellung");
		showType.setNullSelectionAllowed(false);
		showType.select("I");

		mainLayout.addComponent(showType);

		// Create the upload with a caption and set receiver later
		Upload upload = new Upload("starte Datenupload hier", receiver);
		upload.setButtonCaption("Start Upload");
		upload.addSucceededListener(receiver);
		upload.addFailedListener(receiver);
		upload.setId("upload");

		mainLayout.addComponent(upload);

		return (mainLayout);

	}

	// Implement both receiver that saves upload in a file and
	// listener for successful upload
	private class FileUploader implements Receiver, SucceededListener,
			FailedListener {
		public File file;
		public String filename;

		private SQLContainer schauContainer;
		private TableQuery q1;
		private SQLContainer schauRingContainer;
		private TableQuery q2;
		private SQLContainer schauHundContainer;
		private TableQuery q3;

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
			q1 = new TableQuery("schau",
					DBConnection.INSTANCE.getConnectionPool());
			q1.setVersionColumn("version");

			q2 = new TableQuery("schauring",
					DBConnection.INSTANCE.getConnectionPool());
			q2.setVersionColumn("version");

			q3 = new TableQuery("schauhund",
					DBConnection.INSTANCE.getConnectionPool());
			q3.setVersionColumn("version");

			try {
				Database db = DatabaseBuilder.open(file);
				Table table = db.getTable("T_Ausstellungsdaten");

				for (Row row : table) {

					String rasse = "";
					switch (row.getString("Rassename")) {
					case "Golden Retriever":
						rasse = Rassen.GOLDEN_RETRIEVER
								.getRassenKurzBezeichnung();
						break;
					case "Labrador Retriever":
						rasse = Rassen.LABRADOR_RETRIEVER
								.getRassenKurzBezeichnung();
						break;
					case "Flat-Coated Retriever":
						rasse = Rassen.FLAT_COATED_RETRIEVER
								.getRassenKurzBezeichnung();
						break;
					case "Nova Scotia Duck Tolling Retriever":
						rasse = Rassen.NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER
								.getRassenKurzBezeichnung();
						break;
					case "Chesapeake Bay Retriever":
						rasse = Rassen.CHESAPEAKE_BAY_RETRIEVER
								.getRassenKurzBezeichnung();
						break;
					case "Curly-Coated Retriever":
						rasse = Rassen.CURLY_COATED_RETRIEVER
								.getRassenKurzBezeichnung();
						break;
					default:
						break;
					}

					if (!"".equals(rasse)) {
						String show = row.getString("AusstellungsCode");

						schauContainer = new SQLContainer(q1);
						schauContainer.addContainerFilter(new Equal(
								"schaukuerzel", show));

						IdClass schauId = new IdClass();
						schauContainer.addRowIdChangeListener(schauId);

						Object id;
						Item newItem;

						if (schauContainer.size() == 0) {
							id = schauContainer.addItem();
							newItem = schauContainer.getItemUnfiltered(id);
						} else {
							id = schauContainer.getIdByIndex(0);
							newItem = schauContainer.getItem(id);
						}
						newItem.getItemProperty("schaukuerzel").setValue(
								row.getString("AusstellungsCode"));
						newItem.getItemProperty("bezeichnung").setValue(
								row.getString("Ausstellung"));
						newItem.getItemProperty("datum").setValue(
								row.getDate("Datum"));
						newItem.getItemProperty("schautyp").setValue(
								showType.getValue());

						schauContainer.commit();
						schauContainer.refresh();

						id = schauContainer.getIdByIndex(0);
						newItem = schauContainer.getItem(id);

						System.out.println("id: "
								+ newItem.getItemProperty("idschau").getValue()
										.toString());

						schauRingContainer = new SQLContainer(q2);
						schauRingContainer.addContainerFilter(new Equal(
								"idschau", newItem.getItemProperty("idschau")
										.getValue()));
						schauRingContainer.addContainerFilter(new Equal(
								"ringnummer", row.getInt("Ring").toString()));

						Item schauRingContainerItem;
						if (schauRingContainer.size() == 0) {
							id = schauRingContainer.addItem();
							schauRingContainerItem = schauRingContainer
									.getItemUnfiltered(id);
						} else {
							id = schauRingContainer.getIdByIndex(0);
							schauRingContainerItem = schauRingContainer
									.getItem(id);
						}

						schauRingContainerItem.getItemProperty("ringnummer")
								.setValue(row.getInt("Ring").toString());
						schauRingContainerItem.getItemProperty("richter")
								.setValue(row.getString("Richtername"));
						schauRingContainerItem.getItemProperty("idschau")
								.setValue(
										newItem.getItemProperty("idschau")
												.getValue());

						schauRingContainer.commit();
						schauRingContainer.refresh();
						id = schauRingContainer.getIdByIndex(0);
						schauRingContainerItem = schauRingContainer.getItem(id);

						schauHundContainer = new SQLContainer(q3);
						schauHundContainer.addContainerFilter(new Equal(
								"idschauring", schauRingContainerItem
										.getItemProperty("idschauring")
										.getValue()));
						schauHundContainer.addContainerFilter(new Equal(
								"katalognummer", row.getInt("Katalognummer")
										.toString().trim()
										+ row.getString("aNummer").trim()));

						Item schauHundItem;

						if (schauHundContainer.size() == 0) {
							id = schauHundContainer.addItem();
							schauHundItem = schauHundContainer
									.getItemUnfiltered(id);
						} else {
							id = schauHundContainer.getIdByIndex(0);
							schauHundItem = schauHundContainer.getItem(id);
						}

						System.out.println("value: "
								+ schauRingContainerItem.getItemProperty(
										"idschauring").getValue());
						schauHundItem.getItemProperty("idschauring").setValue(

								schauRingContainerItem.getItemProperty(
										"idschauring").getValue());
						schauHundItem.getItemProperty("name").setValue(
								row.getString("Hundename"));
						schauHundItem.getItemProperty("wurftag").setValue(
								row.getDate("Wurfdatum"));
						schauHundItem.getItemProperty("zuchtbuchnummer")
								.setValue(row.getString("Zuchtbuchnummer"));
						schauHundItem.getItemProperty("chipnummer").setValue(
								"000");
						schauHundItem.getItemProperty("klasse").setValue(
								row.getString("Klasse"));

						schauHundItem.getItemProperty("vater").setValue(
								row.getString("Vater"));
						schauHundItem.getItemProperty("mutter").setValue(
								row.getString("Mutter"));

						schauHundItem.getItemProperty("besitzershow").setValue(
								row.getString("Besitzername"));

						schauHundItem.getItemProperty("katalognummer")
								.setValue(
										row.getInt("Katalognummer").toString()
												.trim()
												+ row.getString("aNummer")
														.trim());

						if (!(row.getString("Beschreibung") == null)) {
							schauHundItem.getItemProperty("bewertung")
									.setValue(row.getString("Beschreibung"));
							

						}

						schauHundItem
						.getItemProperty("hundfehlt")
						.setValue(
								row.getBoolean("fehlt") ? "J" : "N");
						
						schauHundItem.getItemProperty("rasse").setValue(rasse);
						schauHundItem.getItemProperty("idschau").setValue(
								newItem.getItemProperty("idschau").getValue());

						schauHundItem.getItemProperty("geschlecht").setValue(
								row.getString("Geschlecht"));
						schauHundItem.getItemProperty("zuechter").setValue(
								row.getString("Zuechter"));

						if (!(row.getInt("Numerierung") == null)) {
							schauHundItem.getItemProperty("platzierung")
									.setValue(
											row.getInt("Numerierung")
													.toString());
						}

						if (!(row.getInt("Bewertung") == null)) {
							switch (row.getInt("Bewertung")) {
							case 1:
								schauHundItem.getItemProperty("formwert")
										.setValue("v");
								break;
							case 2:
								schauHundItem.getItemProperty("formwert")
										.setValue("sg");
								break;
							case 3:
								schauHundItem.getItemProperty("formwert")
										.setValue("g");
								break;
							case 4:
								schauHundItem.getItemProperty("formwert")
										.setValue("gen");
								break;
							case 5:
								schauHundItem.getItemProperty("formwert")
										.setValue("ob");
								break;
							default:
								System.out
										.println("unbekannter wert für formwert erwachsene: "
												+ row.getInt("Bewertung"));
								break;
							}
						}

						if (!(row.getInt("Juengstenklasse") == null)) {
							switch (row.getInt("Juengstenklasse")) {
							case 6:
								schauHundItem.getItemProperty("formwert")
										.setValue("vv");
								break;
							case 7:
								schauHundItem.getItemProperty("formwert")
										.setValue("v");
								break;
							case 8:
								schauHundItem.getItemProperty("formwert")
										.setValue("g");
								break;
							default:
								System.out
										.println("unbekannter wert für formwert jüngsten: "
												+ row.getInt("Bewertung"));
								break;
							}
						}

						if (!(row.getInt("CACA_JB") == null)) {
							switch (row.getInt("CACA_JB")) {
							case 1:
								schauHundItem.getItemProperty("CACA").setValue(
										"J");
								break;
							case 2:
								schauHundItem.getItemProperty("CACA").setValue(
										"C");
								break;
							case 3:
								schauHundItem.getItemProperty("CACA").setValue(
										"R");
								break;
							}
						}

						if (!(row.getInt("CACIB") == null)) {
							switch (row.getInt("CACIB")) {
							case 1:
								schauHundItem.getItemProperty("CACIB")
										.setValue("C");
								break;
							case 2:
								schauHundItem.getItemProperty("CACIB")
										.setValue("R");
								break;
							}
						}

						if (!(row.getInt("BobBos") == null)) {
							switch (row.getInt("BobBos")) {
							case 1:
								schauHundItem.getItemProperty("BOB").setValue(
										"B");
								break;
							case 2:
								schauHundItem.getItemProperty("BOB").setValue(
										"O");
								break;
							}
						}

						schauHundContainer.commit();

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		@Override
		public void uploadFailed(FailedEvent event) {
			// TODO Auto-generated method stub

		}
	};

	private class IdClass implements RowIdChangeListener {

		Integer rowId;

		@Override
		public void rowIdChange(RowIdChangeEvent arg0) {
			// TODO Auto-generated method stub
			this.rowId = Integer.valueOf(arg0.getNewRowId().getId()[0]
					.toString());
			;
		}

		public Integer getRowId() {
			return this.rowId;
		}

	}

}
