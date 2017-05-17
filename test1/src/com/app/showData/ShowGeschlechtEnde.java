package com.app.showData;

public class ShowGeschlechtEnde extends ShowKlasse {
	
	private ShowRing ringGeschlechtEndeFor;

	private String geschlechtEnde;
	public ShowGeschlechtEnde (ShowRing ringGeschlechtEndeFor) {
		this.ringGeschlechtEndeFor = ringGeschlechtEndeFor;
	}
	
	public ShowGeschlechtEnde() {
		
	}
	
	@Override
	public String getKatalogNummer() {
		return geschlechtEnde + "-Ende";
	}

	public String getKatalognumer() {
		return "notDefined";
	}
	
	public ShowRing getRingGeschlechtEndeFor() {
		return ringGeschlechtEndeFor;
	}

	public void setKlasseEndeFor(ShowRing ringGeschlechtEndeFor) {
		this.ringGeschlechtEndeFor = ringGeschlechtEndeFor;
	}

	
	
}
