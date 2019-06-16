package com.app.enumdatatypes;

public enum BestandenDataType {
	
	NICHT_BESTANDEN("N","nicht bestanden"),
	BESTANDEN("J","bestanden"),
	AUSZEICHNUNG("A","Auszeichnung");
	
	public String dbWert;
	public String bezeichnung;
	
	private BestandenDataType(String dbWert, String bezeichnung) {
		this.dbWert = dbWert;
		this.bezeichnung = bezeichnung;
	}

	public static BestandenDataType getBestandenDataTypeForDb(String dbWert) {
		for(BestandenDataType x: BestandenDataType.values()) {
			if  (x.dbWert.equals(dbWert)) {
				return x;
			}
		}
		
		return null;
	}
	
	public String getBezeichnung() {
		return this.bezeichnung;
	}
}
