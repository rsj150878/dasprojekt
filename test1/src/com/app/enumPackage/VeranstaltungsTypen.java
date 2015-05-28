package com.app.enumPackage;

public enum VeranstaltungsTypen {

	BH_BGH_PRÜFUNG(1, "BH-BGH-Prüfung", 1, 2, 3, 4);

	private Integer veranstaltungsTypID;
	private String veranstaltungsTypBezeichnung;
	private Integer[] veranstaltungsStufen;

	private VeranstaltungsTypen(Integer veranstaltungsTypId,
			String veranstaltungsTypBezeichnung, Integer... veranstaltungsStufen) {
		this.veranstaltungsTypID = veranstaltungsTypId;
		this.veranstaltungsTypBezeichnung = veranstaltungsTypBezeichnung;
		this.veranstaltungsStufen = veranstaltungsStufen;

	}

	public Integer getVeranstaltungsTypID() {
		return veranstaltungsTypID;
	}

	public void setVeranstaltungsTypID(Integer veranstaltungsTypID) {
		this.veranstaltungsTypID = veranstaltungsTypID;
	}

	public String getVeranstaltungsTypBezeichnung() {
		return veranstaltungsTypBezeichnung;
	}

	public void setVeranstaltungsTypBezeichnung(
			String veranstaltungsTypBezeichnung) {
		this.veranstaltungsTypBezeichnung = veranstaltungsTypBezeichnung;
	}

	public Integer[] getVeranstaltungsStufen() {
		return veranstaltungsStufen;
	}

	public void setVeranstaltungsStufen(Integer[] veranstaltungsStufen) {
		this.veranstaltungsStufen = veranstaltungsStufen;
	}

}
