package com.app.veranstaltung;

import java.util.Date;

import com.app.enumdatatypes.VeranstaltungsStufen;

public class VeranstaltungsStufe {
	private Integer idStufe;
	private String VeranstaltungsLeiter;
	private VeranstaltungsStufen stufenTyp;
	private String richter;
	private Integer idVeranstaltung;
	private Integer version;
	private Date datum;
	
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
	public VeranstaltungsStufen getStufenTyp() {
		return stufenTyp;
	}
	public void setStufenTyp(VeranstaltungsStufen stufenTyp) {
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
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	
	
	

}
