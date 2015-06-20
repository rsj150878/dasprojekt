package com.app.enumPackage;


public enum VeranstaltungsStufen {

	STUFE_BH(1, "BH", "Begleithundeprüfung"), 
	STUFE_BGH1(2, "BGH1", "Begleithundeprüfung 1",1,2,3,4,5), 
	STUFE_BGH2(3, "BGH2","Begleithundeprüfung 2",1,2,3,4,5), 
	STUFE_BGH3(4, "BGH3","Begleithundeprüfung 3", 1,2,3,4,5);

	private Integer veranstaltungsStufeId;
	private String bezeichnung;
	private String langBezeichnung;
	private Integer[] bewertung;

	private VeranstaltungsStufen(Integer id, String bezeichnung, String langBezeichnung,
			Integer... bewertung) {
		this.veranstaltungsStufeId = id;
		this.bezeichnung = bezeichnung;
		this.bewertung = bewertung;
		this.langBezeichnung = langBezeichnung;

	}

	public Integer getVeranstaltungsStufeId() {
		return veranstaltungsStufeId;
	}

	public void setVeranstaltungsStufeId(Integer veranstaltungsStufeId) {
		this.veranstaltungsStufeId = veranstaltungsStufeId;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public static VeranstaltungsStufen getBezeichnungForId(Integer id) {
		for (VeranstaltungsStufen x : VeranstaltungsStufen.values()) {
			if (id.equals(x.veranstaltungsStufeId)) {
				return x;
			}

		}

		return null;
	}
	
	public String getBewertung(Integer punkte) {
		for(Integer x: bewertung) {
			BewertungEnum bwEnum = BewertungEnum.getBewertung(x);
			if (bwEnum.von <= punkte && bwEnum.bis >= punkte) {
				return bwEnum.bewertung;
			}
		}
		return "";
	}

	public String getLangBezeichnung() {
		return langBezeichnung;
	}

	public void setLangBezeichnung(String langBezeichnung) {
		this.langBezeichnung = langBezeichnung;
	}

}
