package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

import java.util.Date;

/**
 * oerc_hund generated by hbm2java
 */
public class oerc_hund implements java.io.Serializable {

	private int oercid;
	private int version;
	private int hundenr;
	private Date wurfdatum;
	private String chipnummer;
	private String titel;
	private String mitgliedNachname;
	private String mitgliedVorname;
	private String name;
	private String rasse;
	private char geschlecht;
	private String zbnr;
	private Integer mitgliednr;
	private String zuchtbuch;

	public oerc_hund() {
	}

	public oerc_hund(int hundenr, String name, String rasse, char geschlecht, String zbnr) {
		this.hundenr = hundenr;
		this.name = name;
		this.rasse = rasse;
		this.geschlecht = geschlecht;
		this.zbnr = zbnr;
	}

	public oerc_hund(int hundenr, Date wurfdatum, String chipnummer, String titel, String mitgliedNachname,
			String mitgliedVorname, String name, String rasse, char geschlecht, String zbnr, Integer mitgliednr,
			String zuchtbuch) {
		this.hundenr = hundenr;
		this.wurfdatum = wurfdatum;
		this.chipnummer = chipnummer;
		this.titel = titel;
		this.mitgliedNachname = mitgliedNachname;
		this.mitgliedVorname = mitgliedVorname;
		this.name = name;
		this.rasse = rasse;
		this.geschlecht = geschlecht;
		this.zbnr = zbnr;
		this.mitgliednr = mitgliednr;
		this.zuchtbuch = zuchtbuch;
	}

	public int getOercid() {
		return this.oercid;
	}

	public void setOercid(int oercid) {
		this.oercid = oercid;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getHundenr() {
		return this.hundenr;
	}

	public void setHundenr(int hundenr) {
		this.hundenr = hundenr;
	}

	public Date getWurfdatum() {
		return this.wurfdatum;
	}

	public void setWurfdatum(Date wurfdatum) {
		this.wurfdatum = wurfdatum;
	}

	public String getChipnummer() {
		return this.chipnummer;
	}

	public void setChipnummer(String chipnummer) {
		this.chipnummer = chipnummer;
	}

	public String getTitel() {
		return this.titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getMitgliedNachname() {
		return this.mitgliedNachname;
	}

	public void setMitgliedNachname(String mitgliedNachname) {
		this.mitgliedNachname = mitgliedNachname;
	}

	public String getMitgliedVorname() {
		return this.mitgliedVorname;
	}

	public void setMitgliedVorname(String mitgliedVorname) {
		this.mitgliedVorname = mitgliedVorname;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRasse() {
		return this.rasse;
	}

	public void setRasse(String rasse) {
		this.rasse = rasse;
	}

	public char getGeschlecht() {
		return this.geschlecht;
	}

	public void setGeschlecht(char geschlecht) {
		this.geschlecht = geschlecht;
	}

	public String getZbnr() {
		return this.zbnr;
	}

	public void setZbnr(String zbnr) {
		this.zbnr = zbnr;
	}

	public Integer getMitgliednr() {
		return this.mitgliednr;
	}

	public void setMitgliednr(Integer mitgliednr) {
		this.mitgliednr = mitgliednr;
	}

	public String getZuchtbuch() {
		return this.zuchtbuch;
	}

	public void setZuchtbuch(String zuchtbuch) {
		this.zuchtbuch = zuchtbuch;
	}

}