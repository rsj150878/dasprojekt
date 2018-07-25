package com.app.enumdatatypes;

public enum BewertungEnum {

	BGH_MANGELHAFT(1,0,69,"mangelhaft"),
	BGH_BEFRIEDIGEND(2,70,79,"befriedigend"),
	BGH_GUT(3,80,89,"gut"),
	BGH_SEHRGUT(4,90,95,"sehr gut"),
	BGH_VORZÜGLICH(5,96,100,"vorzüglich"),
	RBP_MANGELHAFT(6,0,69,"mangelhaft"),
	RBP_BEFRIEDIGEND(7,70,79,"befriedigend"),
	RBP_GUT(8,80,89,"gut"),
	RBP_SEHR_GUT(9,90,95,"sehr gut"),
	RBP_VORZÜGLICH(10,96,100,"vorzüglich"),
	GAP_MANGELHAFT(11,0,69,"mangelhaft"),
	GAP_BEFRIEDIGEND(12,70,79,"befriedigend"),
	GAP_GUT(13,80,89,"gut"),
	GAP_SEHR_GUT(14,90,95,"sehr gut"),
	GAP_VORZÜGLICH(15,96,100,"vorzüglich"),
	TRAINING_WT_BESTANDEN(16,0,100,"bestanden"),
	WESENSTEST_BESTANDEN(17, 81,99, "bestanden"),
	WESENSTEST_NICHT_BESTANDEN(18,0,80, "nicht bestanden");
	
	
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
