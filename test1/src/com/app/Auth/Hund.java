package com.app.Auth;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.app.dbIO.DBConnection;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class Hund implements QueryDelegate.RowIdChangeListener {

	private Integer idhund;
	private Integer idperson;
	private String zwingername;
	private String rufname;
	private Date wurfdatum;
	private String rasse;
	private Date bhdatum;
	private String zuchtbuchnummer;
	private String chipnummer;
	private String zuechter;
	private String farbe;
	private String geschlecht;

	// legt einen neuen hund an
	public Hund(Integer idperson) {

		TableQuery q1 = new TableQuery("hund",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		Item hundItem;
		SQLContainer hundContainer;

		try {
			hundContainer = new SQLContainer(q1);
			hundContainer.addRowIdChangeListener(this);
			hundItem = hundContainer.getItem(hundContainer.addItem());

			hundItem.getItemProperty("idperson").setValue(idperson);
			hundItem.getItemProperty("zwingername").setValue("neu");
			;
			hundItem.getItemProperty("rufname").setValue("neu");
			hundItem.getItemProperty("wurfdatum").setValue(new Date());
			hundItem.getItemProperty("rasse").setValue("GR");
			hundItem.getItemProperty("chipnummer").setValue("0000000000000000");
			hundItem.getItemProperty("geschlecht").setValue("R");

			hundContainer.commit();

			this.rufname = "Neu";
			this.rasse = "GR";
			this.geschlecht = "R";
			this.chipnummer = "0000000000000000";
			this.wurfdatum = new Date();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Hund(Integer idperson, Integer idhund) {

		TableQuery q1 = new TableQuery("hund",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		Item hundItem;
		SQLContainer hundContainer;

		try {
			hundContainer = new SQLContainer(q1);
			hundContainer.addContainerFilter(new Equal("idhund", idhund));
			hundItem = hundContainer.getItem(hundContainer.firstItemId());

			this.bhdatum = hundItem.getItemProperty("bh_datum").getValue() == null ? null
					: (Date) hundItem.getItemProperty("bh_datum").getValue();
			
			this.chipnummer = hundItem.getItemProperty("chipnummer").getValue().toString();
			this.farbe = hundItem.getItemProperty("farbe").getValue() == null ? null : hundItem.getItemProperty("farbe").getValue().toString();
			this.geschlecht = hundItem.getItemProperty("geschlecht").getValue() == null ? "R": hundItem.getItemProperty("geschlecht").getValue().toString();
			this.idhund = Integer.valueOf(hundItem.getItemProperty("idhund").getValue().toString());
			this.rasse = hundItem.getItemProperty("rasse").getValue() == null ? "GR": hundItem.getItemProperty("rasse").getValue().toString();
			this.rufname = hundItem.getItemProperty("rufname").getValue().toString();
			this.wurfdatum = (Date) hundItem.getItemProperty("wurfdatum").getValue();
			this.zuchtbuchnummer = hundItem.getItemProperty("zuchtbuchnummer").getValue() == null ? null: hundItem.getItemProperty("zuchtbuchnummer").getValue().toString();
			this.zuechter = hundItem.getItemProperty("zuechter").getValue() == null ? null: hundItem.getItemProperty("zuechter").getValue().toString();
			this.zwingername = hundItem.getItemProperty("zwingername").getValue() == null ? null: hundItem.getItemProperty("zwingername").getValue().toString();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void commit() throws Exception {
		TableQuery q1 = new TableQuery("hund",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		Item hundItem;
		SQLContainer hundContainer;

		hundContainer = new SQLContainer(q1);
		hundContainer.addContainerFilter(new Equal("idhund", this.idhund));

		hundItem = hundContainer.getItem(hundContainer.firstItemId());

		hundItem.getItemProperty("zwingername").setValue(this.zwingername);
		hundItem.getItemProperty("rufname").setValue(this.rufname);
		hundItem.getItemProperty("wurfdatum").setValue(this.wurfdatum);
		hundItem.getItemProperty("rasse").setValue(this.rasse);
		hundItem.getItemProperty("chipnummer").setValue(this.chipnummer);
		hundItem.getItemProperty("bh_datum").setValue(this.bhdatum);
		hundItem.getItemProperty("farbe").setValue(this.farbe);
		hundItem.getItemProperty("zuechter").setValue(this.zuechter);
		hundItem.getItemProperty("geschlecht").setValue(this.geschlecht);
		hundItem.getItemProperty("zuchtbuchnummer").setValue(
				this.zuchtbuchnummer);

		hundContainer.commit();

	}

	@Override
	public void rowIdChange(RowIdChangeEvent arg0) {
		RowId x = arg0.getNewRowId();
		Long newID = (Long) x.getId()[0];

		this.idhund = new Integer(newID.intValue());

	}

	public Integer getIdhund() {
		return idhund;
	}

	public void setIdhund(Integer idhund) {
		this.idhund = idhund;
	}

	public Integer getIdperson() {
		return idperson;
	}

	public void setIdperson(Integer idperson) {
		this.idperson = idperson;
	}

	public String getZwingername() {
		return zwingername;
	}

	public void setZwingername(String zwingername) {
		this.zwingername = zwingername;
	}

	public String getRufname() {
		return rufname;
	}

	public void setRufname(String rufname) {
		this.rufname = rufname;
	}

	public Date getWurfdatum() {
		return wurfdatum;
	}

	public void setWurfdatum(Date wurfdatum) {
		this.wurfdatum = wurfdatum;
	}

	public String getRasse() {
		return rasse;
	}

	public void setRasse(String rasse) {
		this.rasse = rasse;
	}

	public Date getBhdatum() {
		return bhdatum;
	}

	public void setBhdatum(Date bhdatum) {
		this.bhdatum = bhdatum;
	}

	public String getZuchtbuchnummer() {
		return zuchtbuchnummer;
	}

	public void setZuchtbuchnummer(String zuchtbuchnummer) {
		this.zuchtbuchnummer = zuchtbuchnummer;
	}

	public String getChipnummer() {
		return chipnummer;
	}

	public void setChipnummer(String chipnummer) {
		this.chipnummer = chipnummer;
	}

	public String getZuechter() {
		return zuechter;
	}

	public void setZuechter(String zuechter) {
		this.zuechter = zuechter;
	}

	public String getFarbe() {
		return farbe;
	}

	public void setFarbe(String farbe) {
		this.farbe = farbe;
	}

	public String getGeschlecht() {
		return geschlecht;
	}

	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}

}
