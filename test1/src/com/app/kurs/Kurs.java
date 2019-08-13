package com.app.kurs;

import java.util.Date;

public class Kurs 
{
	private Integer idKurs;
	private Integer version;
	private String kursBezeichnung;
	private Date startDat;
	private Date endeDat;
	private Double preisOerc1Hund;
	private Double preisOerc2Hund;
	
	private Double preisNoerc1Hund;
	private Double preisNoerc2Hund;
	public Integer getIdKurs() {
		return idKurs;
	}
	public void setIdKurs(Integer idKurs) {
		this.idKurs = idKurs;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getKursBezeichnung() {
		return kursBezeichnung;
	}
	public void setKursBezeichnung(String kursBezeichnung) {
		this.kursBezeichnung = kursBezeichnung;
	}
	public Date getStartDat() {
		return startDat;
	}
	public void setStartDat(Date startDat) {
		this.startDat = startDat;
	}
	public Date getEndeDat() {
		return endeDat;
	}
	public void setEndeDat(Date endeDat) {
		this.endeDat = endeDat;
	}
	public Double getPreisOerc1Hund() {
		return preisOerc1Hund;
	}
	public void setPreisOerc1Hund(Double preisOerc1Hund) {
		this.preisOerc1Hund = preisOerc1Hund;
	}
	public Double getPreisOerc2Hund() {
		return preisOerc2Hund;
	}
	public void setPreisOerc2Hund(Double preisOerc2Hund) {
		this.preisOerc2Hund = preisOerc2Hund;
	}
	public Double getPreisNoerc1Hund() {
		return preisNoerc1Hund;
	}
	public void setPreisNoerc1Hund(Double preisNoerc1Hund) {
		this.preisNoerc1Hund = preisNoerc1Hund;
	}
	public Double getPreisNoerc2Hund() {
		return preisNoerc2Hund;
	}
	public void setPreisNoerc2Hund(Double preisNoerc2Hund) {
		this.preisNoerc2Hund = preisNoerc2Hund;
	}
	
	
	

}
