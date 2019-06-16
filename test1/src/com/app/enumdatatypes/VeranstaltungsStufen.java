package com.app.enumdatatypes;

import com.app.printclasses.BGH1RichterBlatt;
import com.app.printclasses.BHRichterBlatt;
import com.app.printclasses.BhVtRichterBlatt;
import com.app.printclasses.GAP1RichterBlatt;
import com.app.printclasses.GAP2RichterBlatt;
import com.app.printclasses.GAP3RichterBlatt;
import com.app.printclasses.IBgh1RichterBlatt;
import com.app.printclasses.IBgh2RichterBlatt;
import com.app.printclasses.IBgh3RichterBlatt;
import com.app.printclasses.IBghRichterBlatt;
import com.app.printclasses.RBP1RichterBlatt;
import com.app.printclasses.RBP22017RichterBlatt;
import com.app.printclasses.RBP2RichterBlatt;
import com.app.printclasses.RBP32017RichterBlatt;
import com.app.printclasses.RBP3RichterBlatt;
import com.app.printclasses.RBP4RichterBlatt;
import com.vaadin.ui.CustomComponent;


public enum VeranstaltungsStufen {

	STUFE_BH(1, "BH", "Begleithundeprüfung", BHRichterBlatt.class, null), 
	STUFE_BGH1(2, "BGH1", "Begleithundeprüfung 1", BGH1RichterBlatt.class, VeranstaltungsStationen.BGH1, 1,2,3,4,5), 
	STUFE_BGH2(3, "BGH2","Begleithundeprüfung 2", null, VeranstaltungsStationen.BGH2, 1,2,3,4,5), 
	STUFE_BGH3(4, "BGH3","Begleithundeprüfung 3", null, VeranstaltungsStationen.BGH3, 1,2,3,4,5),
	STUFE_RBP1(5,"RBP1","Retrieverbasisprüfung 1", RBP1RichterBlatt.class, VeranstaltungsStationen.RBP1, 6,7,8,9,10),
	STUFE_RBP2(6,"RBP2","Retrieverbasisprüfung 2", RBP2RichterBlatt.class, VeranstaltungsStationen.RBP2, 6,7,8,9,10),
	STUFE_RBP3(7,"RBP3","Retrieverbasisprüfung 3", RBP3RichterBlatt.class, VeranstaltungsStationen.RBP3, 6,7,8,9,10),
	STUFE_RBP4_O_WASSER(8,"RBP4","Retrieverbasisprüfung 4", RBP4RichterBlatt.class, VeranstaltungsStationen.RBP4, 6,7,8,9,10),
	STUFE_RBP4_M_WASSER(9,"RBP4","Retrieverbasisprüfung 4 (mit Wasser)", RBP4RichterBlatt.class, VeranstaltungsStationen.RBP4, 6,7,8,9,10),
	STUFE_GAP1(10,"GAP1","Gehorsam- und Apportierprüfung 1", GAP1RichterBlatt.class, VeranstaltungsStationen.GAP1, 11,12,13,14,15),
	STUFE_GAP2(11,"GAP2","Gehorsam- und Apportierprüfung 2", GAP2RichterBlatt.class, VeranstaltungsStationen.GAP2, 11,12,13,14,15),
	STUFE_GAP3(12,"GAP3","Gehorsam- und Apportierprüfung 3", GAP3RichterBlatt.class, VeranstaltungsStationen.GAP3, 11,12,13,14,15),
	TRAININGS_WT_ANFAENGER(13, "Anfänger", "Anfänger", null, VeranstaltungsStationen.TRAIN_WT_ANFAENGER, BewertungEnum.TRAINING_WT_BESTANDEN.id),
	TRAININGS_WT_FORTGESCHRITTEN(14, "Fortgeschritten", "Fortgeschritten", null, VeranstaltungsStationen.TRAIN_WT_FORTGESCHRITTEN
		, BewertungEnum.TRAINING_WT_BESTANDEN.id),
	WESENSTEST_GRUPPE_ALLGEMEIN(15, "Wesenstest Allgemein", "WesenstestAllgemein", null, VeranstaltungsStationen.WESENSTEST, 17, 18),
	WESENSTEST_GRUPPE_1(16, "Wesenstest Gruppe 1", "Wesenstest Gruppe 1", null, VeranstaltungsStationen.WESENSTEST, 17, 18),
	WESENSTEST_GRUPPE_2(17, "Wesenstest Gruppe 2", "Wesenstest Gruppe 2", null, VeranstaltungsStationen.WESENSTEST, 17, 18),
	WESENSTEST_GRUPPE_3(18, "Wesenstest Gruppe 3", "Wesenstest Gruppe 3", null, VeranstaltungsStationen.WESENSTEST, 17, 18),
	WESENSTEST_GRUPPE_4(19, "Wesenstest Gruppe 4", "Wesenstest Gruppe 4", null, VeranstaltungsStationen.WESENSTEST, 17, 18),
	WESENSTEST_GRUPPE_5(20, "Wesenstest Gruppe 5", "Wesenstest Gruppe 5", null, VeranstaltungsStationen.WESENSTEST, 17, 18),
	WESENSTEST_GRUPPE_6(21, "Wesenstest Gruppe 6", "Wesenstest Gruppe 6", null, VeranstaltungsStationen.WESENSTEST, 17, 18),
	STUFE_RBP2_2017(22,"RBP2","Retrieverbasisprüfung 2", RBP22017RichterBlatt.class, VeranstaltungsStationen.RBP2_2017, 6,7,8,9,10),
	STUFE_RBP3_2017(23,"RBP3","Retrieverbasisprüfung 3", RBP32017RichterBlatt.class, VeranstaltungsStationen.RBP3_2017, 6,7,8,9,10),
	TRAININGS_WT_EINSTEIGER(24, "Einsteiger", "Einsteiger", null, null, BewertungEnum.TRAINING_WT_BESTANDEN.id),
	TRAININGS_WT_LEICHT(25, "Fortgeschritten", "Fortgeschritten", null, null, BewertungEnum.TRAINING_WT_BESTANDEN.id),
	TRAININGS_WT_MITTEL(26, "Advanced", "Advanced", null, null, BewertungEnum.TRAINING_WT_BESTANDEN.id),
	JUNGHUNDEPRUEFUNG(27, "Junghundepruefung", "Junghundepruefung", null, null, BewertungEnum.TRAINING_WT_BESTANDEN.id),
	
//	TRAININGS_WT_EINSTEIGER(24, "Einsteiger", "Einsteiger", null, VeranstaltungsStationen.TRAIN_WT_EINSTEIGER, BewertungEnum.TRAINING_WT_BESTANDEN.id),
//	TRAININGS_WT_LEICHT(25, "Fortgeschritten", "Fortgeschritten", null, VeranstaltungsStationen.TRAIN_WT_LEICHT, BewertungEnum.TRAINING_WT_BESTANDEN.id),
//	TRAININGS_WT_MITTEL(26, "Advanced", "Advanced", null, VeranstaltungsStationen.TRAIN_WT_MITTEL, BewertungEnum.TRAINING_WT_BESTANDEN.id),
	
	STUFE_BH_VT(28, "BHVT", "BH VT", BhVtRichterBlatt.class, null), 
	STUFE_IBH_VT(29, "IBGH", "IBGH", IBghRichterBlatt.class, null), 
	STUFE_IBGH1(30, "IBGH1", "IBGH 1", IBgh1RichterBlatt.class, VeranstaltungsStationen.IBGH1, 1,2,3,4,5), 
	STUFE_IBGH2(31, "IBGH2","IBGH 2", IBgh2RichterBlatt.class, VeranstaltungsStationen.IBGH2, 1,2,3,4,5), 
	STUFE_IBGH3(32, "IBGH3","IBGH 3", IBgh3RichterBlatt.class, VeranstaltungsStationen.IBGH3, 1,2,3,4,5),
	
//	

	;
	;
	
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
