package com.app.enumdatatypes;

public enum VeranstaltungsStation {
	
	RBP1_UE1 (1, 1,"uebung1", 0, 20),
	RBP1_UE2 (2, 2,"uebung2", 0, 20),
	RBP1_UE3 (3, 3,"uebung3", 0, 20),
	RBP1_UE4 (4, 4,"uebung4", 0, 10),
	RBP1_UE5 (5, 5,"uebung5", 0, 15),
	RBP1_UE6 (6, 6,"uebung6", 0, 15),
	RBP2_UE1 (7, 1,"uebung1", 0, 15),
	RBP2_UE2 (8, 2,"uebung2", 0, 15),
	RBP2_UE3 (9, 3,"uebung3", 0, 15),
	RBP2_UE4 (10, 4,"uebung4", 0, 15),
	RBP2_UE5 (11, 5,"uebung5", 0, 10),
	RBP2_UE6 (12, 6,"uebung6", 0, 15),
	RBP2_UE7 (13, 7,"uebung7", 0, 15),
	RBP3_UE1 (14, 1,"uebung1", 0, 10),
	RBP3_UE2 (15, 2,"uebung2", 0, 15),
	RBP3_UE3 (16, 3,"uebung3", 0, 15),
	RBP3_UE4 (17, 4,"uebung4", 0, 15),
	RBP3_UE5 (18, 5,"uebung5", 0, 15),
	RBP3_UE6 (19, 6,"uebung6", 0, 15),
	RBP3_UE7 (20, 7,"uebung7", 0, 15),
	RBP4_UE1 (21, 1,"uebung1", 0, 20),
	RBP4_UE2 (22, 2,"uebung2", 0, 20),
	RBP4_UE3 (23, 3,"uebung3", 0, 20),
	RBP4_UE4 (24, 4,"uebung4", 0, 20),
	RBP4_UE5 (25, 5,"uebung5", 0, 20),
	GAP1_UE1 (26, 1,"uebung1", 0, 30),
	GAP1_UE2 (27, 2,"uebung2", 0, 15),
	GAP1_UE3 (28, 3,"uebung3", 0, 20),
	GAP1_UE4 (29, 4,"uebung4", 0, 25),
	GAP1_UE5 (30, 5,"uebung5", 0, 10),
	GAP2_UE1 (31, 1,"uebung1", 0, 25),
	GAP2_UE2 (32, 2,"uebung2", 0, 25),
	GAP2_UE3 (33, 3,"uebung3", 0, 20),
	GAP2_UE4 (34, 4,"uebung4", 0, 20),
	GAP2_UE5 (35, 5,"uebung5", 0, 10),
	GAP3_UE1 (36, 1,"uebung1", 0, 20),
	GAP3_UE2 (37, 2,"uebung2", 0, 20),
	GAP3_UE3 (38, 3,"uebung3", 0, 20),
	GAP3_UE4 (39, 4,"uebung4", 0, 20),
	GAP3_UE5 (40, 5,"uebung5", 0, 20),
	BGH1_UE1 (41, 1,"uebung1", 0, 30),
	BGH1_UE2 (42, 2,"uebung2", 0, 30),
	BGH1_UE3 (43, 3,"uebung3", 0, 15),
	BGH1_UE4 (44, 4,"uebung4", 0, 15),
	BGH1_UE5 (45, 5,"uebung5", 0, 10),
	WESENSTEST(46, 1,"uebung1", 0, 100),
	WESENSTEST_BEMERKUNG(47,0,"bemerkung",null,null),
	RBP2_UE1_2017 (7, 1,"uebung1", 0, 25),
	RBP2_UE4_2017 (10,4, "uebung4", 0, 20),
	RBP3_UE1_2017 (14, 1,"uebung1", 0, 20),
	RBP3_UE3_2017 (16, 3,"uebung3", 0, 20),
	BGH2_UE1 (47, 1,"uebung1", 0, 20),
	BGH2_UE2 (48, 2,"uebung2", 0, 20),
	BGH2_UE3 (49, 3,"uebung3", 0, 15),
	BGH2_UE4 (50, 4,"uebung4", 0, 15),
	BGH2_UE5 (51, 5,"uebung5", 0, 10),
	BGH2_UE6 (52, 6,"uebung6", 0, 10),
	BGH2_UE7 (53, 7,"uebung7", 0, 10),

	BGH3_UE1 (54, 1,"uebung1", 0, 20),
	BGH3_UE2 (55, 2,"uebung2", 0, 10),
	BGH3_UE3 (56, 3,"uebung3", 0, 10),
	BGH3_UE4 (57, 4,"uebung4", 0, 10),
	BGH3_UE5 (58, 5,"uebung5", 0, 15),
	BGH3_UE6 (59, 6,"uebung6", 0, 15),
	BGH3_UE7 (60, 7,"uebung7", 0, 10),
	BGH3_UE8 (61, 8,"uebung8", 0, 10),

	;
	
	private Integer idStation;
	private String uebung;
	private Integer minPunkte;
	private Integer maxPunkte;
	private Integer stationNr;
	
	
	private VeranstaltungsStation (Integer idStation, Integer stationNr, String uebung, Integer minPunkte, Integer maxPunkte) {
		
		this.idStation = idStation;
		this.uebung = uebung;
		this.minPunkte = minPunkte;
		this.maxPunkte = maxPunkte;
		this.stationNr = stationNr;
		
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
	
	public Integer getStationNr() {
		return stationNr;
	}
	
	


}
