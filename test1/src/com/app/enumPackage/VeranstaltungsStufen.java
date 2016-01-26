package com.app.enumPackage;


public enum VeranstaltungsStufen {

	STUFE_BH(1, "BH", "Begleithundeprüfung"), 
	STUFE_BGH1(2, "BGH1", "Begleithundeprüfung 1",1,2,3,4,5), 
	STUFE_BGH2(3, "BGH2","Begleithundeprüfung 2",1,2,3,4,5), 
	STUFE_BGH3(4, "BGH3","Begleithundeprüfung 3", 1,2,3,4,5),
	STUFE_RBP1(5,"RBP1","Retrieverbasisprüfung 1",6,7,8,9,10),
	STUFE_RBP2(6,"RBP2","Retrieverbasisprüfung 2",6,7,8,9,10),
	STUFE_RBP3(7,"RBP3","Retrieverbasisprüfung 3",6,7,8,9,10),
	STUFE_RBP4_O_WASSER(8,"RBP4","Retrieverbasisprüfung 4",6,7,8,9,10),
	STUFE_RBP4_M_WASSER(9,"RBP4","Retrieverbasisprüfung 4 (mit Wasser)",6,7,8,9,10),
	STUFE_GAP1(10,"GAP1","Gehorsam- und Apportierprüfung 1",11,12,13,14,15),
	STUFE_GAP2(11,"GAP2","Gehorsam- und Apportierprüfung 2",11,12,13,14,15),
	STUFE_GAP3(12,"GAP3","Gehorsam- und Apportierprüfung 3", 11,12,13,14,15);
	
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
