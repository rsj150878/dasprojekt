package com.app.enumPackage;

public enum Rassen {
	GOLDEN_RETRIEVER("GR","Golden Retriever"),
	LABRADOR_RETRIEVER("LR","Labrador Retriever"),
	FLAT_COATED_RETRIEVER("FCR","Flat Coated Retriever"),
	NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER("DTR","Nova Scotia Duck Tolling Retriever"),
	CHESAPEAKE_BAY_RETRIEVER("CBR","Chesapeake Bay Retriever"),
	CURLY_COATED_RETRIEVER("CCR","Curly Coated Retriever");
	
	private final String RassenLangBezeichnung;
	private final String RassenKurzBezeichnung;
	
	private Rassen(String kurzBezeichnung, String langBezeichnung) {
		this.RassenLangBezeichnung = langBezeichnung;
		this.RassenKurzBezeichnung = kurzBezeichnung;
	}
	
	public String getRassenLangBezeichnung() {
		return this.RassenLangBezeichnung;
	}
	
	public String getRassenKurzBezeichnung() {
		return this.RassenKurzBezeichnung;
	}

}
