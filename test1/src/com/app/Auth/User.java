package com.app.Auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.dbIO.DBConnection;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

@SuppressWarnings({ "serial", "unchecked" })
public final class User {
	private String role;
	private Integer idUser;
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

	public User(String userid, String password) {
		TableQuery q1 = new TableQuery("user",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		TableQuery q2 = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item userItem;
		Item personItem;
		SQLContainer personContainer;
		SQLContainer userContainer;

		try {

			userContainer = new SQLContainer(q1);
			userContainer.addContainerFilter(new Equal("email", userid));

			System.out.println("userid: " + userid);
			if (userContainer.size() == 0) {
				throw new UsernameNotFoundException("User Unbekannt");
			}

			userItem = userContainer.getItem(userContainer.firstItemId());

			if (userItem.getItemProperty("password").getValue().toString()
					.equals(password)) {

			} else {
			}
			System.out.println("user");

			personContainer = new SQLContainer(q2);
			personContainer.addContainerFilter(new Equal("iduser", userItem
					.getItemProperty("iduser").getValue()));

			personItem = personContainer.getItem(personContainer.firstItemId());

			this.role = "admin";
			
			this.idPerson = Integer.valueOf(personItem.getItemProperty("idperson").getValue().toString());

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

			this.idUser = Integer.valueOf(userItem.getItemProperty("iduser")
					.getValue().toString());

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
					: personItem.getItemProperty("zusatztext").getValue().toString();

			this.website = personItem.getItemProperty("website").getValue() == null ? null
					: personItem.getItemProperty("website").getValue().toString();

			
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

	public void commit() throws Exception {
		TableQuery q1 = new TableQuery("user",
				DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		TableQuery q2 = new TableQuery("person",
				DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item userItem;
		Item personItem;
		SQLContainer personContainer;
		SQLContainer userContainer;

		userContainer = new SQLContainer(q1);
		userContainer.addContainerFilter(new Equal("iduser", this.idUser));

		userItem = userContainer.getItem(userContainer.firstItemId());

		personContainer = new SQLContainer(q2);
		personContainer.addContainerFilter(new Equal("iduser", userItem
				.getItemProperty("iduser").getValue()));

		personItem = personContainer.getItem(personContainer.firstItemId());

		personItem.getItemProperty("nachname").setValue(this.lastName);
		personItem.getItemProperty("vorname").setValue(this.firstName);
		personItem.getItemProperty("email").setValue(this.email);
		personItem.getItemProperty("land").setValue(this.land);
		personItem.getItemProperty("geb_dat").setValue(this.gebdat);
		personItem.getItemProperty("newsletter").setValue(this.newsletter);

		personItem.getItemProperty("email2").setValue(this.email2);
		personItem.getItemProperty("email3").setValue(this.email3);

		personItem.getItemProperty("newsletter2").setValue(this.newsletter2);
		personItem.getItemProperty("newsletter3").setValue(this.newsletter3);

		personItem.getItemProperty("titel").setValue(this.title);
		personItem.getItemProperty("geschlecht").setValue(this.male);

		personItem.getItemProperty("strasse").setValue(this.strasse);
		personItem.getItemProperty("hausnummer").setValue(this.hausnummer);
		personItem.getItemProperty("plz").setValue(this.plz);
		personItem.getItemProperty("ort").setValue(this.ort);

		if (this.phone == null || this.phone.isEmpty() || phone.equals(new String(""))) {
			personItem.getItemProperty("telnr").setValue(null);
		} else {
			personItem.getItemProperty("telnr").setValue(this.phone);

		}

		if (this.mobnr == null || this.mobnr.isEmpty() || mobnr.equals(new String(""))) {
			personItem.getItemProperty("mobnr").setValue(null);
		} else {
			personItem.getItemProperty("mobnr").setValue(this.mobnr);

		}
		
		if (this.bio == null || this.bio.isEmpty() || bio.equals(new String(""))) {
			personItem.getItemProperty("zusatztext").setValue(null);
		} else {
			personItem.getItemProperty("zusatztext").setValue(this.bio);

		}
		
		if (this.website == null || this.website.isEmpty() || website.equals(new String(""))) {
			personItem.getItemProperty("website").setValue(null);
		} else {
			personItem.getItemProperty("website").setValue(this.website);

		}



		personContainer.commit();
		userContainer.commit();

		// personContainer.commit();
		// userContainer.commit();
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getEmail2() {
		return this.email2;
	}

	public void setEmail2(final String email2) {
		this.email2 = email2;
	}

	public String getEmail3() {
		return this.email3;
	}

	public void setEmail3(final String email3) {
		this.email3 = email3;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getMobnr() {
		return mobnr;
	}

	public void setMobnr(final String mobnr) {
		this.mobnr = mobnr;
	}

	public String getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(final String newsletter) {
		this.newsletter = newsletter;
	}

	public String getNewsletter2() {
		return newsletter2;
	}

	public void setNewsletter2(final String newsletter2) {
		this.newsletter2 = newsletter2;
	}

	public String getNewsletter3() {
		return newsletter3;
	}

	public void setNewsletter3(final String newsletter3) {
		this.newsletter3 = newsletter3;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(final String website) {
		this.website = website;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(final String bio) {
		this.bio = bio;
	}

	public String getMale() {
		return male;
	}

	public void setMale(final String male) {
		this.male = male;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getRole() {
		return role;
	}

	public void setRole(final String role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getLand() {
		System.out.println("bin in getter land");
		return this.land;
	}

	public void setLand(final String land) {
		this.land = land;
	}

	public Date getgebdat() {
		return this.gebdat;
	}

	public void setgebdat(final Date gebdat) {
		this.gebdat = gebdat;

	}

	public String getstrasse() {
		return this.strasse;
	}

	public void setstrasse(final String strasse) {
		this.strasse = strasse;
	}

	public String gethausnummer() {
		return this.hausnummer;
	}

	public void sethausnummer(final String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getort() {
		return this.ort;
	}

	public void setort(final String ort) {
		this.ort = ort;
	}

	public String getplz() {
		return this.plz;
	}

	public void setplz(final String plz) {
		this.plz = plz;
	}
	
	public Integer getIduser() {
		return this.idUser;
	}
	
	public Integer getIdperson() {
		return this.idPerson;
	}

}