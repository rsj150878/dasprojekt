package com.app.printClasses;

import java.util.Date;

public class Person {

	private String name;
	private String adresse;
	private String emailAdresse;
	private String mobNR;
	private String telNR;
	private Integer oercMitgliedsNummer;
	private Date geburtsDatum;
	
	public void Person() {
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getEmailAdresse() {
		return emailAdresse;
	}
	public void setEmailAdresse(String emailAdresse) {
		this.emailAdresse = emailAdresse;
	}
	public String getMobNR() {
		return mobNR;
	}
	public void setMobNR(String mobNR) {
		this.mobNR = mobNR;
	}
	public String getTelNR() {
		return telNR;
	}
	public void setTelNR(String telNR) {
		this.telNR = telNR;
	}
	public Integer getOercMitgliedsNummer() {
		return oercMitgliedsNummer;
	}
	public void setOercMitgliedsNummer(Integer oercMitgliedsNummer) {
		this.oercMitgliedsNummer = oercMitgliedsNummer;
	}
	public Date getGeburtsDatum() {
		return geburtsDatum;
	}
	public void setGeburtsDatum(Date geburtsDatum) {
		this.geburtsDatum = geburtsDatum;
	}
	
}
