package com.app.veranstaltung;

import java.util.Date;
import java.util.List;

import com.app.enumdatatypes.VeranstaltungsTypen;

public class Veranstaltung {

	private Integer id_veranstaltung;
	private Integer version;
	private String name;
	private Date datum;
	private String richter;
	private String veranstaltungsleiter;
	private VeranstaltungsTypen typ;
	private String veranstalter;
	private String veranstaltungsort;
	private String plz_leiter;
	private String ort_leiter;
	private String strasse_leiter;
	private String tel_nr_leiter;
	private Integer idschau;
	
	private List<VeranstaltungsRichter> vaRichter;
	
	public List<VeranstaltungsRichter> getVaRichter() {
		return vaRichter;
	}
	public void setVaRichter(List<VeranstaltungsRichter> vaRichter) {
		this.vaRichter = vaRichter;
	}
	public Integer getId_veranstaltung() {
		return id_veranstaltung;
	}
	public void setId_veranstaltung(Integer id_veranstaltung) {
		this.id_veranstaltung = id_veranstaltung;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public String getRichter() {
		return richter;
	}
	public void setRichter(String richter) {
		this.richter = richter;
	}
	public String getVeranstaltungsleiter() {
		return veranstaltungsleiter;
	}
	public void setVeranstaltungsleiter(String veranstaltungsleiter) {
		this.veranstaltungsleiter = veranstaltungsleiter;
	}
	public VeranstaltungsTypen getTyp() {
		return typ;
	}
	public void setTyp(VeranstaltungsTypen typ) {
		this.typ = typ;
	}
	public String getVeranstalter() {
		return veranstalter;
	}
	public void setVeranstalter(String veranstalter) {
		this.veranstalter = veranstalter;
	}
	public String getVeranstaltungsort() {
		return veranstaltungsort;
	}
	public void setVeranstaltungsort(String veranstaltungsort) {
		this.veranstaltungsort = veranstaltungsort;
	}
	public String getPlz_leiter() {
		return plz_leiter;
	}
	public void setPlz_leiter(String plz_leiter) {
		this.plz_leiter = plz_leiter;
	}
	public String getOrt_leiter() {
		return ort_leiter;
	}
	public void setOrt_leiter(String ort_leiter) {
		this.ort_leiter = ort_leiter;
	}
	public String getStrasse_leiter() {
		return strasse_leiter;
	}
	public void setStrasse_leiter(String strasse_leiter) {
		this.strasse_leiter = strasse_leiter;
	}
	public String getTel_nr_leiter() {
		return tel_nr_leiter;
	}
	public void setTel_nr_leiter(String tel_nr_leiter) {
		this.tel_nr_leiter = tel_nr_leiter;
	}
	public Integer getIdschau() {
		return idschau;
	}
	public void setIdschau(Integer idschau) {
		this.idschau = idschau;
	}

	
}
