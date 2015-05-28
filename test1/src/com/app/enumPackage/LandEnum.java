package com.app.enumPackage;

public enum LandEnum {
	
	OESTERREICH("AT","Österreich"),
	DEUTSCHLAND("DE","Deutschland"),
	SCHWEIZ("CH","Schweiz");
	
	private final String landKurzBezeichnung;
	private final String landLangBezeichnung;
	
	private LandEnum(String landKurzBezeichnung, String landLangBezeichnung) {
		this.landKurzBezeichnung = landKurzBezeichnung;
		this.landLangBezeichnung = landLangBezeichnung;
	}

	public String getLandKurzBezeichnung() {
		return landKurzBezeichnung;
	}

	public String getLandLangBezeichnung() {
		return landLangBezeichnung;
	}

}
