package com.app.enumPackage;

public enum DokumentGehoertZuType {

	VERANSTALTUNG(1),VERANSTALTUNGSSTUFE(2),HUND(3),PERSON(4);
	private Integer gehoertZu;
	
	private DokumentGehoertZuType(Integer gehoertZu) {
		this.gehoertZu = gehoertZu;
	}
	
	public Integer getGehoertZu() {
		return gehoertZu;
	}

}
