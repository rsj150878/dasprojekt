package com.app.kurs;

import java.sql.Time;

public class KursStunde {

	private Integer idKursStunde;
	private Integer version;
	private String bezeichnung;
	private Integer idKursTag;
	
	private Time startZeit;
	private Time endZeit;
	public Integer getIdKursStunde() {
		return idKursStunde;
	}
	public void setIdKursStunde(Integer idKursStunde) {
		this.idKursStunde = idKursStunde;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public Integer getIdKursTag() {
		return idKursTag;
	}
	public void setIdKursTag(Integer idKursTag) {
		this.idKursTag = idKursTag;
	}
	public Time getStartZeit() {
		return startZeit;
	}
	public void setStartZeit(Time startZeit) {
		this.startZeit = startZeit;
	}
	public Time getEndZeit() {
		return endZeit;
	}
	public void setEndZeit(Time endZeit) {
		this.endZeit = endZeit;
	}
	
	
}
