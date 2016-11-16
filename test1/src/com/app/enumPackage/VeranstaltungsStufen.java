package com.app.enumPackage;

import com.app.printClasses.BGH1RichterBlatt;
import com.app.printClasses.BHRichterBlatt;
import com.app.printClasses.GAP1RichterBlatt;
import com.app.printClasses.GAP2RichterBlatt;
import com.app.printClasses.GAP3RichterBlatt;
import com.app.printClasses.RBP1RichterBlatt;
import com.app.printClasses.RBP2RichterBlatt;
import com.app.printClasses.RBP3RichterBlatt;
import com.app.printClasses.RBP4RichterBlatt;
import com.vaadin.ui.CustomComponent;


public enum VeranstaltungsStufen {

	STUFE_BH(1, "BH", "Begleithundeprüfung", BHRichterBlatt.class, null), 
	STUFE_BGH1(2, "BGH1", "Begleithundeprüfung 1", BGH1RichterBlatt.class, VeranstaltungsStationen.BGH1, 1,2,3,4,5), 
	STUFE_BGH2(3, "BGH2","Begleithundeprüfung 2", null, VeranstaltungsStationen.BGH1, 1,2,3,4,5), 
	STUFE_BGH3(4, "BGH3","Begleithundeprüfung 3", null, VeranstaltungsStationen.BGH1, 1,2,3,4,5),
	STUFE_RBP1(5,"RBP1","Retrieverbasisprüfung 1", RBP1RichterBlatt.class, VeranstaltungsStationen.RBP1, 6,7,8,9,10),
	STUFE_RBP2(6,"RBP2","Retrieverbasisprüfung 2", RBP2RichterBlatt.class, VeranstaltungsStationen.RBP2, 6,7,8,9,10),
	STUFE_RBP3(7,"RBP3","Retrieverbasisprüfung 3", RBP3RichterBlatt.class, VeranstaltungsStationen.RBP3, 6,7,8,9,10),
	STUFE_RBP4_O_WASSER(8,"RBP4","Retrieverbasisprüfung 4", RBP4RichterBlatt.class, VeranstaltungsStationen.RBP4, 6,7,8,9,10),
	STUFE_RBP4_M_WASSER(9,"RBP4","Retrieverbasisprüfung 4 (mit Wasser)", RBP4RichterBlatt.class, VeranstaltungsStationen.RBP4, 6,7,8,9,10),
	STUFE_GAP1(10,"GAP1","Gehorsam- und Apportierprüfung 1", GAP1RichterBlatt.class, VeranstaltungsStationen.GAP1, 11,12,13,14,15),
	STUFE_GAP2(11,"GAP2","Gehorsam- und Apportierprüfung 2", GAP2RichterBlatt.class, VeranstaltungsStationen.GAP2, 11,12,13,14,15),
	STUFE_GAP3(12,"GAP3","Gehorsam- und Apportierprüfung 3", GAP3RichterBlatt.class, VeranstaltungsStationen.GAP3, 11,12,13,14,15),
	TRAININGS_WT_ANFAENGER(13, "Anfänger", "Anfänger", null, null, BewertungEnum.TRAINING_WT_BESTANDEN.id),
	TRAININGS_WT_FORTGESCHRITTEN(14, "Fortgeschritten", "Fortgeschritten", null, null, BewertungEnum.TRAINING_WT_BESTANDEN.id);
	
	private Integer veranstaltungsStufeId;
	private String bezeichnung;
	private String langBezeichnung;
	private Integer[] bewertung;
	private VeranstaltungsStationen stationen;
	private Class<? extends CustomComponent> richterBlatt;

	private VeranstaltungsStufen(Integer id, String bezeichnung, String langBezeichnung, Class <? extends CustomComponent> richterBlatt,
			VeranstaltungsStationen stationen, Integer... bewertung) {
		this.veranstaltungsStufeId = id;
		this.bezeichnung = bezeichnung;
		this.bewertung = bewertung;
		this.langBezeichnung = langBezeichnung;
		this.stationen = stationen;
		this.richterBlatt = richterBlatt;

		
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
	
	public String getBewertung(Integer punkte) {
		for(Integer x: bewertung) {
			BewertungEnum bwEnum = BewertungEnum.getBewertung(x);
			if (bwEnum.von <= punkte && bwEnum.bis >= punkte) {
				return bwEnum.bewertung;
			}
		}
		return "";
	}

	public String getLangBezeichnung() {
		return langBezeichnung;
	}

	public void setLangBezeichnung(String langBezeichnung) {
		this.langBezeichnung = langBezeichnung;
	}
	
	public VeranstaltungsStationen getStationen() {
		return stationen;
	}

	public Class<? extends CustomComponent> getRichterBlatt() {
		return richterBlatt;
	}

	public void setRichterBlatt(Class<? extends CustomComponent> richterBlatt) {
		this.richterBlatt = richterBlatt;
	}
	
	

}
