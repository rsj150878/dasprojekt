package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

/**
 * veranstaltungs_stufe generated by hbm2java
 */
public class veranstaltungs_stufe implements java.io.Serializable {

	private int idStufe;
	private int version;
	private int idVeranstaltung;
	private int stufenTyp;
	private String richter;
	private String veranstaltungsLeiter;
	private String datum;

	public veranstaltungs_stufe() {
	}

	public veranstaltungs_stufe(int idVeranstaltung, int stufenTyp) {
		this.idVeranstaltung = idVeranstaltung;
		this.stufenTyp = stufenTyp;
	}

	public veranstaltungs_stufe(int idVeranstaltung, int stufenTyp, String richter, String veranstaltungsLeiter,
			String datum) {
		this.idVeranstaltung = idVeranstaltung;
		this.stufenTyp = stufenTyp;
		this.richter = richter;
		this.veranstaltungsLeiter = veranstaltungsLeiter;
		this.datum = datum;
	}

	public int getIdStufe() {
		return this.idStufe;
	}

	public void setIdStufe(int idStufe) {
		this.idStufe = idStufe;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getIdVeranstaltung() {
		return this.idVeranstaltung;
	}

	public void setIdVeranstaltung(int idVeranstaltung) {
		this.idVeranstaltung = idVeranstaltung;
	}

	public int getStufenTyp() {
		return this.stufenTyp;
	}

	public void setStufenTyp(int stufenTyp) {
		this.stufenTyp = stufenTyp;
	}

	public String getRichter() {
		return this.richter;
	}

	public void setRichter(String richter) {
		this.richter = richter;
	}

	public String getVeranstaltungsLeiter() {
		return this.veranstaltungsLeiter;
	}

	public void setVeranstaltungsLeiter(String veranstaltungsLeiter) {
		this.veranstaltungsLeiter = veranstaltungsLeiter;
	}

	public String getDatum() {
		return this.datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

}