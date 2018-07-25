package com.app.enumdatatypes;

public enum MenschGeschlechtDataType {

	MANN("M", "Herr"), FRAU("F", "Frau");

	private String kurzText;
	private String langText;

	private MenschGeschlechtDataType(String kurzText, String langText) {
		this.kurzText = kurzText;
		this.langText = langText;
	}

	public String getKurzText() {
		return kurzText;
	}

	public String getLangText() {
		return langText;
	}

	public static MenschGeschlechtDataType getMenschGeschlechtDataTypeForKurzbezeichnung(String kurzbezeichnung) {
		return kurzbezeichnung == null || kurzbezeichnung.equals(MenschGeschlechtDataType.MANN.kurzText) ? MenschGeschlechtDataType.MANN
				: MenschGeschlechtDataType.FRAU;
	}

}
