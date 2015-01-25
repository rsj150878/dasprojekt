package com.app.bean;

public class RassenBean {

	private String rassenLangBezeichnung;
	private String rassenKurzBezeichnung;

	public RassenBean(String RassenKurzBezeichnung, String RassenLangBezeichnung) {
		this.rassenKurzBezeichnung = RassenKurzBezeichnung;
		this.rassenLangBezeichnung = RassenLangBezeichnung;
	}

	public String getRassenLangBezeichnung() {
		return rassenLangBezeichnung;
	}

	public void setRassenLangBezeichnung(String rassenLangBezeichnung) {
		this.rassenLangBezeichnung = rassenLangBezeichnung;
	}

	public String getRassenKurzBezeichnung() {
		return rassenKurzBezeichnung;
	}

	public void setRassenKurzBezeichnung(String rassenKurzBezeichnung) {
		this.rassenKurzBezeichnung = rassenKurzBezeichnung;
	}

	public boolean equals(String rassenKurzbezeichnung) {
		System.out.println("bin in equals");
		return this.rassenKurzBezeichnung.equals(rassenKurzBezeichnung);
	}

	
	
}
