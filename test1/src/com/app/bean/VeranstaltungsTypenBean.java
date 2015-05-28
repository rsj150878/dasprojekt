package com.app.bean;

public class VeranstaltungsTypenBean {

	private Integer veranstaltungsTyp;
	private String veranstaltungsBezeichnung;
	private Integer[] veranstaltungsStufen;
	
	public VeranstaltungsTypenBean(Integer veranstaltungsTyp, String veranstaltungsBezeichnung, Integer[] veranstaltungsStufen) {
		this.veranstaltungsTyp = veranstaltungsTyp;
		this.veranstaltungsBezeichnung = veranstaltungsBezeichnung;
		this.veranstaltungsStufen = veranstaltungsStufen;
	}
	
	public Integer getVeranstaltungsTyp() {
		return veranstaltungsTyp;
	}

	public void setVeranstaltungsTyp(Integer veranstaltungsTyp) {
		this.veranstaltungsTyp = veranstaltungsTyp;
	}

	public String getVeranstaltungsBezeichnung() {
		return veranstaltungsBezeichnung;
	}

	public void setVeranstaltungsBezeichnung(String veranstaltungsBezeichnung) {
		this.veranstaltungsBezeichnung = veranstaltungsBezeichnung;
	}

	public Integer[] getVeranstaltungsStufen() {
		return veranstaltungsStufen;
	}

	public void setVeranstaltungsStufen(Integer[] veranstaltungsStufen) {
		this.veranstaltungsStufen = veranstaltungsStufen;
	}
	
	
}
