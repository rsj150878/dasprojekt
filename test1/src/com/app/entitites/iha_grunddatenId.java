package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

/**
 * iha_grunddatenId generated by hbm2java
 */
public class iha_grunddatenId implements java.io.Serializable {

	private int ringnummer;
	private int katalognummer;
	private String klasse;
	private String rasse;
	private char geschlecht;
	private String hundename;
	private String wurfdatum;
	private String zuchtbuchnummer;
	private String vater;
	private String mutter;
	private String züchter;
	private String besitzer;

	public iha_grunddatenId() {
	}

	public iha_grunddatenId(int ringnummer, int katalognummer, String klasse, String rasse, char geschlecht,
			String hundename, String wurfdatum, String zuchtbuchnummer, String vater, String mutter, String züchter,
			String besitzer) {
		this.ringnummer = ringnummer;
		this.katalognummer = katalognummer;
		this.klasse = klasse;
		this.rasse = rasse;
		this.geschlecht = geschlecht;
		this.hundename = hundename;
		this.wurfdatum = wurfdatum;
		this.zuchtbuchnummer = zuchtbuchnummer;
		this.vater = vater;
		this.mutter = mutter;
		this.züchter = züchter;
		this.besitzer = besitzer;
	}

	public int getRingnummer() {
		return this.ringnummer;
	}

	public void setRingnummer(int ringnummer) {
		this.ringnummer = ringnummer;
	}

	public int getKatalognummer() {
		return this.katalognummer;
	}

	public void setKatalognummer(int katalognummer) {
		this.katalognummer = katalognummer;
	}

	public String getKlasse() {
		return this.klasse;
	}

	public void setKlasse(String klasse) {
		this.klasse = klasse;
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

	public String getHundename() {
		return this.hundename;
	}

	public void setHundename(String hundename) {
		this.hundename = hundename;
	}

	public String getWurfdatum() {
		return this.wurfdatum;
	}

	public void setWurfdatum(String wurfdatum) {
		this.wurfdatum = wurfdatum;
	}

	public String getZuchtbuchnummer() {
		return this.zuchtbuchnummer;
	}

	public void setZuchtbuchnummer(String zuchtbuchnummer) {
		this.zuchtbuchnummer = zuchtbuchnummer;
	}

	public String getVater() {
		return this.vater;
	}

	public void setVater(String vater) {
		this.vater = vater;
	}

	public String getMutter() {
		return this.mutter;
	}

	public void setMutter(String mutter) {
		this.mutter = mutter;
	}

	public String getZüchter() {
		return this.züchter;
	}

	public void setZüchter(String züchter) {
		this.züchter = züchter;
	}

	public String getBesitzer() {
		return this.besitzer;
	}

	public void setBesitzer(String besitzer) {
		this.besitzer = besitzer;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof iha_grunddatenId))
			return false;
		iha_grunddatenId castOther = (iha_grunddatenId) other;

		return (this.getRingnummer() == castOther.getRingnummer())
				&& (this.getKatalognummer() == castOther.getKatalognummer())
				&& ((this.getKlasse() == castOther.getKlasse()) || (this.getKlasse() != null
						&& castOther.getKlasse() != null && this.getKlasse().equals(castOther.getKlasse())))
				&& ((this.getRasse() == castOther.getRasse()) || (this.getRasse() != null
						&& castOther.getRasse() != null && this.getRasse().equals(castOther.getRasse())))
				&& (this.getGeschlecht() == castOther.getGeschlecht())
				&& ((this.getHundename() == castOther.getHundename()) || (this.getHundename() != null
						&& castOther.getHundename() != null && this.getHundename().equals(castOther.getHundename())))
				&& ((this.getWurfdatum() == castOther.getWurfdatum()) || (this.getWurfdatum() != null
						&& castOther.getWurfdatum() != null && this.getWurfdatum().equals(castOther.getWurfdatum())))
				&& ((this.getZuchtbuchnummer() == castOther.getZuchtbuchnummer())
						|| (this.getZuchtbuchnummer() != null && castOther.getZuchtbuchnummer() != null
								&& this.getZuchtbuchnummer().equals(castOther.getZuchtbuchnummer())))
				&& ((this.getVater() == castOther.getVater()) || (this.getVater() != null
						&& castOther.getVater() != null && this.getVater().equals(castOther.getVater())))
				&& ((this.getMutter() == castOther.getMutter()) || (this.getMutter() != null
						&& castOther.getMutter() != null && this.getMutter().equals(castOther.getMutter())))
				&& ((this.getZüchter() == castOther.getZüchter()) || (this.getZüchter() != null
						&& castOther.getZüchter() != null && this.getZüchter().equals(castOther.getZüchter())))
				&& ((this.getBesitzer() == castOther.getBesitzer()) || (this.getBesitzer() != null
						&& castOther.getBesitzer() != null && this.getBesitzer().equals(castOther.getBesitzer())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRingnummer();
		result = 37 * result + this.getKatalognummer();
		result = 37 * result + (getKlasse() == null ? 0 : this.getKlasse().hashCode());
		result = 37 * result + (getRasse() == null ? 0 : this.getRasse().hashCode());
		result = 37 * result + this.getGeschlecht();
		result = 37 * result + (getHundename() == null ? 0 : this.getHundename().hashCode());
		result = 37 * result + (getWurfdatum() == null ? 0 : this.getWurfdatum().hashCode());
		result = 37 * result + (getZuchtbuchnummer() == null ? 0 : this.getZuchtbuchnummer().hashCode());
		result = 37 * result + (getVater() == null ? 0 : this.getVater().hashCode());
		result = 37 * result + (getMutter() == null ? 0 : this.getMutter().hashCode());
		result = 37 * result + (getZüchter() == null ? 0 : this.getZüchter().hashCode());
		result = 37 * result + (getBesitzer() == null ? 0 : this.getBesitzer().hashCode());
		return result;
	}

}