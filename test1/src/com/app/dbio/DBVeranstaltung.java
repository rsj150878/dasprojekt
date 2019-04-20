package com.app.dbio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.app.enumdatatypes.VeranstaltungsTypen;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsStufe;
import com.app.veranstaltung.VeranstaltungsTeilnehmer;

public class DBVeranstaltung {

	public List<Veranstaltung> getAllAktiveVeranstaltungen() throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from veranstaltung "
				// + "where datum >= curdate() "
				+ "order by id_veranstaltung desc");

		List<Veranstaltung> resultList = new ArrayList<Veranstaltung>();

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			Veranstaltung zw = new Veranstaltung();
			zw.setId_veranstaltung(rs.getInt("id_veranstaltung"));
			zw.setVersion(rs.getInt("version"));
			zw.setName(rs.getString("name"));
			zw.setDatum(rs.getDate("datum"));
			zw.setRichter(rs.getString("richter"));
			zw.setVeranstaltungsleiter(rs.getString("veranstaltungsleiter"));
			zw.setTyp(VeranstaltungsTypen.getVeranstaltungsTypForId(rs.getInt("typ")));
			zw.setVeranstalter(rs.getString("veranstalter"));
			zw.setVeranstaltungsort(rs.getString("veranstaltungsort"));
			zw.setPlz_leiter(rs.getString("plz_leiter"));
			zw.setOrt_leiter(rs.getString("ort_leiter"));
			zw.setStrasse_leiter(rs.getString("strasse_leiter"));
			zw.setTel_nr_leiter(rs.getString("tel_nr_leiter"));
			zw.setIdschau(rs.getInt("idschau"));

			resultList.add(zw);
		}

		return resultList;
	}

	public VeranstaltungsTeilnehmer getTeilnehmerZuStartnr(Veranstaltung suchVa, Integer startNr) throws Exception {
		VeranstaltungsTeilnehmer resultTeilnehmer = null;
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st;
		st = conn.prepareStatement("select * from veranstaltungs_teilnehmer "
				// + "where datum >= curdate() "
				+ "where id_veranstaltung = ? " + "and startnr = ? ");
		
		st.setInt(1, suchVa.getId_veranstaltung());
		st.setInt(2, startNr);

		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
			resultTeilnehmer = new VeranstaltungsTeilnehmer();
			resultTeilnehmer.setIdTeilnehmer(rs.getInt("id_teilnehmer"));
			resultTeilnehmer.setVersion(rs.getInt("version"));
			resultTeilnehmer.setIdVeranstaltung(rs.getInt("id_veranstaltung"));
			resultTeilnehmer.setIdStufe(rs.getInt("id_stufe"));
			resultTeilnehmer.setIdHund(rs.getInt("id_hund"));
			resultTeilnehmer.setIdPerson(rs.getInt("id_person"));
			resultTeilnehmer.setBezahlt(rs.getString("bezahlt"));
			resultTeilnehmer.setBestanden(rs.getString("bestanden"));
			resultTeilnehmer.setGesPunkte(rs.getInt("ges_punkte"));
			resultTeilnehmer.setHundefuehrer(rs.getString("hundefuehrer"));
			resultTeilnehmer.setUebung1(rs.getInt("uebung1"));
			resultTeilnehmer.setUebung2(rs.getInt("uebung2"));
			resultTeilnehmer.setUebung3(rs.getInt("uebung3"));
			resultTeilnehmer.setUebung4(rs.getInt("uebung4"));
			resultTeilnehmer.setUebung5(rs.getInt("uebung5"));
			resultTeilnehmer.setUebung6(rs.getInt("uebung6"));
			resultTeilnehmer.setUebung7(rs.getInt("uebung7"));
			resultTeilnehmer.setUebung8(rs.getInt("uebung8"));
			resultTeilnehmer.setSonderWertung(rs.getString("sonderwertung"));
			resultTeilnehmer.setBemerkung(rs.getString("bemerkung"));
			resultTeilnehmer.setGruppe(rs.getInt("gruppe"));
			resultTeilnehmer.setFormwert(rs.getString("formwert"));
			resultTeilnehmer.setPlatzierung(rs.getInt("platzierung"));
			resultTeilnehmer.setStartnr(rs.getInt("startnr"));
			
		}

		return resultTeilnehmer;
	}
	
	public VeranstaltungsStufe getStufeZuId(Integer id) throws Exception {
		VeranstaltungsStufe result = null;
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st;
		st = conn.prepareStatement("select * from veranstaltungs_stufe "
				// + "where datum >= curdate() "
				+ "where id_stufe = ? ");
		
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		
		if (rs.next()) {
			result = new VeranstaltungsStufe();
			result.setIdStufe(rs.getInt("id_stufe"));
			result.setIdVeranstaltung(rs.getInt("id_veranstaltung"));
			result.setRichter(rs.getString("richter"));
			result.setStufenTyp(rs.getInt("stufen_typ"));
			result.setVeranstaltungsLeiter(rs.getString("veranstaltungs_leiter"));
			result.setVersion(rs.getInt("version"));
			result.setDatum(rs.getString("datum"));
		}
				
		
		return result;
		
	}
	
	public void saveTeilnehmer(VeranstaltungsTeilnehmer saveItem) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = null;
	
		if (saveItem.getIdTeilnehmer() != null) {
			st = conn.prepareStatement("update veranstaltungs_teilnehmer " +
					" set bezahlt = ?" +
					", bestanden = ? " +
					", ges_punkte = ? " +
					", hundefuehrer = ? " +
					", uebung1 = ? " +
					", uebung2 = ? " +
					", uebung3 = ? " +
					", uebung4 = ? " +
					", uebung5 = ? " +
					", uebung6 = ? " +
					", uebung7 = ? " +
					", uebung8 = ? " +
					", sonderwertung = ? " +
					", bemerkung = ? " +
					", gruppe = ? " +
					", formwert = ? " +
					", platzierung = ? " +
					", startnr = ? " +
					
					"where id_teilnehmer = ? ");
			
			st.setString(1, saveItem.getBezahlt());
			st.setString(2,  saveItem.getBestanden());
			st.setInt(3, saveItem.getGesPunkte());
			st.setString(4, saveItem.getHundefuehrer());
			st.setInt(5, saveItem.getUebung1());
			st.setInt(6, saveItem.getUebung2());
			st.setInt(7, saveItem.getUebung3());
			st.setInt(8, saveItem.getUebung4());
			st.setInt(9, saveItem.getUebung5());
			st.setInt(10, saveItem.getUebung6());
			st.setInt(11, saveItem.getUebung7());
			st.setInt(12, saveItem.getUebung8());
			st.setString(13, saveItem.getSonderWertung());
			st.setString(14, saveItem.getBemerkung());
			st.setInt(15, saveItem.getGruppe());
			st.setString(16, saveItem.getFormwert());
			st.setInt(17,saveItem.getPlatzierung());
			st.setInt(18, saveItem.getStartnr());
			st.setInt(19, saveItem.getIdTeilnehmer());

		}
		
		st.executeUpdate();
		
		ResultSet keySet =  st.getGeneratedKeys();
        if (keySet.next()) {
            saveItem.setIdTeilnehmer(keySet.getInt(1));
        }
		
	}

}
