package com.app.dbio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.auth.Hund;

public class DBHund {

	public List<Hund> getAllHundeForPerson(Integer idPerson) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from hund where idPerson = ?");
		st.setInt(1, idPerson);

		List<Hund> resultList = new ArrayList<Hund>();

		ResultSet hundResult = st.executeQuery();
		while (hundResult.next()) {
			Hund hund = new Hund();

			hund.setZwingername(hundResult.getString("zwingername"));
			hund.setRufname(hundResult.getString("rufname"));
			hund.setWurfdatum(hundResult.getDate("wurfdatum"));
			hund.setRasse(hundResult.getString("rasse"));
			hund.setChipnummer(hundResult.getString("chipnummer"));
			hund.setBhdatum(hundResult.getDate("bhdatum"));
			hund.setFarbe(hundResult.getString("farbe"));
			hund.setZuechter(hundResult.getString("zuechter"));
			hund.setGeschlecht(hundResult.getString("geschlecht"));
			hund.setZuchtbuchnummer(hundResult.getString("zuchtbuchnummer"));
			hund.setIdhund(hundResult.getInt("idhund"));
			hund.setIdperson(hundResult.getInt("idPerson"));
			resultList.add(hund);
		}

		return resultList;

	}

	public void saveHund(Hund hund) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		if (hund.getIdhund() != null) {
			PreparedStatement upd = conn.prepareStatement(
					"update hund set bhdatum = ?, chipnummer = ?, farbe = ?, geschlecht = ?, rasse = ?, rufname = ?, wurfdatum = ?, zuchtbuchnummer = ?, zuechter = ?, zwingername = ? where idhund = ?");
			upd.setDate(1, hund.getBhdatum() != null ? new java.sql.Date(hund.getBhdatum().getTime()) : null);
			upd.setString(2, hund.getChipnummer());
			upd.setString(3, hund.getFarbe());
			upd.setString(4, hund.getGeschlecht());
			upd.setString(5, hund.getRasse());
			upd.setString(6, hund.getRufname());
			upd.setDate(7, new java.sql.Date(hund.getWurfdatum().getTime()));
			upd.setString(8, hund.getZuchtbuchnummer());
			upd.setString(9, hund.getZuechter());
			upd.setString(10, hund.getZwingername());
			upd.setInt(11, hund.getIdhund());

			upd.executeUpdate();
		} else {
			PreparedStatement ins = conn.prepareStatement(
					"insert into hund (bhdatum, chipnummer, farbe, geschlecht, rasse, rufname, wurfdatum, zuchtbuchnummer, zuechter, zwingername, idperson)"
							+ "values (?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ins.setDate(1, hund.getBhdatum() != null ? new java.sql.Date(hund.getBhdatum().getTime()) : null);
			ins.setString(2, hund.getChipnummer());
			ins.setString(3, hund.getFarbe());
			ins.setString(4, hund.getGeschlecht());
			ins.setString(5, hund.getRasse());
			ins.setString(6, hund.getRufname());
			ins.setDate(7, hund.getWurfdatum() != null ? new java.sql.Date(hund.getWurfdatum().getTime()) : null);
			ins.setString(8, hund.getZuchtbuchnummer());
			ins.setString(9, hund.getZuechter());
			ins.setString(10, hund.getZwingername());
			ins.setInt(11, hund.getIdperson());

			ins.executeUpdate();

			ResultSet keys = ins.getGeneratedKeys();
			keys.next();
			hund.setIdhund(keys.getInt(1));
		}

	}

}
