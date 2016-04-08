package com.app.DashBoard.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.app.DashBoard.Event.DashBoardEventBus;
import com.app.dbIO.DBConnection;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ShowImporter  extends VerticalLayout implements View {
	
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
		private class FileUploader implements Receiver, SucceededListener, FailedListener {
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
						
					}
				   
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void uploadFailed(FailedEvent event) {
				// TODO Auto-generated method stub
				
			}
		};

}
