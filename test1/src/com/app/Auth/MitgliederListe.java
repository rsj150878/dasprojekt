package com.app.Auth;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.client.ui.FontIcon;
import com.vaadin.server.FontAwesome;

public class MitgliederListe {
	
	private String familienName;
	private String vorName;
	private String adresse;
	private Person person;
	private Collection<Hund> hunde;
	private String hundeNamen;
	private FontIcon edit;

	public String getFamilienName() {
		return familienName;
	}
	public void setFamilienName(String familienName) {
		this.familienName = familienName;
	}
	public String getVorName() {
		return vorName;
	}
	public void setVorName(String vorName) {
		this.vorName = vorName;
	}
	public String getAdresse() {
		return adresse;
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
		setHunde(person.getAllHunde());
		
		setFamilienName(person.getLastName());
		setVorName(person.getFirstName());
		setAdresse(person.getStrasse() + " " + person.getHausnummer() + ", " + person.getPlz() + " " + person.getOrt());
							

	}
	public Collection<Hund> getHunde() {
		return hunde;
	}
	public void setHunde(Collection<Hund> hunde) {
		this.hunde = hunde;
		Iterator i = this.hunde.iterator();
		
		while(i.hasNext()) {
			Hund o = (Hund) i.next();
			if (hundeNamen == null || "".equals(hundeNamen) || hundeNamen.isEmpty()) {
				hundeNamen = o.getRufname() + "(" + o.getZwingername() + ")";
			} else {
				hundeNamen = hundeNamen + "," + o.getRufname() + "(" + o.getZwingername() + ")";
			}
			
		}
		
	}
	public FontIcon getEdit() {
		return FontAwesome.PENCIL.getHtml();;
	}
	
	
	

}
