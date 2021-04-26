package com.app.entitites;

// default package
// Generated 30.09.2019 14:19:33 by Hibernate Tools 5.4.3.Final

/**
 * ews generated by hbm2java
 */
public class ews implements java.io.Serializable {

	private int id;
	private int version;
	private int typ;
	private String ewsFuer;
	private String ewsId;
	private int idperson;
	private String zusatz;

	public ews() {
	}

	public ews(int typ, String ewsFuer, String ewsId, int idperson) {
		this.typ = typ;
		this.ewsFuer = ewsFuer;
		this.ewsId = ewsId;
		this.idperson = idperson;
	}

	public ews(int typ, String ewsFuer, String ewsId, int idperson, String zusatz) {
		this.typ = typ;
		this.ewsFuer = ewsFuer;
		this.ewsId = ewsId;
		this.idperson = idperson;
		this.zusatz = zusatz;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getTyp() {
		return this.typ;
	}

	public void setTyp(int typ) {
		this.typ = typ;
	}

	public String getEwsFuer() {
		return this.ewsFuer;
	}

	public void setEwsFuer(String ewsFuer) {
		this.ewsFuer = ewsFuer;
	}

	public String getEwsId() {
		return this.ewsId;
	}

	public void setEwsId(String ewsId) {
		this.ewsId = ewsId;
	}

	public int getIdperson() {
		return this.idperson;
	}

	public void setIdperson(int idperson) {
		this.idperson = idperson;
	}

	public String getZusatz() {
		return this.zusatz;
	}

	public void setZusatz(String zusatz) {
		this.zusatz = zusatz;
	}

}