package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

/**
 * files generated by hbm2java
 */
public class files implements java.io.Serializable {

	private int idfiles;
	private String filename;
	private int gehoertZu;
	private int gehoertZuType;
	private byte[] fileContent;

	public files() {
	}

	public files(int idfiles, String filename, int gehoertZu, int gehoertZuType, byte[] fileContent) {
		this.idfiles = idfiles;
		this.filename = filename;
		this.gehoertZu = gehoertZu;
		this.gehoertZuType = gehoertZuType;
		this.fileContent = fileContent;
	}

	public int getIdfiles() {
		return this.idfiles;
	}

	public void setIdfiles(int idfiles) {
		this.idfiles = idfiles;
	}

	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getGehoertZu() {
		return this.gehoertZu;
	}

	public void setGehoertZu(int gehoertZu) {
		this.gehoertZu = gehoertZu;
	}

	public int getGehoertZuType() {
		return this.gehoertZuType;
	}

	public void setGehoertZuType(int gehoertZuType) {
		this.gehoertZuType = gehoertZuType;
	}

	public byte[] getFileContent() {
		return this.fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

}
