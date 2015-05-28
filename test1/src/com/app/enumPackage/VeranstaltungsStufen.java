package com.app.enumPackage;

public enum VeranstaltungsStufen {

	STUFE_BH(1, "BH"), STUFE_BGH1(2, "BGH1"), STUFE_BGH2(3, "BGH2"), STUFE_BGH3(
			4, "BGH3");
	
	private Integer veranstaltungsStufeId;
	private String bezeichnung;

	private VeranstaltungsStufen(Integer id, String bezeichnung) {
		this.veranstaltungsStufeId = id;
		this.bezeichnung = bezeichnung;

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

}
