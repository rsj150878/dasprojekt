package com.app.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.app.dbio.DBConnectionNeu;
import com.app.dbio.DBHund;
import com.app.dbio.DBPerson;

public final class User extends AbstractPersonClass {

	private Person person;
	private String role;

	public User(String userid, String password) {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		try {

			PreparedStatement userStatement = conn.prepareStatement("select * from user where email = ?");
			userStatement.setString(1, userid);

			ResultSet userResult = userStatement.executeQuery();

			if (!userResult.next()) {
				throw new UsernameNotFoundException("User Unbekannt");
			}

			if (userResult.getString("pass").equals(password)) {

			} else {
			}

			this.setRole(userResult.getString("rollen"));

			DBPerson dbPerson = new DBPerson();
			setPerson(dbPerson.getPersonForUserId(userResult.getInt("iduser")));
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public Collection<Hund> getAllHunde() throws Exception {

		DBHund dbHund = new DBHund();
		return dbHund.getAllHundeForPerson(this.person.idPerson);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	// public void commit() throws Exception {
	// TableQuery q1 = new TableQuery("user",
	// DBConnection.INSTANCE.getConnectionPool());
	// q1.setVersionColumn("version");
	//
	// TableQuery q2 = new TableQuery("person",
	// DBConnection.INSTANCE.getConnectionPool());
	// q2.setVersionColumn("version");
	// Item userItem;
	// Item personItem;
	// SQLContainer personContainer;
	// SQLContainer userContainer;
	//
	// userContainer = new SQLContainer(q1);
	// userContainer.addContainerFilter(new Equal("iduser", this.idUser));
	//
	// userItem = userContainer.getItem(userContainer.firstItemId());
	//
	// personContainer = new SQLContainer(q2);
	// personContainer.addContainerFilter(new Equal("iduser",
	// userItem.getItemProperty("iduser").getValue()));
	//
	// personItem = personContainer.getItem(personContainer.firstItemId());
	//
	// personItem.getItemProperty("nachname").setValue(this.lastName);
	// personItem.getItemProperty("vorname").setValue(this.firstName);
	// personItem.getItemProperty("email").setValue(this.email);
	// personItem.getItemProperty("land").setValue(this.land);
	// personItem.getItemProperty("geb_dat").setValue(this.gebdat);
	// personItem.getItemProperty("newsletter").setValue(this.newsletter);
	//
	// personItem.getItemProperty("email2").setValue(this.email2);
	// personItem.getItemProperty("email3").setValue(this.email3);
	//
	// personItem.getItemProperty("newsletter2").setValue(this.newsletter2);
	// personItem.getItemProperty("newsletter3").setValue(this.newsletter3);
	//
	// personItem.getItemProperty("titel").setValue(this.title);
	// personItem.getItemProperty("geschlecht").setValue(this.male);
	//
	// personItem.getItemProperty("strasse").setValue(this.strasse);
	// personItem.getItemProperty("hausnummer").setValue(this.hausnummer);
	// personItem.getItemProperty("plz").setValue(this.plz);
	// personItem.getItemProperty("ort").setValue(this.ort);
	//
	// if (this.phone == null || this.phone.isEmpty() || phone.equals(new
	// String(""))) {
	// personItem.getItemProperty("telnr").setValue(null);
	// } else {
	// personItem.getItemProperty("telnr").setValue(this.phone);
	//
	// }
	//
	// if (this.mobnr == null || this.mobnr.isEmpty() || mobnr.equals(new
	// String(""))) {
	// personItem.getItemProperty("mobnr").setValue(null);
	// } else {
	// personItem.getItemProperty("mobnr").setValue(this.mobnr);
	//
	// }
	//
	// if (this.bio == null || this.bio.isEmpty() || bio.equals(new String(""))) {
	// personItem.getItemProperty("zusatztext").setValue(null);
	// } else {
	// personItem.getItemProperty("zusatztext").setValue(this.bio);
	//
	// }
	//
	// if (this.website == null || this.website.isEmpty() || website.equals(new
	// String(""))) {
	// personItem.getItemProperty("website").setValue(null);
	// } else {
	// personItem.getItemProperty("website").setValue(this.website);
	//
	// }
	//
	// personContainer.commit();
	// userContainer.commit();
	//
	// // personContainer.commit();
	// // userContainer.commit();
	// }

}