package com.app.enumPackage;

public enum BewertungEnum {

	BGH_MANGELHAFT(1,0,69,"mangelhaft"),
	BGH_BEFRIEDIGEND(2,70,79,"befriedigend"),
	BGH_GUT(3,80,89,"gut"),
	BGH_SEHRGUT(4,90,95,"sehr gut"),
	BGH_VORZÜGLICH(5,96,100,"vorzüglich");
	
	public Integer id;
	public Integer von;
	public Integer bis;
	public String bewertung;
	
	private BewertungEnum(Integer id, Integer von, Integer bis, String bewertung) {
		this.id = id;
		this.von = von;
		this.bis = bis;
		this.bewertung = bewertung;
	}
	
	public static BewertungEnum getBewertung(Integer id) {
		for(BewertungEnum x: BewertungEnum.values()) {
			if  (x.id.equals(id)) {
				return x;
			}
		}
		
		return null;
		
	}
	
	
}
