package OERC.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import OERC.Domain.Person;
import OERC.Domain.User;

public class PersonDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6247804754246796604L;

	public User loadUserById(String userId) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		
		PreparedStatement st = conn.prepareStatement("Select * from tabMitglieder where nr = ?");

		st.setString(1, userId);
		ResultSet rs = st.executeQuery();

		User user = null;
	
		if (rs.next()) {
			user = new User();
			user.setRole("admin");
			return user;

		} else {
			throw new UsernameNotFoundException("User unbekannt");

		}
		
	}
	
	public Person getPersonForId(String idMitglieder) throws Exception {
		Person person = new Person();
		
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = conn.prepareStatement("Select * from tabMitglieder where idMitglieder = ?");

		st.setString(1,  idMitglieder);
		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
			person.setAnrede(rs.getString("anrede"));
			person.setNachname(rs.getString("nachname"));
			person.setVorname(rs.getString("vorname"));
			person.setStrasse(rs.getString("strasse"));
			person.setTitel(rs.getString("titel"));
			person.setVorname(rs.getString("vorname"));
			
			StringBuilder query = new StringBuilder();
			query.append("select * from bm_animals tiere, bm_inter_besitzer bez ");
			query.append("where tiere.id_animal = bez.id_animal ");
			query.append("and bez.id_adressen = ?");
			PreparedStatement st1 = conn.prepareStatement(query.toString());
			st1.setInt(1, rs.getInt("user_id"));
			
			ResultSet rs1 = st1.executeQuery();
		}


		
		return person;
	}
	
	public Person getPersonForHundeId(String IDHund) throws Exception {
		Person returnPerson = null;
		StringBuilder sb = new StringBuilder();
		sb.append("select * from tabMitglieder mitglied, tabHundeMitglieder zuordnung ");
		sb.append("where zuordnung.idhunde = ? ");
		sb.append(" and (besitzVon is null or (besitzVon is not null and besitzVon <= CURRENT_TIMESTAMP)) ");
		sb.append("and besitzBis is null " );
		sb.append(" and zuordnung.idMitglieder = mitglied.idMitglieder");
		
		PreparedStatement st = DBConnectionMicrosoftNeu.INSTANCE.getConnection().prepareStatement(sb.toString());
		st.setString(1, IDHund);
		
		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
			returnPerson = new Person();
			//returnPerson.setAnrede(rs.getString("Anrede"));
			returnPerson.setTitel(rs.getString("titel"));
			returnPerson.setNachname(rs.getString("nachname"));
			returnPerson.setVorname(rs.getString("vorname"));
			returnPerson.setStrasse(rs.getString("strasse"));
			returnPerson.setPlz(rs.getString("plz"));
			returnPerson.setOrt(rs.getString("ort"));
			returnPerson.setTelefon1(rs.getString("telefon1"));
			returnPerson.setTelefon2(rs.getString("telefon2"));
			
		}
		
		return returnPerson;
	}
	


}
