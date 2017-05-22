package com.app.showData;

public class ShowKlasseEnde extends ShowKlasse {
	
	private ShowKlasse klasseEndeFor;
	
	public ShowKlasseEnde (ShowKlasse klasseEndeFor) {
		this.klasseEndeFor = klasseEndeFor;
	}
	
	public ShowKlasseEnde() {
		
	}
	
	@Override
	public String getKatalogNummer() {
		return "Klassenende " + klasseEndeFor.getKlasse().getShowKlasseLangBezeichnung();
	}

	public String getKatalognumer() {
		return "notDefined";
	}
	 public String getClubsieger() {
	    	return "";
	    }
	
	public ShowKlasse getKlasseEndeFor() {
		return klasseEndeFor;
	}

	public void setKlasseEndeFor(ShowKlasse klasseEndeFor) {
		this.klasseEndeFor = klasseEndeFor;
	}

	
	
}
