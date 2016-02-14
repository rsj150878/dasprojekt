package com.app.Auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.dbIO.DBConnection;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

@SuppressWarnings({ "serial", "unchecked" })
public class Person {

	private String role;

	private Integer idPerson;
	private String firstName;
	private String lastName;
	private String title;
	private String male;
	private Date gebdat;
	private String email;
	private String strasse;
	private String hausnummer;
	private String plz;
	private String ort;
	private String land;
	private String phone;
	private String mobnr;
	private String newsletter;
	private String website;
	private String bio;

	private String email2;
	private String newsletter2;
	private String email3;
	private String newsletter3;

	public Person(Integer idPerson) {
		TableQuery q2 = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item personItem;
		SQLContainer personContainer;

		try {

			personContainer = new SQLContainer(q2);
			personContainer.addContainerFilter(new Equal("idperson", idPerson));

			personItem = personContainer.getItem(personContainer.firstItemId());

			this.role = "admin";

			this.idPerson = Integer.valueOf(personItem
					.getItemProperty("idperson").getValue().toString());

			this.email = personItem.getItemProperty("email").getValue() == null ? null
					: personItem.getItemProperty("email").getValue().toString();
			this.firstName = personItem.getItemProperty("vorname").getValue()
					.toString();
			this.lastName = personItem.getItemProperty("nachname").getValue()
					.toString();
			this.land = personItem.getItemProperty("land").getValue()
					.toString();
			this.strasse = personItem.getItemProperty("strasse").getValue()
					.toString();
			this.hausnummer = personItem.getItemProperty("hausnummer")
					.getValue().toString();
			this.plz = personItem.getItemProperty("plz").getValue().toString();
			this.ort = personItem.getItemProperty("ort").getValue().toString();

			this.gebdat = (Date) personItem.getItemProperty("geb_dat")
					.getValue();

			this.newsletter = personItem.getItemProperty("newsletter")
					.getValue() == null ? "N" : personItem
					.getItemProperty("newsletter").getValue().toString();

			this.newsletter2 = personItem.getItemProperty("newsletter2")
					.getValue() == null ? "N" : personItem
					.getItemProperty("newsletter2").getValue().toString();
			this.newsletter3 = personItem.getItemProperty("newsletter3")
					.getValue() == null ? "N" : personItem
					.getItemProperty("newsletter3").getValue().toString();

			this.email2 = personItem.getItemProperty("email2").getValue() == null ? null
					: personItem.getItemProperty("email2").getValue()
							.toString();
			this.email2 = personItem.getItemProperty("email3").getValue() == null ? null
					: personItem.getItemProperty("email3").getValue()
							.toString();

			this.title = personItem.getItemProperty("titel").getValue() == null ? ""
					: personItem.getItemProperty("titel").getValue().toString();
			this.male = personItem.getItemProperty("geschlecht").getValue() == null ? "M"
					: personItem.getItemProperty("geschlecht").getValue()
							.toString();

			this.phone = personItem.getItemProperty("telnr").getValue() == null ? null
					: personItem.getItemProperty("telnr").getValue().toString();
			this.mobnr = personItem.getItemProperty("mobnr").getValue() == null ? null
					: personItem.getItemProperty("mobnr").getValue().toString();

			this.bio = personItem.getItemProperty("zusatztext").getValue() == null ? null
					: personItem.getItemProperty("zusatztext").getValue()
							.toString();

			this.website = personItem.getItemProperty("website").getValue() == null ? null
					: personItem.getItemProperty("website").getValue()
							.toString();

		} catch (SQLException e) {
			e.printStackTrace();

		}

	}
	
	public Collection<Hund> getAllHunde() {
		Collection<Hund> hundeCollection = new ArrayList<Hund>();
		
		TableQuery q1 = new TableQuery("hund",DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");
		SQLContainer hundeContainer;
		try {
			hundeContainer = new SQLContainer(q1);
			hundeContainer.addContainerFilter(new Equal ("idperson",this.idPerson));
			
			for (int i = 0; i < hundeContainer.size(); i++) {
				Item item = hundeContainer.getItem(hundeContainer.getIdByIndex(i));
				final Hund hund = new Hund(this.idPerson, Integer.valueOf(item.getItemProperty("idhund").getValue().toString()));
				hundeCollection.add(hund);
				
			}
			
 		} catch (SQLException e) {
 			
 		}
		
		
		return hundeCollection;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(Integer idPerson) {
		this.idPerson = idPerson;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMale() {
		return male;
	}

	public void setMale(String male) {
		this.male = male;
	}

	public Date getGebdat() {
		return gebdat;
	}

	public void setGebdat(Date gebdat) {
		this.gebdat = gebdat;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
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

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobnr() {
		return mobnr;
	}

	public void setMobnr(String mobnr) {
		this.mobnr = mobnr;
	}

	public String getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(String newsletter) {
		this.newsletter = newsletter;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getNewsletter2() {
		return newsletter2;
	}

	public void setNewsletter2(String newsletter2) {
		this.newsletter2 = newsletter2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getNewsletter3() {
		return newsletter3;
	}

	public void setNewsletter3(String newsletter3) {
		this.newsletter3 = newsletter3;
	}

}
