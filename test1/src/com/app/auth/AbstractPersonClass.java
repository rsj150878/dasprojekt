package com.app.auth;

import java.util.Date;

public class AbstractPersonClass {
	protected String role;
	protected Integer idUser;
	protected Integer idPerson;
	protected String firstName;
	protected String lastName;
	protected String title;
	protected String male;
	protected Date gebdat;
	protected String email;
	protected String strasse;
	protected String hausnummer;
	protected String plz;
	protected String ort;
	protected String land;
	protected String phone;
	protected String mobnr;
	protected String newsletter;
	protected String website;
	protected String bio;

	protected String email2;
	protected String newsletter2;
	protected String email3;
	protected String newsletter3;
	
	
	public void commit() throws Exception {
		
	};
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(Integer idPerson) {
		this.idPerson = idPerson;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMale() {
		return male;
	}

	public void setMale(String male) {
		this.male = male;
	}

	public Date getGebdat() {
		return gebdat;
	}

	public void setGebdat(Date gebdat) {
		this.gebdat = gebdat;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobnr() {
		return mobnr;
	}

	public void setMobnr(String mobnr) {
		this.mobnr = mobnr;
	}

	public String getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(String newsletter) {
		this.newsletter = newsletter;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getNewsletter2() {
		return newsletter2;
	}

	public void setNewsletter2(String newsletter2) {
		this.newsletter2 = newsletter2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getNewsletter3() {
		return newsletter3;
	}

	public void setNewsletter3(String newsletter3) {
		this.newsletter3 = newsletter3;
	}


}
