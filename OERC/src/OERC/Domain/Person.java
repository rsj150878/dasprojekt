package OERC.Domain;

import java.util.List;

public class Person {
	
	private String anrede;
	private String titel;
	private String vorname;
	private String nachname;
	private String strasse;
	private String plz;
	private String ort;
	private String telefon1;
	private String telefon2;
	
	
	private List<Hund> hunde;
	
	
	public String getAnrede() {
		return anrede;
	}
	public void setAnrede(String anrede) {
		this.anrede = anrede;
	}
	public String getTitel() {
		return titel;
	}
	public void setTitel(String titel) {
		this.titel = titel;
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getStrasse() {
		return strasse;
	}
	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}
	public List<Hund> getHunde() {
		return hunde;
	}
	public void setHunde(List<Hund> hunde) {
		this.hunde = hunde;
	}
	public String getPlz() {
		return plz;
	}
	public void setPlz(String plz) {
		this.plz = plz;
	}
	public String getOrt() {
		return ort;
	}
	public void setOrt(String ort) {
		this.ort = ort;
	}
	public String getTelefon1() {
		return telefon1;
	}
	public void setTelefon1(String telefon1) {
		this.telefon1 = telefon1;
	}
	public String getTelefon2() {
		return telefon2;
	}
	public void setTelefon2(String telefon2) {
		this.telefon2 = telefon2;
	}
	
	
	

}
