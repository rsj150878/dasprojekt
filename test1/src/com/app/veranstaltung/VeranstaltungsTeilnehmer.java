package com.app.veranstaltung;

public class VeranstaltungsTeilnehmer {

	private Integer idTeilnehmer;
	private Integer version;
	private Integer idVeranstaltung;
	private Integer idStufe;
	private Integer idPerson;
	private Integer idHund;
	private String bezahlt;
	private String bestanden;
	private Integer gesPunkte;
	private String hundefuehrer;
	private Integer uebung1;
	private Integer uebung2;
	private Integer uebung3;
	private Integer uebung4;
	private Integer uebung5;
	private Integer uebung6;
	private Integer uebung7;
	private Integer uebung8;

	private String sonderWertung;
	private String bemerkung;
	private Integer gruppe;
	private String formwert;
	private Integer platzierung;
	private Integer startnr;
	public Integer getIdTeilnehmer() {
		return idTeilnehmer;
	}
	public void setIdTeilnehmer(Integer idTeilnehmer) {
		this.idTeilnehmer = idTeilnehmer;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getIdVeranstaltung() {
		return idVeranstaltung;
	}
	public void setIdVeranstaltung(Integer idVeranstaltung) {
		this.idVeranstaltung = idVeranstaltung;
	}
	public Integer getIdStufe() {
		return idStufe;
	}
	public void setIdStufe(Integer idStufe) {
		this.idStufe = idStufe;
	}
	public Integer getIdPerson() {
		return idPerson;
	}
	public void setIdPerson(Integer idPerson) {
		this.idPerson = idPerson;
	}
	public Integer getIdHund() {
		return idHund;
	}
	public void setIdHund(Integer idHund) {
		this.idHund = idHund;
	}
	public String getBezahlt() {
		return bezahlt;
	}
	public void setBezahlt(String bezahlt) {
		this.bezahlt = bezahlt;
	}
	public String getBestanden() {
		return bestanden;
	}
	public void setBestanden(String bestanden) {
		this.bestanden = bestanden;
	}
	public Integer getGesPunkte() {
		return gesPunkte;
	}
	public void setGesPunkte(Integer gesPunkte) {
		this.gesPunkte = gesPunkte;
	}
	public String getHundefuehrer() {
		return hundefuehrer;
	}
	public void setHundefuehrer(String hundefuehrer) {
		this.hundefuehrer = hundefuehrer;
	}
	public Integer getUebung1() {
		return uebung1;
	}
	public void setUebung1(Integer uebung1) {
		this.uebung1 = uebung1;
	}
	public Integer getUebung2() {
		return uebung2;
	}
	public void setUebung2(Integer uebung2) {
		this.uebung2 = uebung2;
	}
	public Integer getUebung3() {
		return uebung3;
	}
	public void setUebung3(Integer uebung3) {
		this.uebung3 = uebung3;
	}
	public Integer getUebung4() {
		return uebung4;
	}
	public void setUebung4(Integer uebung4) {
		this.uebung4 = uebung4;
	}
	public Integer getUebung5() {
		return uebung5;
	}
	public void setUebung5(Integer uebung5) {
		this.uebung5 = uebung5;
	}
	public Integer getUebung6() {
		return uebung6;
	}
	public void setUebung6(Integer uebung6) {
		this.uebung6 = uebung6;
	}
	public Integer getUebung7() {
		return uebung7;
	}
	public void setUebung7(Integer uebung7) {
		this.uebung7 = uebung7;
	}
	public String getSonderWertung() {
		return sonderWertung;
	}
	public void setSonderWertung(String sonderWertung) {
		this.sonderWertung = sonderWertung;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public Integer getGruppe() {
		return gruppe;
	}
	public void setGruppe(Integer gruppe) {
		this.gruppe = gruppe;
	}
	public String getFormwert() {
		return formwert;
	}
	public void setFormwert(String formwert) {
		this.formwert = formwert;
	}
	public Integer getPlatzierung() {
		return platzierung;
	}
	public void setPlatzierung(Integer platzierung) {
		this.platzierung = platzierung;
	}
	public Integer getStartnr() {
		return startnr;
	}
	public void setStartnr(Integer startnr) {
		this.startnr = startnr;
	}
	
	public void setPunkteFuerUebung(Integer uebung, Integer punkte ) {
		switch (uebung) { 
		case 1:
			uebung1 = punkte;
			break;
		case 2:
			uebung2 = punkte;
			break;
		case 3:
			uebung3 = punkte;
			break;
		case 4:
			uebung4 = punkte;
			break;
		case 5:
			uebung5 = punkte;
			break;
		case 6:
			uebung6 = punkte;
			break;
		case 7:
			uebung7 = punkte;
			break;
		case 8:
			uebung8 = punkte;
			break;
		}
		
		gesPunkte = uebung1 + uebung2 + uebung3 + uebung4 + uebung5 + uebung6 + uebung7 + uebung8;
	}
	public Integer getUebung8() {
		return uebung8;
	}
	public void setUebung8(Integer uebung8) {
		this.uebung8 = uebung8;
	}
	
	
}
