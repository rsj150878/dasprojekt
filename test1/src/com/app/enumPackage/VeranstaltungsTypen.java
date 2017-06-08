package com.app.enumPackage;

public enum VeranstaltungsTypen {

	BH_BGH_PRÜFUNG(1, "BH-BGH-Prüfung", Boolean.TRUE, 1, 2, 3, 4),
	RBP_O_WASSER(2,"RBP ohne Wasser" ,Boolean.FALSE,5,6,7,8),
	RBP_M_WASSER(3,"RBP mit Wasser", Boolean.FALSE,5,6,7,9),
	GAP_PRÜFUNG(4,"GAP", Boolean.TRUE,10,11,12),
	TRAIN_WT(5, "Trainings-Working-Test", Boolean.TRUE, 13,14),
	WESENSTEST(6, "Wesentest", Boolean.TRUE, 15,16,17,18,19,20,21),
	RBP_2017_WASSER(7,"RBP" ,Boolean.TRUE,5,22,23,8),
;

	private Integer veranstaltungsTypID;
	private String veranstaltungsTypBezeichnung;
	private Integer[] veranstaltungsStufen;
	private Boolean isAktiv;


	private VeranstaltungsTypen(Integer veranstaltungsTypId,
			String veranstaltungsTypBezeichnung, Boolean isAktiv, Integer... veranstaltungsStufen) {
		this.veranstaltungsTypID = veranstaltungsTypId;
		this.veranstaltungsTypBezeichnung = veranstaltungsTypBezeichnung;
		this.veranstaltungsStufen = veranstaltungsStufen;
		this.isAktiv = isAktiv;

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
	
	public static VeranstaltungsTypen getVeranstaltungsTypForId(Integer id) {
		for (VeranstaltungsTypen x : VeranstaltungsTypen.values()) {
			if (id.equals(x.veranstaltungsTypID)) {
				return x;
			}

		}

		return null;
		
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

	public Boolean getIsAktiv() {
		return isAktiv;
	}

	public void setIsAktiv(Boolean isAktiv) {
		this.isAktiv = isAktiv;
	}

}
