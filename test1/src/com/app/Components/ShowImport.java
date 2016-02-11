package com.app.Components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import com.app.dbIO.DBConnection;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
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

public class ShowImport extends CustomComponent {

	private VerticalLayout mainLayout;

	public ShowImport() {
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
				Table table = db.getTable("OPTIAutoCheckin");

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
