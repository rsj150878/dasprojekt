package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

/**
 * emaillist generated by hbm2java
 */
public class emaillist implements java.io.Serializable {

	private int id;
	private int version;
	private String emailadresse;
	private char newsletter;

	public emaillist() {
	}

	public emaillist(String emailadresse, char newsletter) {
		this.emailadresse = emailadresse;
		this.newsletter = newsletter;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getEmailadresse() {
		return this.emailadresse;
	}

	public void setEmailadresse(String emailadresse) {
		this.emailadresse = emailadresse;
	}

	public char getNewsletter() {
		return this.newsletter;
	}

	public void setNewsletter(char newsletter) {
		this.newsletter = newsletter;
	}

}
