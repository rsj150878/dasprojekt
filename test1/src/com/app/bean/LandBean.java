package com.app.bean;

public class LandBean {
	
	private String landKurzBezeichnung;
	private String landLangBezeichnung;
	
	public LandBean(String landKurzBezeichnung, String landLangBezeichnung ){
		this.landKurzBezeichnung = landKurzBezeichnung;
		this.landLangBezeichnung = landLangBezeichnung;
	}

	public String getLandKurzBezeichnung() {
		return landKurzBezeichnung;
	}

	public void setLandKurzBezeichnung(String landKurzBezeichnung) {
		this.landKurzBezeichnung = landKurzBezeichnung;
	}

	public String getLandLangBezeichnung() {
		return landLangBezeichnung;
	}

	public void setLandLangBezeichnung(String landLangBezeichnung) {
		this.landLangBezeichnung = landLangBezeichnung;
	}
	
		

}
