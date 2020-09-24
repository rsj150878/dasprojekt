package com.app.showdata;

import com.app.enumdatatypes.Rassen;

public class ShowRasseEnde extends ShowKlasse {

	private ShowRing ringRasseEndeFor;

	private Rassen rasse;

	// @Override
	public String getKatalogNummer() {
		System.out.println("rasse " + rasse);
		return rasse.getRassenKurzBezeichnung() +  "-Ende";
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

	public ShowRing getRasseEndeFor() {
		return ringRasseEndeFor;
	}

	public void setRasseEndeFor(ShowRing ringRasseEndeFor) {
		this.ringRasseEndeFor = ringRasseEndeFor;
	}

	public Rassen getRasse() {
		System.out.println("rasse in abschluss-klasse " + rasse);
		return rasse;
	}

	public void setRasse(Rassen rasse) {
		this.rasse = rasse;
	}

	
	public String getClubsieger() {
		return "not-defined";
	}

}
