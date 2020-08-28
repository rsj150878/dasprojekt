package com.app.enumdatatypes;

import java.util.ArrayList;
import java.util.List;

public enum Rassen {
	GOLDEN_RETRIEVER("GR", "Golden Retriever", "Golden Retriever", true),
	LABRADOR_RETRIEVER("LR", "Labrador Retriever", "Labrador Retriever", true),
	FLAT_COATED_RETRIEVER("FCR", "Flat Coated Retriever", "FLat Coated Retriever", true),
	NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER("DTR", "Nova Scotia Duck Tolling Retriever",
			"Nova Scotia Duck Tolling Retriever", true),
	CHESAPEAKE_BAY_RETRIEVER("CBR", "Chesapeake Bay Retriever", "Chesapeake Bay Retriever", true),
	CURLY_COATED_RETRIEVER("CCR", "Curly Coated Retriever", "Curly Coated Retriever", true),
	BORDER_COLLIE_TRICOLOR_LANGHAAR("BCTL", "Border Collie, Tricolor, Langhaar", "Border Collie, Tricolor, Langhaar",
			false),
	MISCHLING("MI", "Mischling", "", false),
	BERGER_BLANC_SUISSE("BBS", "Weisser Schäfer", "Berger Blance Suisse", false),
	BORDER_COLLIE("BC", "Border Collie", "Border Collie", false),
	COLLIE_LANGHAAR("CLH", "Langhaar Collie", "Langhaar Collie", false),
	ALLGEMEIN("ALG", "Allgemein", "Allgemein", false),
	AUSTRALIAN_SHEPHERD("ASH", "Australian Sheperd", "Australian Sheperd", false),
	PERRO_DE_AGUA_ESPANOL("PAE", "Perro de Agua Espanol", "Perro de Agua Espanol", false),
	LAGOTTO_ROGMANOLO("LGR", "Lagotto Rogmanolo", "Lagotto Rogmanolo", false),
	WEIMARANER_KURZHAAR("WK", "Weimaraner Kurzhaar", "Weimaraner", false),
	WEIMARANER_LANGHAAR("WL", "Weimaraner Langhaar", "Weimaraner", false),
	OESTERREICHISCHER_PINSCHER("OEP", "Österreichischer Pinscher", "Pinscher", false),
	AUSTRALIAN_LABRADOODLE("ALD", "Australian Labradoodle", "Australian Labradoodle", false),
	MAGYAR_VISZLA("MGZ", "Magyar Vizsla", "Magyar Vizsla", false),
	ENGLISH_COCKER_SPANIEL("CS", "English Cocker Spaniel", "English Cocker Spaniel", false),
	ISLANDHUND("ISL", "Islandhund", "Islandhund", false);

	private final String RassenLangBezeichnung;
	private final String RassenKurzBezeichnung;
	private final String UrkundenBezeichnung;
	private final Boolean isOercRasse;

	private Rassen(String kurzBezeichnung, String langBezeichnung, String urkundenBezeichnung, Boolean isOercRasse) {
		this.RassenLangBezeichnung = langBezeichnung;
		this.RassenKurzBezeichnung = kurzBezeichnung;
		this.UrkundenBezeichnung = urkundenBezeichnung;
		this.isOercRasse = isOercRasse;
	}

	public String getRassenLangBezeichnung() {
		return this.RassenLangBezeichnung;
	}

	public String getRassenKurzBezeichnung() {
		return this.RassenKurzBezeichnung;
	}

	public static String getLangBezeichnungFuerKurzBezeichnung(String kurzbezeichnung) {

		for (Rassen o : Rassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o.RassenLangBezeichnung;
			}

		}
		return "";
	}

	public static Rassen getRasseForKurzBezeichnung(String kurzbezeichnung) {

		for (Rassen o : Rassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o;
			}

		}
		return null;
	}

	public static String getUrkundenBezeichnungFuerKurzBezeichnung(String kurzbezeichnung) {

		for (Rassen o : Rassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o.UrkundenBezeichnung;
			}

		}
		return "";
	}

	public static List<Rassen> getOercRassen() {
		List<Rassen> result = new ArrayList<>();
		for (Rassen o : Rassen.values()) {
			if (o.isOercRasse) {
				result.add(o);
			}

		}
		return result;
	}

	public Boolean getIsOercRasse() {
		return isOercRasse;
	}

}
