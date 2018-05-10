package com.app.enumPackage;

public enum FormWertJuengsten {
	
	VIELVERSPRECHEND ("VV", "vielversprechend"),
	VERSPRECHEND("V", "versprechend"),
	NICHT_VERSPRECHEND("NV", "nicht versprechend");
	
	private String formwert;
	private String formWertBezeichnung;
	
	private FormWertJuengsten(String formwert, String formWertBezeichnung) {
		this.formwert = formwert;
		this.formWertBezeichnung = formWertBezeichnung;
	}
	
	public static FormWertJuengsten getFormwertForKurzBezeichnung(String kurzbezeichnung) {

		for (FormWertJuengsten o : FormWertJuengsten.values()) {
			if (o.formwert.equals(kurzbezeichnung)) {
				return o;
			}

		}
		return null;
	}

	public String getFormwert() {
		return formwert;
	}

	public String getFormWertBezeichnung() {
		return formWertBezeichnung;
	}

	

}
