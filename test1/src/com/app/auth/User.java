package com.app.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.dbio.DBConnection;
import com.app.dbio.DBConnectionNeu;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.util.filter.Compare.Equal;
import com.vaadin.v7.data.util.sqlcontainer.SQLContainer;
import com.vaadin.v7.data.util.sqlcontainer.query.TableQuery;

@SuppressWarnings({ "serial", "unchecked" })
public final class User extends AbstractPersonClass {

	public User(String userid, String password) {
		
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		try {

			PreparedStatement userStatement = conn.prepareStatement("select * from user where email = ?");
			userStatement.setString(1, userid);

			ResultSet userResult = userStatement.executeQuery();

			if (!userResult.next()) {
				throw new UsernameNotFoundException("User Unbekannt");
			}

		
			if (userResult.getString("password").equals(password)) {

			} else {
			}

			this.role = userResult.getString("rollen");
			
			PreparedStatement personStatement = conn.prepareStatement("select * from person where iduser = ?");
			personStatement.setInt(1, userResult.getInt("iduser"));
			
			ResultSet personResult = personStatement.executeQuery();
			
			personResult.next();
			this.idPerson = personResult.getInt("idperson");

			this.email = personResult.getString("email");
			
			this.firstName = personResult.getString("vorname");
			this.lastName = personResult.getString("nachname");
			this.land = personResult.getString("land");
			this.strasse = personResult.getString("strasse");
			this.hausnummer = personResult.getString("hausnummer");
			this.plz = personResult.getString("plz");
			this.ort = personResult.getString("ort");

			this.idUser = personResult.getInt("iduser");

			this.gebdat = personResult.getDate("geb_dat");

			this.newsletter = personResult.getString("newsletter");

			this.newsletter2 = personResult.getString("newsletter2");
			this.newsletter3 = personResult.getString("newsletter3");

			this.email2 = personResult.getString("email2");
			this.email2 = personResult.getString("email3");

			this.title = personResult.getString("titel");
			this.male =personResult.getString("geschlecht");

			this.phone = personResult.getString("telnr");
			this.mobnr = personResult.getString("mobnr");

			this.bio = personResult.getString("zusatztext");

			this.website = personResult.getString("website");

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