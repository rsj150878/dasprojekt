package com.app.enumdatatypes;

public enum ShowKlassen {
	BABYKLASSE("WE", "Babyklasse", "J", "J"), 
	JUENGSTENKLASSE("JÜ", "Jüngstenklasse", "J", "J"), 
	JUGENDKLASSE("JU","Jugendklasse", "J", "J"), 
	ZWISCHENKLASSE("ZK", "Zwischenklasse", "J", "J"), 
	OFFENEKLASSE("OF", "Offene Klasse","J", "J"), 
	GEBRAUCHSHUNDEKLASSE("GB", "Gebrauchshundeklasse", "J", "J"), 
	CHAMPIONKLASSE("CH","Championklasse","J", "J"), 
	VETERANENKLASSE("VE", "Veteranenklasse", "J", "J"), 
	WESENSTEST("WT", "Wesenstest", "N", "N");

	private final String showKlasseKurzBezeichnung;
	private final String showKlasseLangBezeichnung;
	private final String platzierungWirdBerechnet;
	private final String printBewertungSummenBlatt;

	private ShowKlassen(String kurzBezeichnung, String langBezeichnung, String platzierungWirdBerechnet, String printBewertungSummenBlatt) {
		this.showKlasseKurzBezeichnung = kurzBezeichnung;
		this.showKlasseLangBezeichnung = langBezeichnung;
		this.platzierungWirdBerechnet = platzierungWirdBerechnet;
		this.printBewertungSummenBlatt = printBewertungSummenBlatt;
	}

	public String getShowKlasseLangBezeichnung() {
		return this.showKlasseLangBezeichnung;
	}

	public String getShowKlassenKurzBezeichnung() {
		return this.showKlasseKurzBezeichnung;
	}

	public static String getLangBezeichnungFuerKurzBezeichnung(String kurzbezeichnung) {

		for (ShowKlassen o : ShowKlassen.values()) {
			if (o.showKlasseKurzBezeichnung.equals(kurzbezeichnung)) {
				return o.showKlasseLangBezeichnung;
			}

		}
		return "";
	}

	public static ShowKlassen getKlasseForKurzBezeichnung(String kurzbezeichnung) {

		for (ShowKlassen o : ShowKlassen.values()) {
			if (o.showKlasseKurzBezeichnung.equals(kurzbezeichnung)) {
				return o;
			}

		}
		return null;
	}

	public static boolean platzWirdFuerKlasseBerechnet(String kurzbezeichnung) {
		for (ShowKlassen o : ShowKlassen.values()) {
			if (o.showKlasseKurzBezeichnung.equals(kurzbezeichnung)) {
				if (o.platzierungWirdBerechnet.equals("J"))
					return true;
				else
					return false;
			}

		}
		return false;

	}

}
