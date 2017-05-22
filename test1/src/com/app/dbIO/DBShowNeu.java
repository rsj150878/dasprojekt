package com.app.dbIO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.app.enumPackage.Rassen;
import com.app.enumPackage.ShowKlassen;
import com.app.showData.ShowGeschlechtEnde;
import com.app.showData.ShowHund;
import com.app.showData.ShowKlasse;
import com.app.showData.ShowKlasseEnde;
import com.app.showData.ShowRing;

public class DBShowNeu {

	public List<ShowRing> getRingeFuerShow(Integer idSchau) throws Exception {

		Connection conn = DBConnectionNeu.INSTANCE.getConnection();

		PreparedStatement st = conn.prepareStatement("select * from schauring where idschau = ?");
		st.setInt(1, idSchau.intValue());

		ResultSet resultSet = st.executeQuery();

		List<ShowRing> resultList = new ArrayList<ShowRing>();
		while (resultSet.next()) {
			ShowRing zw = new ShowRing();
			zw.setRingNummer(resultSet.getString("ringnummer"));
			zw.setRichter(resultSet.getString("richter"));
			zw.addShowKlassen(getKlassenForShow(zw, idSchau, Integer.valueOf(resultSet.getInt("idschauring"))));
			resultList.add(zw);
		}

		return resultList;

	}

	public List<ShowKlasse> getKlassenForShow(ShowRing show, Integer idSchau, Integer idSchauRing) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st = conn
				.prepareStatement("select distinct rasse, geschlecht, klasse from schauhund where idschau = ? and idschauring = ?");
		st.setInt(1, idSchau.intValue());
		st.setInt(2, idSchauRing.intValue());
		List<ShowKlasse> resultList = new ArrayList<>();
		ResultSet resultSet = st.executeQuery();
		String oldGeschlecht = "";
		Rassen oldRasse = null;
		Rassen zwrasse = null;
		
		while (resultSet.next()) {

			zwrasse = Rassen.getRasseForKurzBezeichnung(resultSet.getString("rasse"));
			ShowKlassen zwklasse = ShowKlassen.getKlasseForKurzBezeichnung(resultSet.getString("klasse"));
			System.out.println(" zwrasse " + zwrasse.getRassenKurzBezeichnung() + " klasse "
					+ zwklasse.getShowKlassenKurzBezeichnung());
			ShowKlasse zw = new ShowKlasse(zwrasse, zwklasse);
			zw.setHundeDerKlasse(getHundeForKlasse(zw, idSchau, idSchauRing, resultSet.getString("klasse")));
			resultList.add(zw);
			
			if (oldGeschlecht.isEmpty()) {
				oldGeschlecht = resultSet.getString("geschlecht");
			}
			
			if (oldRasse == null) {
				oldRasse = zwrasse;
			}

			if (!oldRasse.equals(zwrasse) || !oldGeschlecht.equals(resultSet.getString("geschlecht"))) {
				oldGeschlecht = resultSet.getString("geschlecht");
				ShowGeschlechtEnde ende = new ShowGeschlechtEnde();
				ende.setGeschlechtEnde(oldGeschlecht);
				ende.setRingGeschlechtEndeFor(show);
				ende.setRasse(oldRasse);
				resultList.add(ende);
			}
			
		}
		ShowGeschlechtEnde ende = new ShowGeschlechtEnde();
		ende.setGeschlechtEnde(oldGeschlecht);
		ende.setRasse(zwrasse);
		ende.setRingGeschlechtEndeFor(show);
		
		resultList.add(ende);

		return resultList;
	}

	public List<ShowRing> getHundeForKlasse(ShowKlasse zwKlasse, Integer idSchau, Integer idSchauRing, String klasse) throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st = conn
				.prepareStatement("select * from schauhund where idschau = ? and idschauring = ? and klasse = ?");
		st.setInt(1, idSchau.intValue());
		st.setInt(2, idSchauRing.intValue());
		st.setString(3, klasse);
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
			zw.setClubsieger(resultSet.getString("clubsieger"));
			ShowKlassen zwklasse = ShowKlassen.getKlasseForKurzBezeichnung(resultSet.getString("klasse"));
			zw.setKlasse(zwklasse);
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
		
		st.executeUpdate();

	}

}
