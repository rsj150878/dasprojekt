package com.app.auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.dbio.DBConnection;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

@SuppressWarnings({ "serial", "unchecked" })
public final class User extends AbstractPersonClass {

	public User(String userid, String password) {
		TableQuery q1 = new TableQuery("user", DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		TableQuery q2 = new TableQuery("person", DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item userItem;
		Item personItem;
		SQLContainer personContainer;
		SQLContainer userContainer;

		try {

			userContainer = new SQLContainer(q1);
			userContainer.addContainerFilter(new Equal("email", userid));

			if (userContainer.size() == 0) {
				throw new UsernameNotFoundException("User Unbekannt");
			}

			userItem = userContainer.getItem(userContainer.firstItemId());

			if (userItem.getItemProperty("password").getValue().toString().equals(password)) {

			} else {
			}

			this.role = userItem.getItemProperty("rollen").getValue().toString();
			personContainer = new SQLContainer(q2);
			personContainer.addContainerFilter(new Equal("iduser", userItem.getItemProperty("iduser").getValue()));

			personItem = personContainer.getItem(personContainer.firstItemId());

			this.idPerson = Integer.valueOf(personItem.getItemProperty("idperson").getValue().toString());

			this.email = personItem.getItemProperty("email").getValue() == null ? null
					: personItem.getItemProperty("email").getValue().toString();
			this.firstName = personItem.getItemProperty("vorname").getValue().toString();
			this.lastName = personItem.getItemProperty("nachname").getValue().toString();
			this.land = personItem.getItemProperty("land").getValue().toString();
			this.strasse = personItem.getItemProperty("strasse").getValue().toString();
			this.hausnummer = personItem.getItemProperty("hausnummer").getValue().toString();
			this.plz = personItem.getItemProperty("plz").getValue().toString();
			this.ort = personItem.getItemProperty("ort").getValue().toString();

			this.idUser = Integer.valueOf(userItem.getItemProperty("iduser").getValue().toString());

			this.gebdat = (Date) personItem.getItemProperty("geb_dat").getValue();

			this.newsletter = personItem.getItemProperty("newsletter").getValue() == null ? "N"
					: personItem.getItemProperty("newsletter").getValue().toString();

			this.newsletter2 = personItem.getItemProperty("newsletter2").getValue() == null ? "N"
					: personItem.getItemProperty("newsletter2").getValue().toString();
			this.newsletter3 = personItem.getItemProperty("newsletter3").getValue() == null ? "N"
					: personItem.getItemProperty("newsletter3").getValue().toString();

			this.email2 = personItem.getItemProperty("email2").getValue() == null ? null
					: personItem.getItemProperty("email2").getValue().toString();
			this.email2 = personItem.getItemProperty("email3").getValue() == null ? null
					: personItem.getItemProperty("email3").getValue().toString();

			this.title = personItem.getItemProperty("titel").getValue() == null ? ""
					: personItem.getItemProperty("titel").getValue().toString();
			this.male = personItem.getItemProperty("geschlecht").getValue() == null ? "M"
					: personItem.getItemProperty("geschlecht").getValue().toString();

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

		TableQuery q1 = new TableQuery("hund", DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");
		SQLContainer hundeContainer;
		try {
			hundeContainer = new SQLContainer(q1);
			hundeContainer.addContainerFilter(new Equal("idperson", this.idPerson));

			for (int i = 0; i < hundeContainer.size(); i++) {
				Item item = hundeContainer.getItem(hundeContainer.getIdByIndex(i));
				final Hund hund = new Hund(this.idPerson,
						Integer.valueOf(item.getItemProperty("idhund").getValue().toString()));
				hundeCollection.add(hund);

			}

		} catch (SQLException e) {

		}

		return hundeCollection;
	}

	public void commit() throws Exception {
		TableQuery q1 = new TableQuery("user", DBConnection.INSTANCE.getConnectionPool());
		q1.setVersionColumn("version");

		TableQuery q2 = new TableQuery("person", DBConnection.INSTANCE.getConnectionPool());
		q2.setVersionColumn("version");
		Item userItem;
		Item personItem;
		SQLContainer personContainer;
		SQLContainer userContainer;

		userContainer = new SQLContainer(q1);
		userContainer.addContainerFilter(new Equal("iduser", this.idUser));

		userItem = userContainer.getItem(userContainer.firstItemId());

		personContainer = new SQLContainer(q2);
		personContainer.addContainerFilter(new Equal("iduser", userItem.getItemProperty("iduser").getValue()));

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

}