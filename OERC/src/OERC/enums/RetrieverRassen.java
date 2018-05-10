package OERC.enums;


public enum RetrieverRassen {
	GOLDEN_RETRIEVER(1, "GR","Golden Retriever", "Golden Retriever"),
	LABRADOR_RETRIEVER(2, "LR","Labrador Retriever", "Labrador Retriever"),
	FLAT_COATED_RETRIEVER(3, "FCR","Flat Coated Retriever", "FLat Coated Retriever"),
	NOVA_SCOTIA_DUCK_TOLLING_RETRIEVER(5, "DTR","Nova Scotia Duck Tolling Retriever", "Nova Scotia Duck Tolling Retriever"),
	CHESAPEAKE_BAY_RETRIEVER(4, "CBR","Chesapeake Bay Retriever","Chesapeake Bay Retriever"),
	CURLY_COATED_RETRIEVER(6, "CCR","Curly Coated Retriever", "Curly Coated Retriever");
	
	private final String RassenLangBezeichnung;
	private final String RassenKurzBezeichnung;
	private final String UrkundenBezeichnung;
	private final Integer idRasse;
	
	private RetrieverRassen(Integer idRasse, String kurzBezeichnung, String langBezeichnung, String urkundenBezeichnung) {
		this.idRasse = idRasse;
		this.RassenLangBezeichnung = langBezeichnung;
		this.RassenKurzBezeichnung = kurzBezeichnung;
		this.UrkundenBezeichnung = urkundenBezeichnung;
	}
	
	public String getRassenLangBezeichnung() {
		return this.RassenLangBezeichnung;
	}
	
	public String getRassenKurzBezeichnung() {
		return this.RassenKurzBezeichnung;
	}
	
	public static String getLangBezeichnungFuerKurzBezeichnung(String kurzbezeichnung) {
		
		for (RetrieverRassen o: RetrieverRassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o.RassenLangBezeichnung;
			}
			
		}
		return "";
	}
	
	public static RetrieverRassen getRasseForKurzBezeichnung(String kurzbezeichnung) {
		
		for (RetrieverRassen o: RetrieverRassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o;
			}
			
		}
		return null;
	}

	public static String getUrkundenBezeichnungFuerKurzBezeichnung(String kurzbezeichnung) {
		
		for (RetrieverRassen o: RetrieverRassen.values()) {
			if (o.RassenKurzBezeichnung.equals(kurzbezeichnung)) {
				return o.UrkundenBezeichnung;
			}
			
		}
		return "";
	}

	public String getUrkundenBezeichnung() {
		return UrkundenBezeichnung;
	}

	public Integer getIdRasse() {
		return idRasse;
	}
	


}
