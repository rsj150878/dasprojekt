package com.app.enumPackage;

public enum VeranstaltungsStationen {

	RBP1(1, VeranstaltungsStation.RBP1_UE1, VeranstaltungsStation.RBP1_UE2,
			VeranstaltungsStation.RBP1_UE3, VeranstaltungsStation.RBP1_UE4,
			VeranstaltungsStation.RBP1_UE5, VeranstaltungsStation.RBP1_UE6), 
	RBP2(2, VeranstaltungsStation.RBP2_UE1, VeranstaltungsStation.RBP2_UE2,
			VeranstaltungsStation.RBP2_UE3, VeranstaltungsStation.RBP2_UE4,
			VeranstaltungsStation.RBP2_UE5, VeranstaltungsStation.RBP2_UE6,
			VeranstaltungsStation.RBP2_UE7),
	RBP3(3, VeranstaltungsStation.RBP3_UE1, VeranstaltungsStation.RBP3_UE2,
			VeranstaltungsStation.RBP3_UE3, VeranstaltungsStation.RBP3_UE4,
			VeranstaltungsStation.RBP3_UE5, VeranstaltungsStation.RBP3_UE6,
			VeranstaltungsStation.RBP3_UE7), 
	RBP4(4, VeranstaltungsStation.RBP4_UE1, VeranstaltungsStation.RBP4_UE2,
			VeranstaltungsStation.RBP4_UE3, VeranstaltungsStation.RBP4_UE4,
			VeranstaltungsStation.RBP4_UE5), 
	GAP1(5, VeranstaltungsStation.GAP1_UE1, VeranstaltungsStation.GAP1_UE2,
			VeranstaltungsStation.GAP1_UE3, VeranstaltungsStation.GAP1_UE4,
			VeranstaltungsStation.GAP1_UE5), 
	GAP2(6, VeranstaltungsStation.GAP2_UE1, VeranstaltungsStation.GAP2_UE2,
			VeranstaltungsStation.GAP2_UE3, VeranstaltungsStation.GAP2_UE4,
			VeranstaltungsStation.GAP2_UE5), 
	GAP3(7, VeranstaltungsStation.GAP3_UE1, VeranstaltungsStation.GAP3_UE2,
			VeranstaltungsStation.GAP3_UE3, VeranstaltungsStation.GAP3_UE4,
			VeranstaltungsStation.GAP3_UE5), 
	BGH1(8, VeranstaltungsStation.BGH1_UE1, VeranstaltungsStation.BGH1_UE2,
			VeranstaltungsStation.BGH1_UE3, VeranstaltungsStation.BGH1_UE4,
			VeranstaltungsStation.BGH1_UE5);

	public Integer id;
	private VeranstaltungsStation[] station;

	private VeranstaltungsStationen(Integer id,
			VeranstaltungsStation... station) {
		this.id = id;
		this.station = station;
	}

	public VeranstaltungsStation []getStation() {
		return this.station;
	}

}
