package com.app.veranstaltung;

public class VeranstaltungsStufe {
	private Integer idStufe;
	private String VeranstaltungsLeiter;
	private Integer stufenTyp;
	private String richter;
	private Integer idVeranstaltung;
	private Integer version;
	private String datum;
	
	public Integer getIdStufe() {
		return idStufe;
	}
	public void setIdStufe(Integer idStufe) {
		this.idStufe = idStufe;
	}
	public String getVeranstaltungsLeiter() {
		return VeranstaltungsLeiter;
	}
	public void setVeranstaltungsLeiter(String veranstaltungsLeiter) {
		VeranstaltungsLeiter = veranstaltungsLeiter;
	}
	public Integer getStufenTyp() {
		return stufenTyp;
	}
	public void setStufenTyp(Integer stufenTyp) {
		this.stufenTyp = stufenTyp;
	}
	public String getRichter() {
		return richter;
	}
	public void setRichter(String richter) {
		this.richter = richter;
	}
	public Integer getIdVeranstaltung() {
		return idVeranstaltung;
	}
	public void setIdVeranstaltung(Integer idVeranstaltung) {
		this.idVeranstaltung = idVeranstaltung;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	
	
	

}
