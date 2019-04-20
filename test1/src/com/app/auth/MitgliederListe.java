package com.app.auth;

import java.util.Collection;
import java.util.Iterator;

public class MitgliederListe {

	private String familienName;
	private String vorName;
	private String adresse;
	private Person person;
	private Collection<Hund> hunde;
	private String hundeNamen;
	// private String edit;
	// private String hundeforbutton;

	public String getFamilienName() {
		return person.getLastName();
	}

	public void setFamilienName(String familienName) {
		this.familienName = familienName;
	}

	public String getVorName() {
		return person.getFirstName();
	}

	public void setVorName(String vorName) {
		this.vorName = vorName;
	}

	public String getAdresse() {
		return person.getStrasse() + " " + person.getHausnummer() + ", " + person.getPlz() + " " + person.getOrt();
	}

	public String getHundeNamen() {
		return hundeNamen;
	}

	public void setHundeNamen(String hundeNamen) {
		this.hundeNamen = hundeNamen;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
		try {
			setHunde(person.getAllHunde());

		} catch (Exception e) {

		}

		setFamilienName(person.getLastName());
		setVorName(person.getFirstName());
		setAdresse(person.getStrasse() + " " + person.getHausnummer() + ", " + person.getPlz() + " " + person.getOrt());

	}

	public Collection<Hund> getHunde() {
		return hunde;
	}

	public String getSearchString() {
		return person.getLastName() + " " + person.getFirstName() + " " + hundeNamen;
	}

	public void setHunde(Collection<Hund> hunde) {
		this.hunde = hunde;
		Iterator<Hund> i = this.hunde.iterator();

		while (i.hasNext()) {
			Hund o = (Hund) i.next();
			if (hundeNamen == null || "".equals(hundeNamen) || hundeNamen.isEmpty()) {
				hundeNamen = o.getRufname() + "(" + o.getZwingername() + ")";
			} else {
				hundeNamen = hundeNamen + "," + o.getRufname() + "(" + o.getZwingername() + ")";
			}

		}

	}

}
