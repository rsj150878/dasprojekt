package com.app.showdata;

import com.app.enumdatatypes.Rassen;

public class ShowGeschlechtEnde extends ShowKlasse {

	private ShowRing ringGeschlechtEndeFor;

	private String geschlechtEnde;
	private Rassen rasse;

	public ShowGeschlechtEnde(ShowRing ringGeschlechtEndeFor) {
		this.ringGeschlechtEndeFor = ringGeschlechtEndeFor;
	}

	public ShowGeschlechtEnde() {

	}    

	// @Override
	public String getKatalogNummer() {
		return rasse.getRassenKurzBezeichnung() + " - " + ("R".equals(geschlechtEnde) ? "Rüden" : "Hündinnen") + "-Ende";
	}

//	public Stream<ShowRing> getKlassenAsStream() {
//		return ringGeschlechtEndeFor == null ? Stream.empty() : ringGeschlechtEndeFor.getKlassenAsStream();
//	}
//	
//	public Stream<ShowRing> flattened() {
//        return Stream.concat(Stream.of(this),
//                getKlassenAsStream().flatMap(ShowRing::flattened));
//    }

	public String getKatalognumer() {
		return "notDefined";
	}

	public ShowRing getRingGeschlechtEndeFor() {
		return ringGeschlechtEndeFor;
	}

	public void setKlasseEndeFor(ShowRing ringGeschlechtEndeFor) {
		this.ringGeschlechtEndeFor = ringGeschlechtEndeFor;
	}

	public String getGeschlechtEnde() {
		return geschlechtEnde;
	}

	public void setGeschlechtEnde(String geschlechtEnde) {
		this.geschlechtEnde = geschlechtEnde;
	}

	public Rassen getRasse() {
		System.out.println("rasse in abschluss-klasse " + rasse);
		return rasse;
	}

	public void setRasse(Rassen rasse) {
		this.rasse = rasse;
	}

	public void setRingGeschlechtEndeFor(ShowRing ringGeschlechtEndeFor) {
		this.ringGeschlechtEndeFor = ringGeschlechtEndeFor;
	}

	public String getClubsieger() {
		return "not-defined";
	}

}
