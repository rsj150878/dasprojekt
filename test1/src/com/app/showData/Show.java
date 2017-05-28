package com.app.showData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Show {
	private Integer idSchau;
	private String schaubezeichnung;
	private String schauKuerzel;
	private Date schauDate;
	private String leiter;
	private String schauTyp;
	private List<ShowRing> ringe;
	
	public Integer getIdSchau() {
		return idSchau;
	}
	public void setIdSchau(Integer idSchau) {
		this.idSchau = idSchau;
	}
	public String getSchaubezeichnung() {
		return schaubezeichnung;
	}
	public void setSchaubezeichnung(String schaubezeichnung) {
		this.schaubezeichnung = schaubezeichnung;
	}
	public String getSchauKuerzel() {
		return schauKuerzel;
	}
	public void setSchauKuerzel(String schauKuerzel) {
		this.schauKuerzel = schauKuerzel;
	}
	public Date getSchauDate() {
		return schauDate;
	}
	public void setSchauDate(Date schauDate) {
		this.schauDate = schauDate;
	}
	public String getLeiter() {
		return leiter;
	}
	public void setLeiter(String leiter) {
		this.leiter = leiter;
	}
	public String getSchauTyp() {
		return schauTyp;
	}
	public void setSchauTyp(String schauTyp) {
		this.schauTyp = schauTyp;
	}
	public List<ShowRing> getRinge() {
		return ringe;
	}
	public void setRinge(List<ShowRing> ringe) {
		this.ringe = ringe;
	}
	
	
	

}
