package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

/**
 * Kurskartentyp generated by hbm2java
 */
public class Kurskartentyp implements java.io.Serializable {

	private int idkurskartentyp;
	private Integer maxbesuche;
	private String bezeichnung;

	public Kurskartentyp() {
	}

	public Kurskartentyp(Integer maxbesuche, String bezeichnung) {
		this.maxbesuche = maxbesuche;
		this.bezeichnung = bezeichnung;
	}

	public int getIdkurskartentyp() {
		return this.idkurskartentyp;
	}

	public void setIdkurskartentyp(int idkurskartentyp) {
		this.idkurskartentyp = idkurskartentyp;
	}

	public Integer getMaxbesuche() {
		return this.maxbesuche;
	}

	public void setMaxbesuche(Integer maxbesuche) {
		this.maxbesuche = maxbesuche;
	}

	public String getBezeichnung() {
		return this.bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

}