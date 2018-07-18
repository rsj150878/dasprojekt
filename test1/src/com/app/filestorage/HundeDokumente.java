package com.app.filestorage;

import java.io.File;

public class HundeDokumente {

	private Integer idFile;
	private String fileName;
	private Integer gehoertZu;
	private File hundDokument;
	private Integer gehoertZuType;
		
	public Integer getIdFile() {
		return idFile;
	}
	public void setIdFile(Integer idFile) {
		this.idFile = idFile;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getGehoertZu() {
		return gehoertZu;
	}
	public void setGehoertZu(Integer gehoertZu) {
		this.gehoertZu = gehoertZu;
	}
	public File getHundDokument() {
		return hundDokument;
	}
	public void setHundDokument(File hundDokument) {
		this.hundDokument = hundDokument;
	}
	public Integer getGehoertZuType() {
		return gehoertZuType;
	}
	public void setGehoertZuType(Integer gehoertZuType) {
		this.gehoertZuType = gehoertZuType;
	}
	
	
}
