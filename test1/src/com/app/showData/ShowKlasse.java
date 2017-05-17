package com.app.showData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.app.enumPackage.Rassen;
import com.app.enumPackage.ShowKlassen;

public class ShowKlasse extends ShowRing  {
	
	private Rassen rasse;
	private ShowKlassen klasse;
	private final List <ShowRing> hundeDerKlasse;
	
	public ShowKlasse (Rassen rasse, ShowKlassen klasse)  {
		//super();
		this.rasse = rasse;
		this.klasse = klasse;
		hundeDerKlasse = new ArrayList<ShowRing>();
	}
	
	public ShowKlasse() {
		//super();

		this.rasse =null;
		this.klasse = null;
		hundeDerKlasse = new ArrayList<ShowRing>();
	
		
	}

	public String getKatalogNummer() {
		System.out.println("getkatlognummer klasse");
		return rasse.getRassenLangBezeichnung() + " " + klasse.getShowKlasseLangBezeichnung();
	}
	
	public void setHundeDerKlasse (List <ShowRing> hundeDerKlasse) {
		this.hundeDerKlasse.addAll(hundeDerKlasse);
	}
	
	public void addHundZuKlasse(ShowHund hund) {
		this.hundeDerKlasse.add(hund);
	} 
	
	public Stream<ShowRing> getKlassenAsStream() {
		return hundeDerKlasse.stream();
    }
    
	public Stream<ShowRing> flattened() {
        return Stream.concat(Stream.of(this),
                getKlassenAsStream().flatMap(ShowRing::flattened));
    }

	public Rassen getRasse() {
		return rasse;
	}

	public void setRasse(Rassen rasse) {
		this.rasse = rasse;
	}

	public ShowKlassen getKlasse() {
		return klasse;
	}

	public void setKlasse(ShowKlassen klasse) {
		this.klasse = klasse;
	}
	
	public List <ShowRing> getChilds() {
		return this.hundeDerKlasse;
	}
 }
