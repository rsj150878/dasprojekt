package com.app.dbio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.app.auth.MitgliederListe;
import com.app.auth.Person;

public class DBPerson {
	
	public List<Person> getAllPerson() throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from person order by nachname");
		
		List <Person> resultList = new ArrayList<Person>();
		
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			Person zw = new Person();
			zw.setBio(rs.getString("zusatztext"));
			zw.setEmail(rs.getString("email"));
			zw.setEmail2(rs.getString("email2"));
			zw.setEmail3(rs.getString("email3"));
			zw.setFirstName(rs.getString("vorname"));
			zw.setGebdat(rs.getDate("geb_dat"));
			zw.setHausnummer(rs.getString("hausnummer"));
			zw.setIdPerson(rs.getInt("idperson"));
			zw.setLand(rs.getString("land"));
			zw.setLastName(rs.getString("nachname"));
			zw.setMale(rs.getString("geschlecht"));
			zw.setMobnr(rs.getString("mobnr"));
			zw.setNewsletter(rs.getString("newsletter"));
			zw.setNewsletter2(rs.getString("newsletter2"));
			zw.setNewsletter3(rs.getString("newsletter3"));
			zw.setOrt(rs.getString("ort"));
			zw.setPhone(rs.getString("telnr"));
			zw.setPlz(rs.getString("plz"));
			zw.setStrasse(rs.getString("strasse"));
			//zw.setRole(rs.getString("role"));
			zw.setTitle(rs.getString("titel"));
			zw.setWebsite(rs.getString("website"));
			resultList.add(zw);
		}
		
		return resultList;
	}
	
	public List<MitgliederListe> getMitgliederListe() throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from person order by nachname");
		
		List <MitgliederListe> resultList = new ArrayList<MitgliederListe>();
		
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			Person zw = new Person();
			zw.setBio(rs.getString("zusatztext"));
			zw.setEmail(rs.getString("email"));
			zw.setEmail2(rs.getString("email2"));
			zw.setEmail3(rs.getString("email3"));
			zw.setFirstName(rs.getString("vorname"));
			zw.setGebdat(rs.getDate("geb_dat"));
			zw.setHausnummer(rs.getString("hausnummer"));
			zw.setIdPerson(rs.getInt("idperson"));
			zw.setLand(rs.getString("land"));
			zw.setLastName(rs.getString("nachname"));
			zw.setMale(rs.getString("geschlecht"));
			zw.setMobnr(rs.getString("mobnr"));
			zw.setNewsletter(rs.getString("newsletter"));
			zw.setNewsletter2(rs.getString("newsletter2"));
			zw.setNewsletter3(rs.getString("newsletter3"));
			zw.setOrt(rs.getString("ort"));
			zw.setPhone(rs.getString("telnr"));
			zw.setPlz(rs.getString("plz"));
			zw.setStrasse(rs.getString("strasse"));
			//zw.setRole(rs.getString("role"));
			zw.setTitle(rs.getString("titel"));
			zw.setWebsite(rs.getString("website"));
			
			MitgliederListe zwl = new MitgliederListe();
			zwl.setPerson(zw);
			resultList.add(zwl);
		}
		
		return resultList;
	}
	
	
	public Person getPersonForId(Integer idPerson) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from person where idPerson = ?");
		st.setInt(1, idPerson);
		
		Person zw = null;
		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
			zw = new Person();
			zw.setBio(rs.getString("zusatztext"));
			zw.setEmail(rs.getString("email"));
			zw.setEmail2(rs.getString("email2"));
			zw.setEmail3(rs.getString("email3"));
			zw.setFirstName(rs.getString("vorname"));
			zw.setGebdat(rs.getDate("geb_dat"));
			zw.setHausnummer(rs.getString("hausnummer"));
			zw.setIdPerson(rs.getInt("idperson"));
			zw.setLand(rs.getString("land"));
			zw.setLastName(rs.getString("nachname"));
			zw.setMale(rs.getString("geschlecht"));
			zw.setMobnr(rs.getString("mobnr"));
			zw.setNewsletter(rs.getString("newsletter"));
			zw.setNewsletter2(rs.getString("newsletter2"));
			zw.setNewsletter3(rs.getString("newsletter3"));
			zw.setOrt(rs.getString("ort"));
			zw.setPhone(rs.getString("telnr"));
			zw.setPlz(rs.getString("plz"));
			zw.setStrasse(rs.getString("strasse"));
			//zw.setRole(rs.getString("role"));
			zw.setTitle(rs.getString("titel"));
			zw.setWebsite(rs.getString("website"));
		}
		
		return zw;
	}
	
	public void savePersion(Person person) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		
		if (person.getIdPerson() != null) {
			String updateString = "update person set ";
			updateString += " zusatztext = ?,";
			updateString += " email = ?,";
			updateString += " email2 = ?,";
			updateString += " email3 = ?,";
			updateString += " vorname = ?,";
			updateString += " geb_dat = ?,";
			updateString += " hausnummer = ?,";
			updateString += " land = ?,";
			updateString += " nachname = ?,";
			updateString += " geschlecht = ?,";
			updateString += " mobnr = ?,";
			updateString += " newsletter = ?,";
			updateString += " newsletter2 = ?,";
			updateString += " newsletter3 = ?,";
			updateString += " ort = ?,";
			updateString += " telnr = ?,";
			updateString += " plz = ?,";
			updateString += " strasse = ?,";
			updateString += " titel = ?,";
			updateString += " website = ?";
			updateString += " where idperson = ?";
			
			PreparedStatement upd = conn.prepareStatement(updateString);
			upd.setString(1,person.getBio());
			upd.setString(2,person.getEmail());
			upd.setString(3,person.getEmail2());
			upd.setString(4,person.getEmail3());
			upd.setString(5,person.getFirstName());
			upd.setDate(6,person.getGebdat() != null ? new java.sql.Date(person.getGebdat().getTime()):null);
			upd.setString(7,person.getHausnummer());
			upd.setString(8,person.getLand());
			upd.setString(9,person.getLastName());
			upd.setString(10,person.getMale());
			upd.setString(11,person.getMobnr());
			upd.setString(12,person.getNewsletter());
			upd.setString(13,person.getNewsletter2());
			upd.setString(14,person.getNewsletter3());
			upd.setString(15,person.getOrt());
			upd.setString(16,person.getPhone());
			upd.setString(17,person.getPlz());
			upd.setString(18,person.getStrasse());
			upd.setString(19,person.getTitle());
			upd.setString(20,person.getWebsite());
			upd.setInt(21,person.getIdPerson());
			
			upd.executeUpdate();
			
		} else {
			String insertString = "insert into person ( ";
			insertString += " zusatztext ,";
			insertString += " email  ,";
			insertString += " email2  ,";
			insertString += " email3  ,";
			insertString += " vorname  ,";
			insertString += " geb_dat  ,";
			insertString += " hausnummer  ,";
			insertString += " land  ,";
			insertString += " nachname  ,";
			insertString += " geschlecht  ,";
			insertString += " mobnr  ,";
			insertString += " newsletter  ,";
			insertString += " newsletter2  ,";
			insertString += " newsletter3  ,";
			insertString += " ort  ,";
			insertString += " telnr  ,";
			insertString += " plz  ,";
			insertString += " strasse  ,";
			insertString += " titel  ,";
			insertString += " website ) ";
			insertString += " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ";
			
			PreparedStatement ins = conn.prepareStatement(insertString);
			ins.setString(1,person.getBio());
			ins.setString(2,person.getEmail());
			ins.setString(3,person.getEmail2());
			ins.setString(4,person.getEmail3());
			ins.setString(5,person.getFirstName());
			ins.setDate(6,person.getGebdat() != null ? new java.sql.Date(person.getGebdat().getTime()):null);
			ins.setString(7,person.getHausnummer());
			ins.setString(8,person.getLand());
			ins.setString(9,person.getLastName());
			ins.setString(10,person.getMale());
			ins.setString(11,person.getMobnr());
			ins.setString(12,person.getNewsletter());
			ins.setString(13,person.getNewsletter2());
			ins.setString(14,person.getNewsletter3());
			ins.setString(15,person.getOrt());
			ins.setString(16,person.getPhone());
			ins.setString(17,person.getPlz());
			ins.setString(18,person.getStrasse());
			ins.setString(19,person.getTitle());
			ins.setString(20,person.getWebsite());
			
			ins.executeUpdate();
			
			ResultSet keys = ins.getGeneratedKeys();
			keys.next();
			person.setIdPerson(keys.getInt(1));
			
		}
		
	}
 

}
