package com.app.dbio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.auth.User;
import com.app.enumdatatypes.Rassen;
import com.app.enumdatatypes.ShowKlassen;
import com.app.showdata.Show;
import com.app.showdata.ShowGeschlechtEnde;
import com.app.showdata.ShowHund;
import com.app.showdata.ShowKlasse;
import com.app.showdata.ShowKlasseEnde;
import com.app.showdata.ShowRing;
import com.vaadin.server.VaadinSession;

public class DBShowNeu {

	public List<Show> getShows() throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

		PreparedStatement st;
		if (user.getRole().equals("admin")) {
			st = conn.prepareStatement("select * from schau order by datum desc");
		} else {
			st = conn.prepareStatement("select * from schau where schautyp in ('I','C', 'W') order by datum desc");
		}

		List<Show> resultList = new ArrayList<Show>();

		ResultSet resultSetShow = st.executeQuery();

		while (resultSetShow.next()) {
			Show zwShow = new Show();
			zwShow.setIdSchau(Integer.valueOf(resultSetShow.getInt("idschau")));
			zwShow.setLeiter(resultSetShow.getString("leiter"));
			zwShow.setSchaubezeichnung(resultSetShow.getString("bezeichnung"));
			zwShow.setSchauDate(resultSetShow.getDate("datum"));
			zwShow.setSchauKuerzel(resultSetShow.getString("schaukuerzel"));
			zwShow.setSchauTyp(resultSetShow.getString("schautyp"));

			zwShow.setRinge(getRingeFuerShow(true, zwShow));

			resultList.add(zwShow);

		}

		return resultList;

	}

	public List<Show> getShowsForYear(Integer year) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st;
		st = conn.prepareStatement("select * from schau where schautyp in ('I','C') and year(datum) = ? order by datum desc");
		st.setInt(1, year);

		List<Show> resultList = new ArrayList<Show>();

		ResultSet resultSetShow = st.executeQuery();

		while (resultSetShow.next()) {
			Show zwShow = new Show();
			zwShow.setIdSchau(Integer.valueOf(resultSetShow.getInt("idschau")));
			zwShow.setLeiter(resultSetShow.getString("leiter"));
			zwShow.setSchaubezeichnung(resultSetShow.getString("bezeichnung"));
			zwShow.setSchauDate(resultSetShow.getDate("datum"));
			zwShow.setSchauKuerzel(resultSetShow.getString("schaukuerzel"));
			zwShow.setSchauTyp(resultSetShow.getString("schautyp"));

			zwShow.setRinge(getRingeFuerShow(true, zwShow));

			resultList.add(zwShow);

		}

		return resultList;

	}
	public List<ShowRing> getRingeFuerShow(boolean kurzversion, Show show) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = conn.prepareStatement("select * from schauring where idschau = ?");
		st.setInt(1, show.getIdSchau().intValue());

		ResultSet resultSet = st.executeQuery();

		List<ShowRing> resultList = new ArrayList<ShowRing>();
		while (resultSet.next()) {
			ShowRing zw = new ShowRing();
			zw.setRingNummer(resultSet.getString("ringnummer"));
			zw.setRichter(resultSet.getString("richter"));
			zw.setRingId(Integer.valueOf(resultSet.getInt("idschauring")));
			if (!kurzversion) {
				zw.addShowKlassen(getKlassenForShow(show, zw));
			}
			resultList.add(zw);
		}

		return resultList;

	}

	public List<ShowKlasse> getKlassenForShow(Show show, ShowRing ring) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		List<ShowKlasse> resultList = new ArrayList<>();

		if (show.getSchauTyp().equals("W")) {

			ShowKlasse zw = new ShowKlasse(Rassen.ALLGEMEIN, ShowKlassen.WESENSTEST); 
			zw.setHundeDerKlasse(getHundeForKlasse(zw, show, ring, null, null, null));
			resultList.add(zw);   

		} else {
			String state = "select rasse, geschlecht, klasse, min(sort_kat_nr) as sort"
					+ " from schauhund where idschau = ? and idschauring = ?"
					+ " group by rasse, geschlecht, klasse order by sort";
			PreparedStatement st = conn.prepareStatement(state);
			st.setInt(1, show.getIdSchau().intValue());
			st.setInt(2, ring.getRingId().intValue());

			ResultSet resultSet = st.executeQuery();
			String oldGeschlecht = "";
			String zwGeschlecht = "";
			Rassen oldRasse = null;
			Rassen zwrasse = null;

			while (resultSet.next()) {

				zwrasse = Rassen.getRasseForKurzBezeichnung(resultSet.getString("rasse"));
				zwGeschlecht = resultSet.getString("geschlecht");
				ShowKlassen zwklasse = ShowKlassen.getKlasseForKurzBezeichnung(resultSet.getString("klasse"));
				System.out.println(" zwrasse " + zwrasse.getRassenKurzBezeichnung() + " klasse "
						+ zwklasse.getShowKlassenKurzBezeichnung());

				if (oldGeschlecht.isEmpty()) {
					oldGeschlecht = resultSet.getString("geschlecht");
				}

				if (oldRasse == null) {
					oldRasse = zwrasse;
				}

				if (!oldRasse.equals(zwrasse) || !oldGeschlecht.equals(resultSet.getString("geschlecht"))) {

					ShowGeschlechtEnde ende = new ShowGeschlechtEnde();
					ende.setGeschlechtEnde(oldGeschlecht);
					ende.setRingGeschlechtEndeFor(ring);
					ende.setRasse(oldRasse);

					resultList.add(ende);
					oldGeschlecht = resultSet.getString("geschlecht");
					oldRasse = zwrasse;

				}

				ShowKlasse zw = new ShowKlasse(zwrasse, zwklasse);
				zw.setHundeDerKlasse(
						getHundeForKlasse(zw, show, ring, resultSet.getString("klasse"), zwrasse, zwGeschlecht));
				resultList.add(zw);

			}

			System.out.println("rasse in aufbau " + zwrasse);
			System.out.println("geschlecht in aufbau " + oldGeschlecht);
			ShowGeschlechtEnde ende = new ShowGeschlechtEnde();
			ende.setGeschlechtEnde(zwGeschlecht);
			ende.setRasse(zwrasse);
			ende.setRingGeschlechtEndeFor(ring);

			resultList.add(ende);

		}
		return resultList;
	}

	public List<ShowRing> getHundeForKlasse(ShowKlasse zwKlasse, Show show, ShowRing ring, String klasse, Rassen rasse,
			String geschlecht) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;

		if (show.getSchauTyp().equals("W")) {
			st = conn.prepareStatement(
					"select * from schauhund where idschau = ? and idschauring = ? " + " order by sort_kat_nr");
			st.setInt(1, show.getIdSchau().intValue());
			st.setInt(2, ring.getRingId().intValue());
		} else {
			st = conn.prepareStatement(
					"select * from schauhund where idschau = ? and idschauring = ? and klasse = ? and rasse = ? and geschlecht = ? "
							+ " order by sort_kat_nr");
			st.setInt(1, show.getIdSchau().intValue());
			st.setInt(2, ring.getRingId().intValue());
			st.setString(3, klasse);
			st.setString(4, rasse.getRassenKurzBezeichnung());
			st.setString(5, geschlecht);

		}
		List<ShowRing> resultList = new ArrayList<>();
		ResultSet resultSet = st.executeQuery();
		while (resultSet.next()) {

			ShowHund zw = new ShowHund();
			zw.setKatalognumer(resultSet.getString("katalognummer"));
			zw.setShowHundName(resultSet.getString("name"));
			zw.setIdschauhund(Integer.valueOf(resultSet.getInt("idschauhund")));
			zw.setWurftag(resultSet.getDate("wurftag"));
			zw.setZuchtbuchnummer(resultSet.getString("zuchtbuchnummer"));
			zw.setChipnummer(resultSet.getString("chipnummer"));
			zw.setVater(resultSet.getString("vater"));
			;
			zw.setMutter(resultSet.getString("mutter"));
			zw.setBesitzershow(resultSet.getString("besitzershow"));
			zw.setBewertung(resultSet.getString("bewertung"));
			zw.setHundfehlt(resultSet.getString("hundfehlt"));
			zw.setFormwert(resultSet.getString("formwert"));
			zw.setPlatzierung(resultSet.getString("platzierung"));
			zw.setCACA(resultSet.getString("CACA"));
			zw.setCACIB(resultSet.getString("CACIB"));
			zw.setBOB(resultSet.getString("BOB"));
			zw.setGeschlecht(resultSet.getString("geschlecht"));
			zw.setClubsieger(resultSet.getString("clubsieger"));
			zw.setSort_kat_nr(new Integer(resultSet.getInt("sort_kat_nr")));
			zw.setRingId(ring.getRingId());
			ShowKlassen zwklasse = ShowKlassen.getKlasseForKurzBezeichnung(resultSet.getString("klasse"));
			zw.setKlasse(zwklasse);
			zw.setRasse(Rassen.getRasseForKurzBezeichnung(resultSet.getString("rasse")));
			zw.setRingNummer(ring.getRingNummer());
			zw.setRichter(ring.getRichter());
			zw.setMitglied(resultSet.getString("mitglied"));
			zw.setVeroeffentlichen(resultSet.getBoolean("veroeffentlichen"));
			zw.setBesitzerEmail(resultSet.getString("besitzeremail"));
			zw.setZuechter(resultSet.getString("zuechter"));
			resultList.add(zw);

		}

		ShowKlasseEnde showKlasseEnde = new ShowKlasseEnde(zwKlasse);
		resultList.add(showKlasseEnde);

		return resultList;
	}

	public List<ShowHund> getHundeForKlasse(Show show, ShowKlassen klasse, Rassen rasse, String geschlecht)
			throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;

		st = conn.prepareStatement(
				"select * from schauhund where idschau = ?  and klasse = ? and rasse = ? and geschlecht = ? "
						+ " order by sort_kat_nr");
		st.setInt(1, show.getIdSchau().intValue());
		st.setString(2, klasse.getShowKlassenKurzBezeichnung());
		st.setString(3, rasse.getRassenKurzBezeichnung());
		st.setString(4, geschlecht);

		List<ShowHund> resultList = new ArrayList<>();
		ResultSet resultSet = st.executeQuery();
		while (resultSet.next()) {

			ShowHund zw = new ShowHund();
			zw.setKatalognumer(resultSet.getString("katalognummer"));
			zw.setShowHundName(resultSet.getString("name"));
			zw.setIdschauhund(Integer.valueOf(resultSet.getInt("idschauhund")));
			zw.setWurftag(resultSet.getDate("wurftag"));
			zw.setZuchtbuchnummer(resultSet.getString("zuchtbuchnummer"));
			zw.setChipnummer(resultSet.getString("chipnummer"));
			zw.setVater(resultSet.getString("vater"));
			zw.setMutter(resultSet.getString("mutter"));
			zw.setBesitzershow(resultSet.getString("besitzershow"));
			zw.setBewertung(resultSet.getString("bewertung"));
			zw.setHundfehlt(resultSet.getString("hundfehlt"));
			zw.setFormwert(resultSet.getString("formwert"));
			zw.setPlatzierung(resultSet.getString("platzierung"));
			zw.setCACA(resultSet.getString("CACA"));
			zw.setCACIB(resultSet.getString("CACIB"));
			zw.setBOB(resultSet.getString("BOB"));
			zw.setGeschlecht(resultSet.getString("geschlecht"));
			zw.setClubsieger(resultSet.getString("clubsieger"));
			zw.setSort_kat_nr(new Integer(resultSet.getInt("sort_kat_nr")));
			ShowKlassen zwklasse = ShowKlassen.getKlasseForKurzBezeichnung(resultSet.getString("klasse"));
			zw.setKlasse(zwklasse);
			zw.setRasse(Rassen.getRasseForKurzBezeichnung(resultSet.getString("rasse")));
			zw.setMitglied(resultSet.getString("mitglied"));
			zw.setVeroeffentlichen(resultSet.getBoolean("veroeffentlichen"));
			zw.setBesitzerEmail(resultSet.getString("besitzeremail"));
			zw.setZuechter(resultSet.getString("zuechter"));
			
			
			resultList.add(zw);

		}

		return resultList;
	}

	public List<String> getRichterForRasse(Show show, Rassen rasse) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;

		st = conn.prepareStatement("select distinct richter from schauring where idschau = ? "
				+ "and idschauring in (select idschauring from schauhund where idschau = ? and rasse = ?)");
		st.setInt(1, show.getIdSchau().intValue());
		st.setInt(2, show.getIdSchau().intValue());
		st.setString(3, rasse.getRassenKurzBezeichnung());
		
		
		List<String> resultList = new ArrayList<>();
		ResultSet resultSet = st.executeQuery();
		while (resultSet.next()) {
			resultList.add(resultSet.getString("richter"));

		}

		return resultList;

	}

	public void updateShowHund(ShowHund updateHund) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("update schauhund set bewertung = ?, formwert = ?, ");
		sb.append("hundfehlt = ?, platzierung = ?, ");
		sb.append("CACA = ?, CACIB = ?, BOB = ?, clubsieger = ?");
		sb.append(", name = ?, wurftag = ?, zuchtbuchnummer = ?, katalognummer =?");
		sb.append(",  rasse = ?, vater = ?, mutter = ?, ");
		sb.append("besitzershow = ?, geschlecht = ?, sort_kat_nr = ?, idschauring = ?, chipnummer = ?, veroeffentlichen=?,besitzeremail=?");
		sb.append(" where idschauhund = ?");
		PreparedStatement st = conn.prepareStatement(sb.toString());
		st.setString(1, updateHund.getBewertung());
		st.setString(2, updateHund.getFormwert());
		st.setString(3, updateHund.getHundfehlt());
		st.setString(4, updateHund.getPlatzierung());
		st.setString(5, updateHund.getCACA());
		st.setString(6, updateHund.getCACIB());
		st.setString(7, updateHund.getBOB());
		st.setString(8, updateHund.getClubsieger());
		st.setString(9, updateHund.getShowHundName());
		st.setDate(10, new java.sql.Date(updateHund.getWurftag().getTime()));
		st.setString(11, updateHund.getZuchtbuchnummer());
		st.setString(12, updateHund.getKatalognumer());
		st.setString(13, updateHund.getRasse().getRassenKurzBezeichnung());
		st.setString(14, updateHund.getVater());
		st.setString(15, updateHund.getMutter());
		st.setString(16, updateHund.getBesitzershow());
		st.setString(17, updateHund.getGeschlecht());
		st.setInt(18, updateHund.getSort_kat_nr().intValue());
		st.setInt(19, updateHund.getRingId().intValue());
		st.setString(20,updateHund.getChipnummer());
		st.setBoolean(21,updateHund.getVeroeffentlichen());
		st.setString(22,updateHund.getBesitzerEmail());
		
		st.setInt(23, updateHund.getIdschauhund().intValue());

		System.out.println("update showhund " + updateHund.getIdschauhund());

		st.executeUpdate();

	}

	public Show getShowForVeranstaltung(Integer veranstaltungId) throws Exception {
		Show result = new Show();

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from schau where id_veranstaltung = ? ");

		PreparedStatement st = conn.prepareStatement(sb.toString());
		st.setInt(1, veranstaltungId);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			result.setIdSchau(rs.getInt("idschau"));
			result.setSchaubezeichnung(rs.getString("bezeichnung"));
			result.setSchauDate(rs.getDate("datum"));
			result.setSchauKuerzel(rs.getString("schaukuerzel"));
			result.setSchauTyp(rs.getString("schautyp"));
		} else {
			StringBuilder insertSb = new StringBuilder();
			insertSb.append(
					"insert into schau (version, bezeichnung, schautyp, datum, schaukuerzel, id_veranstaltung)");
			insertSb.append(" values (0, '', '', curdate(), '', ?)");
			PreparedStatement insertSt = conn.prepareStatement(insertSb.toString(), Statement.RETURN_GENERATED_KEYS);
			insertSt.setInt(1, veranstaltungId);
			insertSt.executeUpdate();

			ResultSet keys = insertSt.getGeneratedKeys();
			keys.next();
			result.setIdSchau(keys.getInt(1));

		}

		return result;
	}

	public void updateShow(Show show) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("update schau set bezeichnung = ?, schautyp = ?, datum = ?, schaukuerzel = ? ");
		sb.append("where idschau = ?");
		PreparedStatement st = conn.prepareStatement(sb.toString());

		st.setString(1, show.getSchaubezeichnung());
		st.setString(2, show.getSchauTyp());
		st.setDate(3, new java.sql.Date(show.getSchauDate().getTime()));
		st.setString(4, show.getSchauKuerzel());
		st.setInt(5, show.getIdSchau());

		st.executeUpdate();

	}

	public ShowRing getShowRing(Integer schauId, String ringnummer) throws Exception {
		ShowRing result = new ShowRing();

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from schauring where idschau = ? and ringnummer = ? ");

		PreparedStatement st = conn.prepareStatement(sb.toString());
		st.setInt(1, schauId);
		st.setString(2, ringnummer);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			result.setRichter(rs.getString("richter"));
			result.setRingId(rs.getInt("idschauring"));
			result.setRingNummer(rs.getString("ringnummer"));
		} else {
			StringBuilder insertSb = new StringBuilder();
			insertSb.append("insert into schauring (version, richter, idschau, ringnummer)");
			insertSb.append(" values (0, '', ?, ?)");
			PreparedStatement insertSt = conn.prepareStatement(insertSb.toString(), Statement.RETURN_GENERATED_KEYS);
			insertSt.setInt(1, schauId);
			insertSt.setString(2, ringnummer);
			insertSt.executeUpdate();

			ResultSet keys = insertSt.getGeneratedKeys();
			keys.next();
			result.setRingId(keys.getInt(1));

		}

		return result;
	}

	public ShowHund getShowHundForVeranstaltung(Integer idSchau, ShowRing ring, Integer idTeilnehmer) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st = conn.prepareStatement("select * from schauhund where idschau = ? and id_teilnehmer = ?");
		st.setInt(1, idSchau.intValue());
		st.setInt(2, idTeilnehmer);

		ResultSet resultSet = st.executeQuery();
		ShowHund result = new ShowHund();
		if (resultSet.next()) {

			result.setKatalognumer(resultSet.getString("katalognummer"));
			result.setShowHundName(resultSet.getString("name"));
			result.setIdschauhund(Integer.valueOf(resultSet.getInt("idschauhund")));
			result.setWurftag(resultSet.getDate("wurftag"));
			result.setZuchtbuchnummer(resultSet.getString("zuchtbuchnummer"));
			result.setChipnummer(resultSet.getString("chipnummer"));
			result.setVater(resultSet.getString("vater"));
			result.setMutter(resultSet.getString("mutter"));
			result.setBesitzershow(resultSet.getString("besitzershow"));
			result.setBewertung(resultSet.getString("bewertung"));
			result.setHundfehlt(resultSet.getString("hundfehlt"));
			result.setFormwert(resultSet.getString("formwert"));
			result.setPlatzierung(resultSet.getString("platzierung"));
			result.setCACA(resultSet.getString("CACA"));
			result.setCACIB(resultSet.getString("CACIB"));
			result.setBOB(resultSet.getString("BOB"));
			result.setGeschlecht(resultSet.getString("geschlecht"));
			result.setClubsieger(resultSet.getString("clubsieger"));
			result.setSort_kat_nr(new Integer(resultSet.getInt("sort_kat_nr")));
			ShowKlassen zwklasse = ShowKlassen.getKlasseForKurzBezeichnung(resultSet.getString("klasse"));
			result.setKlasse(zwklasse);
			result.setRasse(Rassen.getRasseForKurzBezeichnung(resultSet.getString("rasse")));
			result.setRingNummer(ring.getRingNummer());
			result.setRichter(ring.getRichter());
			result.setVeroeffentlichen(resultSet.getBoolean("veroeffentlichen"));
			result.setBesitzerEmail(resultSet.getString("besitzeremail"));
			result.setZuechter(resultSet.getString("zuechter"));

		} else {
			StringBuilder insertSb = new StringBuilder();
			insertSb.append("insert into schauhund (version, idschauring, name, wurftag, ");
			insertSb.append("zuchtbuchnummer, chipnummer, katalognummer, klasse, rasse, vater, mutter, ");
			insertSb.append("besitzershow, idschau, geschlecht, sort_kat_nr,id_teilnehmer)");
			insertSb.append(" values (0, ?, '',curdate(), '', '','0','WT', '','','','',?,'',0, ?)");

			PreparedStatement insertSt = conn.prepareStatement(insertSb.toString(), Statement.RETURN_GENERATED_KEYS);
			insertSt.setInt(2, idSchau);
			insertSt.setInt(1, ring.getRingId());
			insertSt.setInt(3, idTeilnehmer);
			insertSt.executeUpdate();

			ResultSet keys = insertSt.getGeneratedKeys();
			keys.next();
			result.setIdschauhund(keys.getInt(1));

		}

		return result;
	}

}
