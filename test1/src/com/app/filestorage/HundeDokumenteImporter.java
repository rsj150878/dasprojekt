package com.app.filestorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.app.dbio.DBHundDokumente;
import com.app.enumdatatypes.DokumentGehoertZuType;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class HundeDokumenteImporter

		implements Receiver, SucceededListener, FailedListener{
	public File file;
	public String filename;
	private Integer gehoertZu;
	private DokumentGehoertZuType gehoertZuType;
	
	public HundeDokumenteImporter(DokumentGehoertZuType gehoertZuType, Integer gehoertZu) {
		this.gehoertZu = gehoertZu;
		this.gehoertZuType = gehoertZuType;
	}

	
	public HundeDokumenteImporter() {
	}

	public OutputStream receiveUpload(String filename, String mimeType) {
		// Create upload stream
		FileOutputStream fos = null; // Stream to write to
		this.filename = filename;

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

		try {
			HundeDokumente dokument = new HundeDokumente();
			dokument.setFileName(this.filename);
			dokument.setGehoertZu(gehoertZu);
			dokument.setGehoertZuType(gehoertZuType.getGehoertZu());
			dokument.setHundDokument(this.file);
			DBHundDokumente db = new DBHundDokumente();
			db.saveDokument(dokument);
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


	public void setGehoertZu(Integer gehoertZu) {
		this.gehoertZu = gehoertZu;
	}


	public void setGehoertZuType(DokumentGehoertZuType gehoertZuType) {
		this.gehoertZuType = gehoertZuType;
	}

	
}
