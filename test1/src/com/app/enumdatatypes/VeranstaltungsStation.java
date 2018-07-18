package com.app.enumdatatypes;

public enum VeranstaltungsStation {
	
	RBP1_UE1 (1, "uebung1", 0, 20),
	RBP1_UE2 (2, "uebung2", 0, 20),
	RBP1_UE3 (3, "uebung3", 0, 20),
	RBP1_UE4 (4, "uebung4", 0, 10),
	RBP1_UE5 (5, "uebung5", 0, 15),
	RBP1_UE6 (6, "uebung6", 0, 15),
	RBP2_UE1 (7, "uebung1", 0, 15),
	RBP2_UE2 (8, "uebung2", 0, 15),
	RBP2_UE3 (9, "uebung3", 0, 15),
	RBP2_UE4 (10, "uebung4", 0, 15),
	RBP2_UE5 (11, "uebung5", 0, 10),
	RBP2_UE6 (12, "uebung6", 0, 15),
	RBP2_UE7 (13, "uebung7", 0, 15),
	RBP3_UE1 (14, "uebung1", 0, 10),
	RBP3_UE2 (15, "uebung2", 0, 15),
	RBP3_UE3 (16, "uebung3", 0, 15),
	RBP3_UE4 (17, "uebung4", 0, 15),
	RBP3_UE5 (18, "uebung5", 0, 15),
	RBP3_UE6 (19, "uebung6", 0, 15),
	RBP3_UE7 (20, "uebung7", 0, 15),
	RBP4_UE1 (21, "uebung1", 0, 20),
	RBP4_UE2 (22, "uebung2", 0, 20),
	RBP4_UE3 (23, "uebung3", 0, 20),
	RBP4_UE4 (24, "uebung4", 0, 20),
	RBP4_UE5 (25, "uebung5", 0, 20),
	GAP1_UE1 (26, "uebung1", 0, 30),
	GAP1_UE2 (27, "uebung2", 0, 15),
	GAP1_UE3 (28, "uebung3", 0, 20),
	GAP1_UE4 (29, "uebung4", 0, 25),
	GAP1_UE5 (30, "uebung5", 0, 10),
	GAP2_UE1 (31, "uebung1", 0, 25),
	GAP2_UE2 (32, "uebung2", 0, 25),
	GAP2_UE3 (33, "uebung3", 0, 20),
	GAP2_UE4 (34, "uebung4", 0, 20),
	GAP2_UE5 (35, "uebung5", 0, 10),
	GAP3_UE1 (36, "uebung1", 0, 20),
	GAP3_UE2 (37, "uebung2", 0, 20),
	GAP3_UE3 (38, "uebung3", 0, 20),
	GAP3_UE4 (39, "uebung4", 0, 20),
	GAP3_UE5 (40, "uebung5", 0, 20),
	BGH1_UE1 (41, "uebung1", 0, 30),
	BGH1_UE2 (42, "uebung2", 0, 30),
	BGH1_UE3 (43, "uebung3", 0, 15),
	BGH1_UE4 (44, "uebung4", 0, 15),
	BGH1_UE5 (45, "uebung5", 0, 10),
	WESENSTEST(46, "uebung1", 0, 100),
	WESENSTEST_BEMERKUNG(47,"bemerkung",null,null),
	RBP2_UE1_2017 (7, "uebung1", 0, 25),
	RBP2_UE4_2017 (10, "uebung4", 0, 20),
	RBP3_UE1_2017 (14, "uebung1", 0, 20),
	RBP3_UE3_2017 (16, "uebung3", 0, 20),

	;
	
	private Integer idStation;
	private String uebung;
	private Integer minPunkte;
	private Integer maxPunkte;
	
	
	private VeranstaltungsStation (Integer idStation, String uebung, Integer minPunkte, Integer maxPunkte) {
		
		this.idStation = idStation;
		this.uebung = uebung;
		this.minPunkte = minPunkte;
		this.maxPunkte = maxPunkte;
		
	}
	
	public static VeranstaltungsStation getStationForId(Integer id) {
		for (VeranstaltungsStation x : VeranstaltungsStation.values()) {
			if (id.equals(x.idStation)) {
				return x;
			}

		}

		return null;
	}

	public Integer getIdStation() {
		return idStation;
	}

	public String getUebung() {
		return uebung;
	}

	public Integer getMinPunkte() {
		return minPunkte;
	}

	public Integer getMaxPunkte() {
		return maxPunkte;
	}
	
	


}
