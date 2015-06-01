package com.app.enumPackage;


public enum VeranstaltungsStufen {

	STUFE_BH(1, "BH"), 
	STUFE_BGH1(2, "BGH1",1,2,3,4,5), 
	STUFE_BGH2(3, "BGH2",1,2,3,4,5), 
	STUFE_BGH3(4, "BGH3",1,2,3,4,5);

	private Integer veranstaltungsStufeId;
	private String bezeichnung;
	private Integer[] bewertung;

	private VeranstaltungsStufen(Integer id, String bezeichnung,
			Integer... bewertung) {
		this.veranstaltungsStufeId = id;
		this.bezeichnung = bezeichnung;
		this.bewertung = bewertung;

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

}
