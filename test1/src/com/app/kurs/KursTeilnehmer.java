package com.app.kurs;

import com.app.auth.Hund;

public class KursTeilnehmer {

	private Integer idKursTeilnehmer;
	private Integer version;
	private Integer idKursStunde;
	private Integer idHund;
	private String abweichenderHundeFuehrer;
	private String bezahlt;
	
	private Hund hund;

	public Integer getIdKursTeilnehmer() {
		return idKursTeilnehmer;
	}

	public void setIdKursTeilnehmer(Integer idKursTeilnehmer) {
		this.idKursTeilnehmer = idKursTeilnehmer;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getIdKursStunde() {
		return idKursStunde;
	}

	public void setIdKursStunde(Integer idKursStunde) {
		this.idKursStunde = idKursStunde;
	}

	public Integer getIdHund() {
		return idHund;
	}

	public void setIdHund(Integer idHund) {
		this.idHund = idHund;
	}

	public String getAbweichenderHundeFuehrer() {
		return abweichenderHundeFuehrer;
	}

	public void setAbweichenderHundeFuehrer(String abweichenderHundeFuehrer) {
		this.abweichenderHundeFuehrer = abweichenderHundeFuehrer;
	}

	public String getBezahlt() {
		return bezahlt;
	}

	public void setBezahlt(String bezahlt) {
		this.bezahlt = bezahlt;
	}

	public Hund getHund() {
		return hund;
	}

	public void setHund(Hund hund) {
		this.hund = hund;
	}

}
