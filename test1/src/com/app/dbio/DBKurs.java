package com.app.dbio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.app.kurs.Kurs;
import com.app.kurs.KursStunde;
import com.app.kurs.KursTag;
import com.app.kurs.KursTeilnehmer;

public class DBKurs {

	public List<Kurs> getAllKurse() throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from kurs "
				// + "where datum >= curdate() "
				+ "order by idkurs desc");

		List<Kurs> resultList = new ArrayList<Kurs>();

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			Kurs zw = new Kurs();
			zw.setIdKurs(rs.getInt("idkurs"));
			zw.setVersion(rs.getInt("version"));
			zw.setKursBezeichnung(rs.getString("kursbezeichnung"));
			zw.setStartDat(rs.getDate("startdat"));
			zw.setEndeDat(rs.getDate("endedat"));
			zw.setPreisOerc1Hund(rs.getDouble("oerc_1_hund"));
			zw.setPreisOerc2Hund(rs.getDouble("oerc_2_hund"));
			zw.setPreisNoerc1Hund(rs.getDouble("noerc_1_hund"));
			zw.setPreisNoerc2Hund(rs.getDouble("noerc_2_hund"));
			resultList.add(zw);
		}

		return resultList;
	}

	public void saveKurs(Kurs saveItem) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;

		if (saveItem.getIdKurs() == null) {
			st = conn.prepareStatement(
					"insert into kurs (version,kursbezeichnung, startdat, endedat, oerc_1_hund, oerc_2_hund,"
							+ " noerc_1_hund, noerc_2_hund) value (?,?,?,?,?,?,?,?) ");

			st.setObject(1, saveItem.getVersion(), Types.INTEGER);
			st.setObject(2, saveItem.getKursBezeichnung(), Types.CHAR);
			st.setObject(3, saveItem.getStartDat(), Types.DATE);
			st.setObject(4, saveItem.getEndeDat(), Types.DATE);
			st.setObject(5, saveItem.getPreisOerc1Hund(), Types.DOUBLE);
			st.setObject(6, saveItem.getPreisOerc2Hund(), Types.DOUBLE);

			st.setObject(7, saveItem.getPreisNoerc1Hund(), Types.DOUBLE);
			st.setObject(8, saveItem.getPreisNoerc2Hund(), Types.DOUBLE);

		} else {

			st = conn.prepareStatement(
					"update kurs set version =? ,kursbezeichnung=?, startdat=?, endedat=?, oerc_1_hund=?, oerc_2_hund=?,"
							+ " noerc_1_hund =?, noerc_2_hund=? where idkurs =? ");

			st.setObject(1, saveItem.getVersion(), Types.INTEGER);
			st.setObject(2, saveItem.getKursBezeichnung(), Types.CHAR);
			st.setObject(3, saveItem.getStartDat(), Types.DATE);
			st.setObject(4, saveItem.getEndeDat(), Types.DATE);
			st.setObject(5, saveItem.getPreisOerc1Hund(), Types.DOUBLE);
			st.setObject(6, saveItem.getPreisOerc2Hund(), Types.DOUBLE);

			st.setObject(7, saveItem.getPreisNoerc1Hund(), Types.DOUBLE);
			st.setObject(8, saveItem.getPreisNoerc2Hund(), Types.DOUBLE);
			st.setObject(9, saveItem.getIdKurs(), Types.INTEGER);

		}

		st.executeUpdate();

		ResultSet keySet = st.getGeneratedKeys();
		if (keySet.next()) {
			saveItem.setIdKurs(keySet.getInt(1));
		}
	}
	
	public List<KursTag> getKursTageZuKurs(Kurs kurs) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from kurstag "
				+ "where idkurs = ? "
				+ "order by idkurstag desc");

		st.setObject(1, kurs.getIdKurs(), Types.INTEGER);
		
		List<KursTag> resultList = new ArrayList<KursTag>();

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			KursTag zw = new KursTag();
			zw.setIdKurs(rs.getInt("idkurs"));
			zw.setVersion(rs.getInt("version"));
			zw.setBezeichnung(rs.getString("bezeichnung"));
			zw.setIdKursTag(rs.getInt("idkurstag"));
			resultList.add(zw);
		}

		return resultList;
	}
	
	public List<KursStunde> getKursStundenZuKursTag(KursTag kursTag) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from kursstunde "
				+ "where idkurstag = ? "
				+ "order by idkursstunde desc");

		st.setObject(1, kursTag.getIdKursTag(), Types.INTEGER);
		
		List<KursStunde> resultList = new ArrayList<KursStunde>();

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			KursStunde zw = new KursStunde();
			zw.setIdKursStunde(rs.getInt("idkursstunde"));
			zw.setVersion(rs.getInt("version"));
			zw.setBezeichnung(rs.getString("bezeichnung"));
			zw.setIdKursTag(rs.getInt("idkurstag"));
			zw.setStartZeit(rs.getTime("startzeit"));
			zw.setEndZeit(rs.getTime("endzeit"));
			resultList.add(zw);
		}

		return resultList;
	}

	public List<KursTeilnehmer> getKursTeilnehmerZuKursStunde(KursStunde kursStunde) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from kursteilnehmer "
				+ "where idkursstunde = ? "
				+ "order by idkursteilnehmer desc");

		st.setObject(1, kursStunde.getIdKursStunde(), Types.INTEGER);
		DBHund dbHund = new DBHund();
		
		List<KursTeilnehmer> resultList = new ArrayList<KursTeilnehmer>();

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			KursTeilnehmer zw = new KursTeilnehmer();
			zw.setIdKursStunde(rs.getInt("idkursstunde"));
			zw.setVersion(rs.getInt("version"));
			zw.setIdKursTeilnehmer(rs.getInt("idkursteilnehmer"));
			zw.setIdHund(rs.getInt("idhund"));
			zw.setAbweichenderHundeFuehrer(rs.getString("abwfuehrer"));
			zw.setBezahlt(rs.getString("kursbezahlt"));
			
			zw.setHund(dbHund.getHundForHundId(zw.getIdHund()));
			resultList.add(zw);
		}

		return resultList;
	}
	
	public void saveKursTeilnehmer(KursTeilnehmer saveItem) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		
		if (saveItem.getIdKursTeilnehmer() == null) {
			st = conn.prepareStatement("insert into kursteilnehmer (version, idkursstunde, idhund, abwfuehrer, kursbezahlt) "
					+ "values (?,?,?,?,?)");
			
			st.setObject(1,saveItem.getVersion(), Types.INTEGER);
			st.setObject(2, saveItem.getIdKursStunde(), Types.INTEGER);
			st.setObject(3, saveItem.getIdHund(), Types.INTEGER);
			st.setObject(4, saveItem.getAbweichenderHundeFuehrer(), Types.CHAR);
			st.setObject(5, saveItem.getBezahlt(), Types.CHAR);
			
		} else
		{
			st = conn.prepareStatement("update kursteilnehmer set version =?, idkursstunde=?, idhund=?, abwfuehrer=?, kursbezahlt=? "
					+ "where idkursteilnehmer = ?");
			
			st.setObject(1,saveItem.getVersion(), Types.INTEGER);
			st.setObject(2, saveItem.getIdKursStunde(), Types.INTEGER);
			st.setObject(3, saveItem.getIdHund(), Types.INTEGER);
			st.setObject(4, saveItem.getAbweichenderHundeFuehrer(), Types.CHAR);
			st.setObject(5, saveItem.getBezahlt(), Types.CHAR);
			st.setObject(6, saveItem.getIdKursTeilnehmer(), Types.INTEGER);
					
		}
		st.executeUpdate();

		ResultSet keySet = st.getGeneratedKeys();
		if (keySet.next()) {
			saveItem.setIdKursTeilnehmer(keySet.getInt(1));
		}
	}
	
	public void deleteKursTeilnehmer(KursTeilnehmer delItem) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("delete from kursteilnehmer "
				+ "where idkursteilnehmer = ? ");

		st.setObject(1, delItem.getIdKursTeilnehmer(), Types.INTEGER);
		st.executeUpdate();
		
		
	}

}
