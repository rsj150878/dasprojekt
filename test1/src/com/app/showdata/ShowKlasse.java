package com.app.showdata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.app.enumdatatypes.Rassen;
import com.app.enumdatatypes.ShowKlassen;

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
	
	
	public int getAnzahl() {
		return hundeDerKlasse.size();
	}

	public String getKatalogNummer() {
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
	
	 public String getClubsieger() {
	    	return "";
	    }
 }
