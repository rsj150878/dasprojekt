package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

/**
 * kurstag generated by hbm2java
 */
public class kurstag implements java.io.Serializable {

	private int idkurstag;
	private int version;
	private String bezeichnung;
	private int idkurs;

	public kurstag() {
	}

	public kurstag(String bezeichnung, int idkurs) {
		this.bezeichnung = bezeichnung;
		this.idkurs = idkurs;
	}

	public int getIdkurstag() {
		return this.idkurstag;
	}

	public void setIdkurstag(int idkurstag) {
		this.idkurstag = idkurstag;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public int getIdkurs() {
		return this.idkurs;
	}

	public void setIdkurs(int idkurs) {
		this.idkurs = idkurs;
	}

}