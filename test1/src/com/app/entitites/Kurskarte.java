package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

import java.util.Date;

/**
 * Kurskarte generated by hbm2java
 */
public class Kurskarte implements java.io.Serializable {

	private int idkurskarte;
	private int idkurskartentyp;
	private Date ausgestelltAm;
	private String bemerkungen;

	public Kurskarte() {
	}

	public Kurskarte(int idkurskartentyp, Date ausgestelltAm) {
		this.idkurskartentyp = idkurskartentyp;
		this.ausgestelltAm = ausgestelltAm;
	}

	public Kurskarte(int idkurskartentyp, Date ausgestelltAm, String bemerkungen) {
		this.idkurskartentyp = idkurskartentyp;
		this.ausgestelltAm = ausgestelltAm;
		this.bemerkungen = bemerkungen;
	}

	public int getIdkurskarte() {
		return this.idkurskarte;
	}

	public void setIdkurskarte(int idkurskarte) {
		this.idkurskarte = idkurskarte;
	}

	public int getIdkurskartentyp() {
		return this.idkurskartentyp;
	}

	public void setIdkurskartentyp(int idkurskartentyp) {
		this.idkurskartentyp = idkurskartentyp;
	}

	public Date getAusgestelltAm() {
		return this.ausgestelltAm;
	}

	public void setAusgestelltAm(Date ausgestelltAm) {
		this.ausgestelltAm = ausgestelltAm;
	}

	public String getBemerkungen() {
		return this.bemerkungen;
	}

	public void setBemerkungen(String bemerkungen) {
		this.bemerkungen = bemerkungen;
	}

}
