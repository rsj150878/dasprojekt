package OERC.Domain;

import java.util.Date;

public class Hund {
	
	private String idHund;;
	private String name;
	private String chipNummer;
	private Date wurfTag;
	private String zbnr;
	
	private String idMitglied;
	private Integer idGeschlecht;
	
	
	public String getIdHund() {
		return idHund;
	}
	public void setIdHund(String idHund) {
		this.idHund = idHund;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChipNummer() {
		return chipNummer;
	}
	public void setChipNummer(String chipNummer) {
		this.chipNummer = chipNummer;
	}
	public Date getWurfTag() {
		return wurfTag;
	}
	public void setWurfTag(Date wurfTag) {
		this.wurfTag = wurfTag;
	}
	public String getZbnr() {
		return zbnr;
	}
	public void setZbnr(String zbnr) {
		this.zbnr = zbnr;
	}
	public String getIdMitglied() {
		return idMitglied;
	}
	public void setIdMitglied(String idMitglied) {
		this.idMitglied = idMitglied;
	}
	public Integer getIdGeschlecht() {
		return idGeschlecht;
	}
	public void setIdGeschlecht(Integer idGeschlecht) {
		this.idGeschlecht = idGeschlecht;
	}
	
	

}
