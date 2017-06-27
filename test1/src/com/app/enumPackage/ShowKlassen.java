package com.app.enumPackage;

public enum ShowKlassen {
	BABYKLASSE("WE", "Babyklasse", "J"), JUENGSTENKLASSE("JÜ",
			"Jüngstenklasse", "J"), JUGENDKLASSE("JU", "Jugendklasse", "J"), ZWISCHENKLASSE(
			"ZK", "Zwischenklasse", "J"), OFFENEKLASSE("OF", "Offene Klasse",
			"J"), GEBRAUCHSHUNDEKLASSE("GB", "Gebrauchshundeklasse", "J"), CHAMPIONKLASSE(
			"CH", "Championklasse", "J"), VETERANENKLASSE("VE",
			"Veteranenklasse", "J"),
	WESENSTEST("WT","Wesenstest","N");

	private final String showKlasseKurzBezeichnung;
	private final String showKlasseLangBezeichnung;
	private final String platzierungWirdBerechnet;

	private ShowKlassen(String kurzBezeichnung, String langBezeichnung,
			String platzierungWirdBerechnet) {
		this.showKlasseKurzBezeichnung = kurzBezeichnung;
		this.showKlasseLangBezeichnung = langBezeichnung;
		this.platzierungWirdBerechnet = platzierungWirdBerechnet;
	}

	public String getShowKlasseLangBezeichnung() {
		return this.showKlasseLangBezeichnung;
	}

	public String getShowKlassenKurzBezeichnung() {
		return this.showKlasseKurzBezeichnung;
	}

	public static String getLangBezeichnungFuerKurzBezeichnung(
			String kurzbezeichnung) {

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
