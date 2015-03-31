package com.app.printClasses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Kursteilnemer")
public class Kursteilnehmer {
	
	private Person hundeBesitzer;
	private Person kursTeilnehmer;
	private Hund hund;
	private Kursart kursart;
	
	public void Kursteilnehmer() {
		
	}
	
	public void Kursteilnehmer(Person hundeBesitzer, Person kursTeilnehmer, Hund hund, Kursart kursart) {
		this.hundeBesitzer = hundeBesitzer;
		this.kursTeilnehmer = kursTeilnehmer;
		this.hund = hund;
		this.kursart = kursart;
		
	}

	public Person getHundeBesitzer() {
		return hundeBesitzer;
	}

	public void setHundeBesitzer(Person hundeBesitzer) {
		this.hundeBesitzer = hundeBesitzer;
	}

	public Person getKursTeilnehmer() {
		return kursTeilnehmer;
	}

	public void setKursTeilnehmer(Person kursTeilnehmer) {
		this.kursTeilnehmer = kursTeilnehmer;
	}

	public Hund getHund() {
		return hund;
	}

	public void setHund(Hund hund) {
		this.hund = hund;
	}

	public Kursart getKursart() {
		return kursart;
	}

	public void setKursart(Kursart kursart) {
		this.kursart = kursart;
	}

}
