package com.app.enumPackage;

public enum JaNeinDataType {
	
	JA("J","Ja"),
	NEIN("N","NEIN");

	private String kurzText;
	private String langText;
	
	private JaNeinDataType(String kurzText, String langText) {
		this.kurzText = kurzText;
		this.langText = langText;
	}

	public String getKurzText() {
		return kurzText;
	}

	public String getLangText() {
		return langText;
	}
	
	public static JaNeinDataType getJaNeinDataTypeForKurzbezeichnung(String kurzbezeichnung) {
		return kurzbezeichnung.equals(JaNeinDataType.JA.kurzText) ? JaNeinDataType.JA:JaNeinDataType.NEIN;
	}
	
	
}
