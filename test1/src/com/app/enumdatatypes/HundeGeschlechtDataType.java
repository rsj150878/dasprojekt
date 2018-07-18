package com.app.enumdatatypes;

import java.util.Arrays;

public enum HundeGeschlechtDataType {

	RUEDE("R", "Rüde"), HÜNDIN("H", "Hündin");

	private String kurzBezeichnung;
	private String langBezeichnung;

	private HundeGeschlechtDataType(String kurzBezeichnung, String langBezeichnung) {
		this.kurzBezeichnung = kurzBezeichnung;
		this.langBezeichnung = langBezeichnung;
	}

	public String getKurzBezeichnung() {
		return kurzBezeichnung;
	}

	public String getLangBezeichnung() {
		return langBezeichnung;
	}

	public static HundeGeschlechtDataType getDataTypeKurzBezeichnung(String kurzbezeichnung) {

		return Arrays.stream(HundeGeschlechtDataType.values())
				.filter(e -> e.getKurzBezeichnung().equals(kurzbezeichnung)).findFirst().orElse(null);
	}
}
