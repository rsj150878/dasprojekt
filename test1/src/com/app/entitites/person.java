package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

import java.util.Date;

/**
 * person generated by hbm2java
 */
public class person implements java.io.Serializable {

	private int idperson;
	private int version;
	private Integer iduser;
	private String nachname;
	private String vorname;
	private String titel;
	private String strasse;
	private String hausnummer;
	private String plz;
	private String ort;
	private String land;
	private String email;
	private Date letzteAenderung;
	private Date gebDat;
	private String password;
	private Character newsletter;
	private String oercMitgliedsnummer;
	private String telnr;
	private String mobnr;
	private String email2;
	private Character newsletter2;
	private String email3;
	private Character newsletter3;
	private Character geschlecht;
	private String zusatztext;
	private String website;

	public person() {
	}

	public person(String nachname, String vorname, String strasse, String hausnummer, String plz, String ort,
			String land, Date letzteAenderung) {
		this.nachname = nachname;
		this.vorname = vorname;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.plz = plz;
		this.ort = ort;
		this.land = land;
		this.letzteAenderung = letzteAenderung;
	}

	public person(Integer iduser, String nachname, String vorname, String titel, String strasse, String hausnummer,
			String plz, String ort, String land, String email, Date letzteAenderung, Date gebDat, String password,
			Character newsletter, String oercMitgliedsnummer, String telnr, String mobnr, String email2,
			Character newsletter2, String email3, Character newsletter3, Character geschlecht, String zusatztext,
			String website) {
		this.iduser = iduser;
		this.nachname = nachname;
		this.vorname = vorname;
		this.titel = titel;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.plz = plz;
		this.ort = ort;
		this.land = land;
		this.email = email;
		this.letzteAenderung = letzteAenderung;
		this.gebDat = gebDat;
		this.password = password;
		this.newsletter = newsletter;
		this.oercMitgliedsnummer = oercMitgliedsnummer;
		this.telnr = telnr;
		this.mobnr = mobnr;
		this.email2 = email2;
		this.newsletter2 = newsletter2;
		this.email3 = email3;
		this.newsletter3 = newsletter3;
		this.geschlecht = geschlecht;
		this.zusatztext = zusatztext;
		this.website = website;
	}

	public int getIdperson() {
		return this.idperson;
	}

	public void setIdperson(int idperson) {
		this.idperson = idperson;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Integer getIduser() {
		return this.iduser;
	}

	public void setIduser(Integer iduser) {
		this.iduser = iduser;
	}

	public String getNachname() {
		return this.nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getVorname() {
		return this.vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getTitel() {
		return this.titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getStrasse() {
		return this.strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnummer() {
		return this.hausnummer;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getPlz() {
		return this.plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return this.ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getLand() {
		return this.land;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLetzteAenderung() {
		return this.letzteAenderung;
	}

	public void setLetzteAenderung(Date letzteAenderung) {
		this.letzteAenderung = letzteAenderung;
	}

	public Date getGebDat() {
		return this.gebDat;
	}

	public void setGebDat(Date gebDat) {
		this.gebDat = gebDat;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Character getNewsletter() {
		return this.newsletter;
	}

	public void setNewsletter(Character newsletter) {
		this.newsletter = newsletter;
	}

	public String getOercMitgliedsnummer() {
		return this.oercMitgliedsnummer;
	}

	public void setOercMitgliedsnummer(String oercMitgliedsnummer) {
		this.oercMitgliedsnummer = oercMitgliedsnummer;
	}

	public String getTelnr() {
		return this.telnr;
	}

	public void setTelnr(String telnr) {
		this.telnr = telnr;
	}

	public String getMobnr() {
		return this.mobnr;
	}

	public void setMobnr(String mobnr) {
		this.mobnr = mobnr;
	}

	public String getEmail2() {
		return this.email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public Character getNewsletter2() {
		return this.newsletter2;
	}

	public void setNewsletter2(Character newsletter2) {
		this.newsletter2 = newsletter2;
	}

	public String getEmail3() {
		return this.email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public Character getNewsletter3() {
		return this.newsletter3;
	}

	public void setNewsletter3(Character newsletter3) {
		this.newsletter3 = newsletter3;
	}

	public Character getGeschlecht() {
		return this.geschlecht;
	}

	public void setGeschlecht(Character geschlecht) {
		this.geschlecht = geschlecht;
	}

	public String getZusatztext() {
		return this.zusatztext;
	}

	public void setZusatztext(String zusatztext) {
		this.zusatztext = zusatztext;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
