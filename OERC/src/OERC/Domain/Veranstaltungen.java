package OERC.Domain;

import java.util.Date;
import java.util.UUID;

public class Veranstaltungen {
	private UUID idVeranstaltung;
	private Integer IDVAKategorie;
	private String bezeichnung;
	private Date anfangDatum;
	private Date endDatum;
	
	public UUID getIdVeranstaltung() {
		return idVeranstaltung;
	}
	public void setIdVeranstaltung(UUID idVeranstaltung) {
		this.idVeranstaltung = idVeranstaltung;
	}
	public Integer getIDVAKategorie() {
		return IDVAKategorie;
	}
	public void setIDVAKategorie(Integer iDVAKategorie) {
		IDVAKategorie = iDVAKategorie;
	}
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public Date getAnfangDatum() {
		return anfangDatum;
	}
	public void setAnfangDatum(Date anfangDatum) {
		this.anfangDatum = anfangDatum;
	}
	public Date getEndDatum() {
		return endDatum;
	}
	public void setEndDatum(Date endDatum) {
		this.endDatum = endDatum;
	}
	

}
