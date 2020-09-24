package com.app.enumdatatypes;

import java.util.ArrayList;
import java.util.List;

public enum ShowKlassen {
	BABYKLASSE("WE", "Babyklasse", "J", "J",false,false), 
	JUENGSTENKLASSE("JÜ", "Jüngstenklasse", "J", "J",false,false), 
	JUGENDKLASSE("JU","Jugendklasse", "J", "J",false,true), 
	ZWISCHENKLASSE("ZK", "Zwischenklasse", "J", "J",true,true), 
	OFFENEKLASSE("OF", "Offene Klasse","J", "J",true,true), 
	GEBRAUCHSHUNDEKLASSE("GB", "Gebrauchshundeklasse", "J", "J",true,true), 
	CHAMPIONKLASSE("CH","Championklasse","J", "J",true,true), 
	VETERANENKLASSE("VE", "Veteranenklasse", "J", "J",false,true), 
	WESENSTEST("WT", "Wesenstest", "N", "N",false,false);

	private final String showKlasseKurzBezeichnung;
	private final String showKlasseLangBezeichnung;
	private final String platzierungWirdBerechnet;
	private final String printBewertungSummenBlatt;
	private final Boolean istErwachsenenKlasse;
	private final Boolean istBestDogKlasse;

	public String getShowKlasseKurzBezeichnung() {
		return showKlasseKurzBezeichnung;
	}

	public String getPlatzierungWirdBerechnet() {
		return platzierungWirdBerechnet;
	}

	public String getPrintBewertungSummenBlatt() {
		return printBewertungSummenBlatt;
	}

	public Boolean getIstErwachsenenKlasse() {
		return istErwachsenenKlasse;
	}

	private ShowKlassen(String kurzBezeichnung, String langBezeichnung, String platzierungWirdBerechnet, String printBewertungSummenBlatt,
			Boolean istErwachsenenKlasse, Boolean istBestDogKlasse
			) {
		this.showKlasseKurzBezeichnung = kurzBezeichnung;
		this.showKlasseLangBezeichnung = langBezeichnung;
		this.platzierungWirdBerechnet = platzierungWirdBerechnet;
		this.printBewertungSummenBlatt = printBewertungSummenBlatt;
		this.istErwachsenenKlasse = istErwachsenenKlasse;
		this.istBestDogKlasse = istBestDogKlasse;
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
	
	public static List<ShowKlassen> getErwachsenenKlassen() {
		List<ShowKlassen> result = new ArrayList<>();

		for (ShowKlassen o : ShowKlassen.values()) {
			if (o.istErwachsenenKlasse) {
				result.add(o);
			}
			
		}
		
		return result;
		
	}
	
	public static List<ShowKlassen> getBestDogKlassen() {
		List<ShowKlassen> result = new ArrayList<>();

		for (ShowKlassen o : ShowKlassen.values()) {
			if (o.istBestDogKlasse) {
				result.add(o);
			}
			
		}
		
		return result;
		
	}

}
