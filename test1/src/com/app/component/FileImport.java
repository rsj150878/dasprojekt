package com.app.component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

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

public class FileImport extends CustomComponent {

	private VerticalLayout mainLayout;

	public FileImport() {
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
			
			try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			    for(String line; (line = br.readLine()) != null; ) {
			        // process the line.
			    	System.out.println(line);
			    } 
			    // line is not visible here.
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
