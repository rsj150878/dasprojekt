package com.app.enumdatatypes;

public enum FormWertErwachsen {
	
	VORZUEGLICH ("v", "vorzüglich"),
	SEHR_GUT("sg", "sehr gut"),
	GUT("g", "gut"),
	GENUEGEND("ge", "genügend"),
	DISQUALIFIZIERT("d", "disqualifiziert"),
	OHNE_BEWERTUNG("ob", "ohne Bewertung");
	
	private String formwert;
	private String formWertBezeichnung;
	
	private FormWertErwachsen(String formwert, String formWertBezeichnung) {
		this.formwert = formwert;
		this.formWertBezeichnung = formWertBezeichnung;
	}
	
	public static FormWertErwachsen getFormwertForKurzBezeichnung(String kurzbezeichnung) {

		for (FormWertErwachsen o : FormWertErwachsen.values()) {
			if (o.formwert.equals(kurzbezeichnung)) {
				return o;
			}

		}
		return null;
	}

	public String getFormwert() {
		return formwert;
	}

	public String getFormWertBezeichnung() {
		return formWertBezeichnung;
	}

	

}
