package com.app.enumPackage;

public enum CacibDataType {
	
	CACIB("C", "CACIB"),
	RES_CACA("R", "Res. CACIB");
	
	private String dataBaseValue;
	private String langText;
	
	private CacibDataType(String dataBaseValue, String langText) {
		this.dataBaseValue = dataBaseValue;
		this.langText = langText;
	}
	
	public static CacibDataType getTextForDataBaseValue(String dataBaseValue) {

		for (CacibDataType o : CacibDataType.values()) {
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
