package com.app.enumPackage;

public enum Rassen {
	GOLDEN_RETRIEVER("GR","Golden Retriever", "Golden Retriever"),
	LABRADOR_RETRIEVER("LR","Labrador Retriever", "Labrador Retriever"),
	FLAT_COATED_RETRIEVER("FCR","Flat Coated Retriever", "FLat Coated Retriever"),
	NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER("DTR","Nova Scotia Duck Tolling Retriever", "Nova Scotia Duck Tolling Retriever"),
	CHESAPEAKE_BAY_RETRIEVER("CBR","Chesapeake Bay Retriever","Chesapeake Bay Retriever"),
	CURLY_COATED_RETRIEVER("CCR","Curly Coated Retriever", "Curly Coated Retriever"),
	BORDER_COLLIE_TRICOLOR_LANGHAAR("BCTL", "Border Collie, Tricolor, Langhaar", "Border Collie, Tricolor, Langhaar"),
	MISCHLING("MI","Mischling",""),
	BERGER_BLANC_SUISSE ("BBS", "Weisser Schäfer", "Berger Blance Suisse"),
	BORDER_COLLIE ("BC", "Border Collie", "Border Collie"),
	COLLIE_LANGHAAR("CLH", "Langhaar Collie", "Langhaar Collie"),
	ALLGEMEIN("ALG", "Allgemein", "Allgemein"), 
	AUSTRALIAN_SHEPHERD ("ASH", "Australian Sheperd", "Australian Sheperd"),
	PERRO_DE_AGUA_ESPANOL("PAE", "Perro de Agua Espanol", "Perro de Agua Espanol"), 
	LAGOTTO_ROMAGNOLO ("LGR", "Lagotto Romagnolo", "Lagotto Romagnolo");
	
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
	
	public static Rassen getRasseForKurzBezeichnung(String kurzbezeichnung) {
		
		for (Rassen o: Rassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o;
			}
			
		}
		return null;
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
