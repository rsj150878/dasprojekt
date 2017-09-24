package com.app.enumPackage;

public enum BobDataType {
	
	BOB("B", "BOB"),
	BOS("O", "BOS");
	
	private String dataBaseValue;
	private String langText;
	
	private BobDataType(String dataBaseValue, String langText) {
		this.dataBaseValue = dataBaseValue;
		this.langText = langText;
	}
	
	public static BobDataType getTextForDataBaseValue(String dataBaseValue) {

		for (BobDataType o : BobDataType.values()) {
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
