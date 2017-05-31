package com.app.dbIO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.app.Auth.User;
import com.app.enumPackage.Rassen;
import com.app.enumPackage.ShowKlassen;
import com.app.showData.Show;
import com.app.showData.ShowGeschlechtEnde;
import com.app.showData.ShowHund;
import com.app.showData.ShowKlasse;
import com.app.showData.ShowKlasseEnde;
import com.app.showData.ShowRing;
import com.vaadin.server.VaadinSession;

public class DBShowNeu {

	public List<Show> getShows() throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		
		User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		
		PreparedStatement st;
		if (user.getRole().equals("admin")) {
			st = conn.prepareStatement("select * from schau order by datum desc");
		} else  {
			st = conn.prepareStatement("select * from schau where schautyp = 'C' order by datum desc");
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

			zwShow.setRinge(getRingeFuerShow(true, zwShow.getIdSchau()));

			resultList.add(zwShow);

		}

		return resultList;

	}

	public List<ShowRing> getRingeFuerShow(boolean kurzversion, Integer idSchau) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = conn.prepareStatement("select * from schauring where idschau = ?");
		st.setInt(1, idSchau.intValue());

		ResultSet resultSet = st.executeQuery();

		List<ShowRing> resultList = new ArrayList<ShowRing>();
		while (resultSet.next()) {
			ShowRing zw = new ShowRing();
			zw.setRingNummer(resultSet.getString("ringnummer"));
			zw.setRichter(resultSet.getString("richter"));
			zw.setRingId(Integer.valueOf(resultSet.getInt("idschauring")));
			if (!kurzversion) {
				zw.addShowKlassen(getKlassenForShow(zw, idSchau, zw));
			}
			resultList.add(zw);
		}

		return resultList;

	}

	public List<ShowKlasse> getKlassenForShow(ShowRing show, Integer idSchau, ShowRing ring) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		String state = "select rasse, geschlecht, klasse, min(sort_kat_nr) as sort"
				+ " from schauhund where idschau = ? and idschauring = ?" +
				" group by rasse, geschlecht, klasse order by sort";
		PreparedStatement st = conn.prepareStatement(state);
		st.setInt(1, idSchau.intValue());
		st.setInt(2, ring.getRingId().intValue());

		List<ShowKlasse> resultList = new ArrayList<>();
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
				ende.setRingGeschlechtEndeFor(show);
				ende.setRasse(oldRasse);

				resultList.add(ende);
				oldGeschlecht = resultSet.getString("geschlecht");
				oldRasse = zwrasse;

			}

			ShowKlasse zw = new ShowKlasse(zwrasse, zwklasse);
			zw.setHundeDerKlasse(
					getHundeForKlasse(zw, idSchau, ring, resultSet.getString("klasse"), zwrasse, zwGeschlecht));
			resultList.add(zw);

		}

		System.out.println("rasse in aufbau " + zwrasse);
		System.out.println("geschlecht in aufbau " + oldGeschlecht);
		ShowGeschlechtEnde ende = new ShowGeschlechtEnde();
		ende.setGeschlechtEnde(zwGeschlecht);
		ende.setRasse(zwrasse);
		ende.setRingGeschlechtEndeFor(show);

		resultList.add(ende);

		return resultList;
	}

	public List<ShowRing> getHundeForKlasse(ShowKlasse zwKlasse, Integer idSchau, ShowRing ring, String klasse,
			Rassen rasse, String geschlecht) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st = conn.prepareStatement(
				"select * from schauhund where idschau = ? and idschauring = ? and klasse = ? and rasse = ? and geschlecht = ? "+
		" order by sort_kat_nr");
		st.setInt(1, idSchau.intValue());
		st.setInt(2, ring.getRingId().intValue());
		st.setString(3, klasse);
		st.setString(4, rasse.getRassenKurzBezeichnung());
		st.setString(5, geschlecht);
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
			ShowKlassen zwklasse = ShowKlassen.getKlasseForKurzBezeichnung(resultSet.getString("klasse"));
			zw.setKlasse(zwklasse);
			zw.setRasse(Rassen.getRasseForKurzBezeichnung(resultSet.getString("rasse")));
			zw.setRingNummer(ring.getRingNummer());
			zw.setRichter(ring.getRichter());
			resultList.add(zw);

		}

		ShowKlasseEnde showKlasseEnde = new ShowKlasseEnde(zwKlasse);
		resultList.add(showKlasseEnde);

		return resultList;
	}

	public void updateShowHund(ShowHund updateHund) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("update schauhund set bewertung = ?, formwert = ?, ");
		sb.append("hundfehlt = ?, platzierung = ?, ");
		sb.append("CACA = ?, CACIB = ?, BOB = ?, clubsieger = ?");
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
		st.setInt(9, updateHund.getIdschauhund().intValue());

		System.out.println("update showhund " + updateHund.getIdschauhund());

		st.executeUpdate();

	}

}
