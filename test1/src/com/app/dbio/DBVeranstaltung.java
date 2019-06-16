package com.app.dbio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.app.enumdatatypes.VeranstaltungsStufen;
import com.app.enumdatatypes.VeranstaltungsTypen;
import com.app.veranstaltung.Veranstaltung;
import com.app.veranstaltung.VeranstaltungsRichter;
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
			
			zw.setVaRichter(this.getRichterList(zw.getId_veranstaltung()));

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
			result.setStufenTyp(VeranstaltungsStufen.getBezeichnungForId(rs.getInt("stufen_typ")));
			result.setVeranstaltungsLeiter(rs.getString("veranstaltungs_leiter"));
			result.setVersion(rs.getInt("version"));
			result.setDatum(rs.getDate("datum"));
		}

		return result;

	}

	public void saveTeilnehmer(VeranstaltungsTeilnehmer saveItem) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = null;

		if (saveItem.getIdTeilnehmer() != null) {
			st = conn.prepareStatement("update veranstaltungs_teilnehmer " + " set bezahlt = ?" + ", bestanden = ? "
					+ ", ges_punkte = ? " + ", hundefuehrer = ? " + ", uebung1 = ? " + ", uebung2 = ? "
					+ ", uebung3 = ? " + ", uebung4 = ? " + ", uebung5 = ? " + ", uebung6 = ? " + ", uebung7 = ? "
					+ ", uebung8 = ? " + ", sonderwertung = ? " + ", bemerkung = ? " + ", gruppe = ? "
					+ ", formwert = ? " + ", platzierung = ? " + ", startnr = ? " +

					"where id_teilnehmer = ? ");

			st.setString(1, saveItem.getBezahlt());
			st.setString(2, saveItem.getBestanden());
			st.setObject(3, saveItem.getGesPunkte(), Types.INTEGER);
			st.setString(4, saveItem.getHundefuehrer());
			st.setObject(5, saveItem.getUebung1(), Types.INTEGER);
			st.setObject(6, saveItem.getUebung2(), Types.INTEGER);
			st.setObject(7, saveItem.getUebung3(), Types.INTEGER);
			st.setObject(8, saveItem.getUebung4(), Types.INTEGER);
			st.setObject(9, saveItem.getUebung5(), Types.INTEGER);
			st.setObject(10, saveItem.getUebung6(), Types.INTEGER);
			st.setObject(11, saveItem.getUebung7(), Types.INTEGER);
			st.setObject(12, saveItem.getUebung8(), Types.INTEGER);
			st.setString(13, saveItem.getSonderWertung());
			st.setString(14, saveItem.getBemerkung());
			st.setObject(15, saveItem.getGruppe(), Types.INTEGER);
			st.setString(16, saveItem.getFormwert());
			st.setObject(17, saveItem.getPlatzierung(), Types.INTEGER);
			st.setObject(18, saveItem.getStartnr(), Types.INTEGER);
			st.setInt(19, saveItem.getIdTeilnehmer());

		} else {
			st = conn.prepareStatement("insert into veranstaltungs_teilnehmer "
					+ " (version, id_veranstaltung, id_stufe, id_hund, id_person, bezahlt) " + "values (?,?,?,?,?,?)");
			st.setInt(1, 0);
			st.setInt(2, saveItem.getIdVeranstaltung());
			st.setInt(3, saveItem.getIdStufe());
			st.setInt(4, saveItem.getIdHund());
			st.setInt(5, saveItem.getIdPerson());
			st.setString(6, saveItem.getBezahlt());

		}

		st.executeUpdate();

		ResultSet keySet = st.getGeneratedKeys();
		if (keySet.next()) {
			saveItem.setIdTeilnehmer(keySet.getInt(1));
		}

	}

	public List<VeranstaltungsStufe> getStufenZuVaId(Integer vaId) throws Exception {
		List<VeranstaltungsStufe> resultList = new ArrayList<>();

		PreparedStatement st = DBConnectionNeu.INSTANCE.getConnection()
				.prepareStatement("select * from veranstaltungs_stufe where id_veranstaltung = ?");
		st.setInt(1, vaId);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			VeranstaltungsStufe stufe = new VeranstaltungsStufe();
			stufe.setIdStufe(rs.getInt("id_stufe"));
			stufe.setIdVeranstaltung(rs.getInt("id_veranstaltung"));
			stufe.setRichter(rs.getString("richter"));
			stufe.setStufenTyp(VeranstaltungsStufen.getBezeichnungForId(rs.getInt("stufen_typ")));
			stufe.setVeranstaltungsLeiter(rs.getString("veranstaltungs_leiter"));
			stufe.setVersion(rs.getInt("version"));
			stufe.setDatum(rs.getDate("datum"));
			resultList.add(stufe);
		}

		return resultList;
	}

	public List<VeranstaltungsTeilnehmer> getAlleTeilnehmerZuStufe(Integer idStufe) throws Exception {
		List<VeranstaltungsTeilnehmer> resultList = new ArrayList<>();
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		DBPerson dbPErson = new DBPerson();
		DBHund dbHund = new DBHund();

		PreparedStatement st;
		st = conn.prepareStatement("select * from veranstaltungs_teilnehmer "
				// + "where datum >= curdate() "
				+ "where id_stufe = ? order by platzierung asc, startnr asc");

		st.setInt(1, idStufe);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			VeranstaltungsTeilnehmer resultTeilnehmer = new VeranstaltungsTeilnehmer();
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

			resultTeilnehmer.setHund(dbHund.getHundForHundId(resultTeilnehmer.getIdHund()));
			resultTeilnehmer.setPerson(dbPErson.getPersonForId(resultTeilnehmer.getIdPerson()));

			resultList.add(resultTeilnehmer);

		}

		return resultList;
	}
	
	public List<VeranstaltungsTeilnehmer> getAlleTeilnehmerZuVeranstaltung(Integer idVeranstaltung) throws Exception {
		List<VeranstaltungsTeilnehmer> resultList = new ArrayList<>();
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		DBPerson dbPErson = new DBPerson();
		DBHund dbHund = new DBHund();

		PreparedStatement st;
		st = conn.prepareStatement("select * from veranstaltungs_teilnehmer "
				// + "where datum >= curdate() "
				+ "where id_veranstaltung = ? order by startnr");

		st.setInt(1, idVeranstaltung);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			VeranstaltungsTeilnehmer resultTeilnehmer = new VeranstaltungsTeilnehmer();
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

			resultTeilnehmer.setHund(dbHund.getHundForHundId(resultTeilnehmer.getIdHund()));
			resultTeilnehmer.setPerson(dbPErson.getPersonForId(resultTeilnehmer.getIdPerson()));

			resultList.add(resultTeilnehmer);

		}

		return resultList;
	}


	public void deleteTeilnehmer(VeranstaltungsTeilnehmer teilnehmer) throws Exception {
		PreparedStatement st = DBConnectionNeu.INSTANCE.getConnection()
				.prepareStatement("delete from veranstaltungs_teilnehmer where id_teilnehmer = ?");
		st.setInt(1, teilnehmer.getIdTeilnehmer());

		st.executeUpdate();

	}

	public void saveVeranstaltung(Veranstaltung saveItem) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = null;

		if (saveItem.getId_veranstaltung() == null) {
			st = conn.prepareStatement(
					"insert into veranstaltung (version, name, datum, richter, veranstaltungsleiter, typ, veranstalter,  "
							+ "veranstaltungsort, plz_leiter, ort_leiter, strasse_leiter, tel_nr_leiter) values (?,?,?,?,?,?,?,?,?,?,?,?)");
			st.setInt(1, 0);
			st.setString(2, saveItem.getName());
			//st.setDate(3, saveItem.getDatum() != null ? new java.sql.Date(saveItem.getDatum().getTime()) : null);
			st.setObject(3, saveItem.getDatum(),Types.DATE);

			st.setString(4, saveItem.getRichter());
			st.setString(5, saveItem.getVeranstaltungsleiter());
			st.setInt(6, saveItem.getTyp().getVeranstaltungsTypID());
			st.setString(7, saveItem.getVeranstalter());
			st.setString(8, saveItem.getVeranstaltungsort());
			st.setString(9, saveItem.getPlz_leiter());
			st.setString(10, saveItem.getOrt_leiter());
			st.setString(11, saveItem.getStrasse_leiter());
			st.setString(12, saveItem.getTel_nr_leiter());
			// st.setInt(13, saveItem.getIdschau() != null ?
			// saveItem.getIdschau() :null);
		} else {

			st = conn.prepareStatement(
					"update veranstaltung  set name = ?, datum = ?, richter =?, veranstaltungsleiter =?, typ =?, veranstalter =?,  "
							+ "veranstaltungsort = ?, plz_leiter = ?, ort_leiter = ?, strasse_leiter =?, tel_nr_leiter =?, idschau=? "
							+ " where id_veranstaltung = ?");

			st.setString(1, saveItem.getName());
			st.setObject(2, saveItem.getDatum(),Types.DATE);

			st.setString(3, saveItem.getRichter());
			st.setString(4, saveItem.getVeranstaltungsleiter());
			st.setInt(5, saveItem.getTyp().getVeranstaltungsTypID());
			st.setString(6, saveItem.getVeranstalter());
			st.setString(7, saveItem.getVeranstaltungsort());
			st.setString(8, saveItem.getPlz_leiter());
			st.setString(9, saveItem.getOrt_leiter());
			st.setString(10, saveItem.getStrasse_leiter());
			st.setString(11, saveItem.getTel_nr_leiter());
			st.setObject(12, saveItem.getIdschau(),Types.INTEGER);

			st.setObject(13, saveItem.getId_veranstaltung(),Types.INTEGER);

		}

		st.executeUpdate();

		ResultSet keySet = st.getGeneratedKeys();
		if (keySet.next()) {
			saveItem.setId_veranstaltung(keySet.getInt(1));
		}

	}

	public void saveVeranstaltungStufe(VeranstaltungsStufe saveItem) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = null;

		if (saveItem.getIdStufe() == null) {
			st = conn.prepareStatement("insert into veranstaltungs_stufe(version, id_veranstaltung, stufen_typ,"
					+ "richter, veranstaltungs_leiter, datum)" + "values (?,?,?,?,?,?)");
			st.setInt(1, 0);
			st.setInt(2, saveItem.getIdVeranstaltung());
			st.setInt(3, saveItem.getStufenTyp().getVeranstaltungsStufeId());
			st.setString(4, saveItem.getRichter());
			st.setString(5, saveItem.getVeranstaltungsLeiter());
			st.setDate(6, saveItem.getDatum() != null ? new java.sql.Date(saveItem.getDatum().getTime()) : null);

		} else {
			st = conn.prepareStatement("update veranstaltungs_stufe "
					+ " set richter = ?, veranstaltungs_leiter =?, datum = ? " + " where id_stufe = ?");
			st.setString(1, saveItem.getRichter());
			st.setString(2, saveItem.getVeranstaltungsLeiter());
			st.setDate(3, saveItem.getDatum() != null ? new java.sql.Date(saveItem.getDatum().getTime()) : null);
			st.setInt(4, saveItem.getIdStufe());
		}

		st.executeUpdate();

		ResultSet keySet = st.getGeneratedKeys();
		if (keySet.next()) {
			saveItem.setIdStufe(keySet.getInt(1));
		}

	}

	public void saveRichter(VeranstaltungsRichter saveRichter) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = null;

		if (saveRichter.getIdRichter() == null) {
			st = conn.prepareStatement(
					"insert into veranstaltungs_richter (lfd_nr, richter_name, id_veranstaltung)" + " values (?,?,?)");
			st.setObject(1,saveRichter.getLfdNummer(), Types.INTEGER);
			st.setString(2, saveRichter.getRichterName());
			st.setObject(3, saveRichter.getIdVeranstaltung(), Types.INTEGER);
		} else {
			st = conn.prepareStatement("update veranstaltungs_richter set richter_name = ? where id_richter = ?");
			st.setString(1, saveRichter.getRichterName());
			st.setInt(2, saveRichter.getIdRichter());
		}
		
		st.executeUpdate();
		ResultSet keySet = st.getGeneratedKeys();
		if (keySet.next()) {
			saveRichter.setIdRichter(keySet.getInt(1));
		}

	}
	
	public List<VeranstaltungsRichter> getRichterList(Integer idVeranstaltung) throws Exception {
		Connection conn =  DBConnectionNeu.INSTANCE.getConnection();
		ArrayList<VeranstaltungsRichter> result = new ArrayList<>();
		PreparedStatement st = conn.prepareStatement("select * from veranstaltungs_richter where id_veranstaltung = ?");
		st.setInt(1, idVeranstaltung);
		
		ResultSet rs = st.executeQuery();
		
		while (rs.next()) {
			VeranstaltungsRichter zw = new VeranstaltungsRichter();
			zw.setIdRichter(rs.getInt("id_richter"));
			zw.setIdVeranstaltung(rs.getInt("id_veranstaltung"));
			zw.setLfdNummer(rs.getInt("lfd_nr"));
			zw.setRichterName(rs.getString("richter_name"));
			
			result.add(zw);
		}
		
		return result;
	}

}
