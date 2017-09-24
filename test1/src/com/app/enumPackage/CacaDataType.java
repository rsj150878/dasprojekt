package com.app.enumPackage;

public enum CacaDataType {
	
	CACA("C", "CACA"),
	RES_CACA("R", "Res. CACA"),
	JUGENDBESTER("J", "Jugendbester"),
	VETERANENSIEGER("V","Veteranensieger"),
	RES_VETERANENSIEGER("W", "Res. Veteranensieger"),
	KEIN_TITEL("K", "Kein Titel");
	
	private String dataBaseValue;
	private String langText;
	
	private CacaDataType(String dataBaseValue, String langText) {
		this.dataBaseValue = dataBaseValue;
		this.langText = langText;
	}
	
	public static CacaDataType getTextForDataBaseValue(String dataBaseValue) {

		for (CacaDataType o : CacaDataType.values()) {
			if (o.dataBaseValue.equals(dataBaseValue)) {
				return o;
			}

		}
		return null;
	}

	public String getDataBaseValue() {
		return dataBaseValue;
	}

	public String getLangText() {
		return langText;
	}

	
}
