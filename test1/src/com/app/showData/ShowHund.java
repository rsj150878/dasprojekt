package com.app.showData;

import java.util.Date;

import com.app.enumPackage.Rassen;

public class ShowHund extends ShowKlasseEnde {
	
	
	private String showHundName;
	private Integer idschauhund;
	private Integer version;
	private Date wurftag;
	private String zuchtbuchnummer;
	private String chipnummer;
	private String katalognumer;
	
	private String vater;
	private String mutter;
	private String besitzershow;
	private String bewertung;
	private String hundfehlt;
	private String mitglied;
	private String formwert;
	private String platzierung;
	private String CACA;
	private String CACIB;
	private String BOB;
	private String clubsieger;
	private String bestehrenring;
	private String platzehrenring;
	private String BOD;
	private String BIS;
	private String geschlecht;
	private String zuechter;
	private Integer sort_kat_nr;
	private Rassen rasse;
	private Integer id_teilnehmer;
	 
	public String getShowHundName() {
		return showHundName;
	}
	public void setShowHundName(String showHundName) {
		this.showHundName = showHundName;
	}
	public Integer getIdschauhund() {
		return idschauhund;
	}
	public void setIdschauhund(Integer idschauhund) {
		this.idschauhund = idschauhund;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Date getWurftag() {
		return wurftag;
	}
	public void setWurftag(Date wurftag) {
		this.wurftag = wurftag;
	}
	public String getZuchtbuchnummer() {
		return zuchtbuchnummer;
	}
	public void setZuchtbuchnummer(String zuchtbuchnummer) {
		this.zuchtbuchnummer = zuchtbuchnummer;
	}
	public String getChipnummer() {
		return chipnummer;
	}
	public void setChipnummer(String chipnummer) {
		this.chipnummer = chipnummer;
	}
	public String getKatalognumer() {
		System.out.println("katnrget +" + katalognumer + "+");
		return katalognumer;
	}
	public void setKatalognumer(String katalognumer) {
		this.katalognumer = katalognumer;
	}
	public String getVater() {
		return vater;
	}
	public void setVater(String vater) {
		this.vater = vater;
	}
	public String getMutter() {
		return mutter;
	}
	public void setMutter(String mutter) {
		this.mutter = mutter;
	}
	public String getBesitzershow() {
		return besitzershow;
	}
	public void setBesitzershow(String besitzershow) {
		this.besitzershow = besitzershow;
	}
	public String getBewertung() {
		return bewertung;
	}
	public void setBewertung(String bewertung) {
		this.bewertung = bewertung;
	}
	public String getHundfehlt() {
		return hundfehlt;
	}
	public void setHundfehlt(String hundfehlt) {
		this.hundfehlt = hundfehlt;
	}
	public String getMitglied() {
		return mitglied;
	}
	public void setMitglied(String mitglied) {
		this.mitglied = mitglied;
	}

	public String getFormwert() {
		return formwert;
	}
	public void setFormwert(String formwert) {
		this.formwert = formwert;
	}
	public String getPlatzierung() {
		return platzierung;
	}
	public void setPlatzierung(String platzierung) {
		this.platzierung = platzierung;
	}
	public String getCACA() {
		return CACA;
	}
	public void setCACA(String cACA) {
		CACA = cACA;
	}
	public String getCACIB() {
		return CACIB;
	}
	public void setCACIB(String cACIB) {
		CACIB = cACIB;
	}
	public String getBOB() {
		return BOB;
	}
	public void setBOB(String bOB) {
		BOB = bOB;
	}
	public String getClubsieger() {
		System.out.println("clubsieger " + this.idschauhund + "-" + clubsieger  + "-");
		return clubsieger;
	}
	public void setClubsieger(String clubsieger) {
		this.clubsieger = clubsieger;
	}
	public String getBestehrenring() {
		return bestehrenring;
	}
	public void setBestehrenring(String bestehrenring) {
		this.bestehrenring = bestehrenring;
	}
	public String getPlatzehrenring() {
		return platzehrenring;
	}
	public void setPlatzehrenring(String platzehrenring) {
		this.platzehrenring = platzehrenring;
	}
	public String getBOD() {
		return BOD;
	}
	public void setBOD(String bOD) {
		BOD = bOD;
	}
	public String getBIS() {
		return BIS;
	}
	public void setBIS(String bIS) {
		BIS = bIS;
	}
	public String getGeschlecht() {
		return geschlecht;
	}
	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}
	public String getZuechter() {
		return zuechter;
	}
	public void setZuechter(String zuechter) {
		this.zuechter = zuechter;
	}
	public Integer getSort_kat_nr() {
		return sort_kat_nr;
	}
	public void setSort_kat_nr(Integer sort_kat_nr) {
		this.sort_kat_nr = sort_kat_nr;
	}
	
	
	@Override
	public String getKatalogNummer() {
		return this.katalognumer + " - " + this.showHundName;
	}
	public Rassen getRasse() {
		System.out.println("rasse des hundes " + rasse);
		return rasse;
	}
	public void setRasse(Rassen rasse) {
		this.rasse = rasse;
	}
	public Integer getId_teilnehmer() {
		return id_teilnehmer;
	}
	public void setId_teilnehmer(Integer id_teilnehmer) {
		this.id_teilnehmer = id_teilnehmer;
	}
	

}
