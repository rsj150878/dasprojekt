package com.app.bean;

public class BewertungsBean {
	
	private Integer von;
	private Integer bis;
	
	private String bewertung;
	
	public void BewertungsBean(Integer von, Integer bis, String bewertung) {
		this.von = von;
		this.bis = bis;
		this.bewertung = bewertung;
	}

	public Integer getVon() {
		return von;
	}

	public void setVon(Integer von) {
		this.von = von;
	}

	public Integer getBis() {
		return bis;
	}

	public void setBis(Integer bis) {
		this.bis = bis;
	}

	public String getBewertung() {
		return bewertung;
	}

	public void setBewertung(String bewertung) {
		this.bewertung = bewertung;
	}

	
}
