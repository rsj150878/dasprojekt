package com.app.enumPackage;

public enum ShowKlassen {
	BABYKLASSE("BK", "Babyklasse"),
	JUENGSTENKLASSE("JÜ","Jüngstenklasse"),
	JUGENDKLASSE("JU","Jugendklasse"),
	ZWISCHENKLASSE("ZK","Zwischenklasse"),
	OFFENEKLASSE("OF","Offene Klasse"),
	GEBRAUCHSHUNDEKLASSE("GB","Gebrauchshundeklasse"),
	CHAMPIONKLASSE("CH","Championklasse"),
	VETERANENKLASSE("VK","Veteranenklasse");
	
	private final String showKlasseKurzBezeichnung;
	private final String showKlasseLangBezeichnung;
	 
	private ShowKlassen(String kurzBezeichnung, String langBezeichnung) {
		this.showKlasseKurzBezeichnung = kurzBezeichnung;
		this.showKlasseLangBezeichnung = langBezeichnung;
	}
	
	public String getShowKlasseLangBezeichnung() {
		return this.showKlasseLangBezeichnung;
	}
	
	public String getRassenKurzBezeichnung() {
		return this.showKlasseKurzBezeichnung;
	}
	
	public static String getLangBezeichnungFuerKurzBezeichnung(String kurzbezeichnung) {
		
		for (ShowKlassen o: ShowKlassen.values()) {
			if (o.showKlasseKurzBezeichnung.equals(kurzbezeichnung)) {
				return o.showKlasseLangBezeichnung;
			}
			
		}
		return "";
	}
	
	public static ShowKlassen getKlasseForKurzBezeichnung(String kurzbezeichnung) {
		
		for (ShowKlassen o: ShowKlassen.values()) {
			if (o.showKlasseKurzBezeichnung.equals(kurzbezeichnung)) {
				return o;
			}
			
		}
		return null;
	}

	

}
