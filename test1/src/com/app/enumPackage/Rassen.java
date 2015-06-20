package com.app.enumPackage;

public enum Rassen {
	GOLDEN_RETRIEVER("GR","Golden Retriever", "Golden Retriever"),
	LABRADOR_RETRIEVER("LR","Labrador Retriever", "Labrador Retriever"),
	FLAT_COATED_RETRIEVER("FCR","Flat Coated Retriever", "FLat Coated Retriever"),
	NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER("DTR","Nova Scotia Duck Tolling Retriever", "Nova Scotia Duck Tolling Retriever"),
	CHESAPEAKE_BAY_RETRIEVER("CBR","Chesapeake Bay Retriever","Chesapeake Bay Retriever"),
	CURLY_COATED_RETRIEVER("CCR","Curly Coated Retriever", "Curly Coated Retriever"),
	MISCHLING("MI","Mischling","");
	
	private final String RassenLangBezeichnung;
	private final String RassenKurzBezeichnung;
	private final String UrkundenBezeichnung;
	
	private Rassen(String kurzBezeichnung, String langBezeichnung, String urkundenBezeichnung) {
		this.RassenLangBezeichnung = langBezeichnung;
		this.RassenKurzBezeichnung = kurzBezeichnung;
		this.UrkundenBezeichnung = urkundenBezeichnung;
	}
	
	public String getRassenLangBezeichnung() {
		return this.RassenLangBezeichnung;
	}
	
	public String getRassenKurzBezeichnung() {
		return this.RassenKurzBezeichnung;
	}
	
	public static String getLangBezeichnungFuerKurzBezeichnung(String kurzbezeichnung) {
		
		for (Rassen o: Rassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o.RassenLangBezeichnung;
			}
			
		}
		return "";
	}

	public static String getUrkundenBezeichnungFuerKurzBezeichnung(String kurzbezeichnung) {
		
		for (Rassen o: Rassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o.UrkundenBezeichnung;
			}
			
		}
		return "";
	}
	

}
