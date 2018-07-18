package com.app.enumPackage;

import java.util.Arrays;

public enum LaenderDataType {

	OESTERREICH("AT", "Oesterreich"), DEUTSCHLAND("DE", "Deutschland"), SCHWEIZ("CH","Schweiz");

	private String kurzText;
	private String langText;

	private LaenderDataType(String kurzText, String langText) {
		this.kurzText = kurzText;
		this.langText = langText;
	}

	public String getKurzText() {
		return kurzText;
	}

	public String getLangText() {
		return langText;
	}

	public static LaenderDataType getLaenderDataTypeForKurzbezeichnung(String kurzbezeichnung) {
		return Arrays.stream(LaenderDataType.values())
				.filter(e -> e.getKurzText().equals(kurzbezeichnung)).findFirst().orElse(null);
	}

}
