package com.app.showData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.app.enumPackage.Rassen;

public class ShowRing {
	
	protected String name;
	
	private String ringNummer;
	private String richter;
	private Integer ringId;
	
	
	private final List <ShowRing> klassenDesRings;

	
	public ShowRing() {
		
		this.klassenDesRings = new ArrayList<ShowRing>();
	}
	 
	public void addShowKlasse(ShowKlasse klasse) {
		klassenDesRings.add(klasse);
	}

	public void addShowKlassen(List <ShowKlasse> klasse) {
		klassenDesRings.addAll(klasse);
	}
	
	public String getRingNummer() {
		return ringNummer;
		
	}
	
	public String getTextForAuswahl() {
		return this.name + " - " + this.richter;
	}

	public void setRingNummer(String ringNummer) {
		this.ringNummer = ringNummer;
		this.name = "Ring " + ringNummer;
	}

	public String getKatalogNummer() {
		return name;
	}
	
	public String getHundeName() {
		return "";
	}

	public String getFormwertUndTitel() {
		return "";
	}
	
    public Stream<ShowRing> getKlassenAsStream() {
    	return klassenDesRings.stream();
    }
    
    public Stream<ShowRing> flattened() {
    	return Stream.concat(Stream.of(this),
                getKlassenAsStream().flatMap(ShowRing::flattened));
    }
    
    public String getRichter()   {
    	return this.richter;
    }
    
    public void setRichter(String richter) {
    	this.richter = richter;
    }
    
    public List<ShowRing> getChilds() {
    	return this.klassenDesRings;
    }
    
    public String getPlatzierung() {
    	return "";
    }
    public String getKatalognumer() {
    	return "";
    }
    
    
    public String getClubsieger() {
    	return "";
    }
    
    public String getGeschlecht() {
    	return "";
    }
    
    public Rassen getRasse() {
    	return null;
    }
    
    public String getBOB() {
    	return "";
    }

	public Integer getRingId() {
		return ringId;
	}

	public void setRingId(Integer ringId) {
		this.ringId = ringId;
	}
	
	public String getCACIB() {
		return "";
	}

}
