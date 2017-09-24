package com.app.dbIO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class DBShowPrintKatalog {

	DBConnectionMicrosoftNeu dbMsConn;

	String idVeranstaltung1 = "446A188C-0A06-4CFE-B845-308699007D7E";
	String idVeranstaltung2 = "A3609130-93F4-4EB1-88D6-1214F68A547E";

	public void uebertrageHunde() {
		bauePdf(1,1);
		bauePdf(1,2);
		
		bauePdf(2,1);
		bauePdf(2,2);

		bauePdf(3,1);
		bauePdf(3,2);
		
		bauePdf(5,1);
		bauePdf(5,2);
		
		bauePdf(4,1);
		bauePdf(4,2);
		
		bauePdf(6,1);
		bauePdf(6,2);
		

	}

	public void bauePdf(Integer idrasse, Integer idgeschlecht) {

		Connection conn = DBConnectionMicrosoftNeu.INSTANCE.getConnection();

		StringBuilder sb = new StringBuilder();
		sb.append("select * from tabAnmeldung anmeldung");
		sb.append(", tabVeranstaltungenKlasse veranstaltungKlasse");
		sb.append(", vgVeranstaltungKlasse vgVeranstaltungKlasse ");
		sb.append(" where anmeldung.idVeranstaltung in ( ? , ?) ");
		sb.append(" and anmeldung.idVeranstaltungKlasse = veranstaltungKlasse.idVeranstaltungKlasse ");
		sb.append(" and veranstaltungKlasse.idVaKlasse = vgVeranstaltungKlasse.idVaKlasse ");
		sb.append(
				" order by vgVeranstaltungKlasse.idVaKlasse, idposition");

		//System.out.println(sb.toString());
		try {
			PreparedStatement st = conn.prepareStatement(sb.toString());
			st.setString(1, idVeranstaltung1);
			st.setString(2, idVeranstaltung2);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Boolean isMitglied = rs.getBoolean("Mitglied");
				ResultSet mitgliedRs;
				ResultSet hundRs;

				String stern = "";
				if (isMitglied) {

					StringBuilder mitgliedPersonBuilder = new StringBuilder();
					mitgliedPersonBuilder.append("select * from tabMitglieder ");
					mitgliedPersonBuilder.append("where idMitglieder = ?");

					PreparedStatement mitgliedStatement = conn.prepareStatement(mitgliedPersonBuilder.toString());
					mitgliedStatement.setString(1, rs.getString("idMitglieder"));

					mitgliedRs = mitgliedStatement.executeQuery();

					StringBuilder mitgliedHundBuilder = new StringBuilder();
					mitgliedHundBuilder.append("select * from tabHunde ");
					mitgliedHundBuilder.append(" where idHunde = ? and idrasse = ? and idhundgeschlecht = ?");

					PreparedStatement hundStatement = conn.prepareStatement(mitgliedHundBuilder.toString());
					hundStatement.setString(1, rs.getString("idHunde"));
					hundStatement.setInt(2, idrasse);
					hundStatement.setInt(3, idgeschlecht);


					hundRs = hundStatement.executeQuery();
					stern = "*";
				} else {
					// System.out.println(" kein mitglied " +
					// rs.getString("idAnmeldungPerson"));

					StringBuilder mitgliedPersonBuilder = new StringBuilder();
					mitgliedPersonBuilder.append("select * from tabAnmeldungPerson ");
					mitgliedPersonBuilder.append("where idAnmeldungPerson = ?");

					PreparedStatement mitgliedStatement = conn.prepareStatement(mitgliedPersonBuilder.toString());
					mitgliedStatement.setString(1, rs.getString("idAnmeldungPerson"));

					mitgliedRs = mitgliedStatement.executeQuery();

					StringBuilder mitgliedHundBuilder = new StringBuilder();
					mitgliedHundBuilder.append("select * from tabAnmeldungHund ");
					mitgliedHundBuilder.append(" where idAnmeldungHund = ? and idrasse = ? and idhundgeschlecht = ?");

					PreparedStatement hundStatement = conn.prepareStatement(mitgliedHundBuilder.toString());
					hundStatement.setString(1, rs.getString("idAnmeldungHund"));
					hundStatement.setInt(2, idrasse);
					hundStatement.setInt(3, idgeschlecht);
					
					hundRs = hundStatement.executeQuery();
					stern = "";

				}

				mitgliedRs.next();
				//System.out.println("name " + mitgliedRs.getString("nachname"));

				if (hundRs.next()) {

					StringBuilder nameLineStringBuilder = new StringBuilder();
					nameLineStringBuilder.append(stern);
					nameLineStringBuilder.append(hundRs.getString("name").toUpperCase());
					nameLineStringBuilder.append(", ");
					// if (isMitglied) {
					// nameLineStringBuilder.append(hundRs.getInt("nr"));
					// }
					nameLineStringBuilder
							.append(new SimpleDateFormat("dd.MM.yyyy").format(hundRs.getDate("wurfdatum")));
					nameLineStringBuilder.append(", ");
					nameLineStringBuilder.append(hundRs.getString("zuchtbuchnummer"));
					System.out.println(nameLineStringBuilder.toString());

					String vater = hundRs.getString("vater");
					if (vater == null || vater.isEmpty()) {
						StringBuilder vaterString = new StringBuilder();
						vaterString.append("select * from tabHunde ");
						vaterString.append("where idhunde = ? ");
						PreparedStatement vaterStatement = conn.prepareStatement(vaterString.toString());
						vaterStatement.setString(1, hundRs.getString("idhundevater"));

						ResultSet vaterRs = vaterStatement.executeQuery();
						vaterRs.next();
						vater = vaterRs.getString("name");
					}

					String mutter = hundRs.getString("mutter");
					if (mutter == null || mutter.isEmpty() || mutter.trim().length() == 0) {
						StringBuilder vaterString = new StringBuilder();
						vaterString.append("select * from tabHunde ");
						vaterString.append("where idhunde = ? ");
						PreparedStatement vaterStatement = conn.prepareStatement(vaterString.toString());
						vaterStatement.setString(1, hundRs.getString("idhundemutter"));

						ResultSet vaterRs = vaterStatement.executeQuery();
						vaterRs.next();
						mutter = vaterRs.getString("name");
					}

					StringBuilder elternZeileBuilder = new StringBuilder();
					elternZeileBuilder.append("V: ");
					elternZeileBuilder.append(vater);
					elternZeileBuilder.append(", M: ");
					elternZeileBuilder.append(mutter);
					System.out.println(elternZeileBuilder.toString());

					String zuechter = hundRs.getString("zuechter");
					if (zuechter == null || zuechter.isEmpty()) {
						StringBuilder vaterString = new StringBuilder();
						vaterString.append("select * from tabMitglieder ");
						vaterString.append("where idmitglieder = ? ");
						PreparedStatement vaterStatement = conn.prepareStatement(vaterString.toString());
						vaterStatement.setString(1, hundRs.getString("idzuechter"));

						ResultSet vaterRs = vaterStatement.executeQuery();
						if (vaterRs.next()) {
							zuechter = vaterRs.getString("nachname") + " " + vaterRs.getString("vorname");
						} else {
							zuechter = "";
						}
					}

					StringBuilder besitzerZeile = new StringBuilder();
					besitzerZeile.append("Z: ");
					besitzerZeile.append(zuechter);
					besitzerZeile.append(", B: ");
					besitzerZeile.append(mitgliedRs.getString("nachname") + " " + mitgliedRs.getString("vorname"));
					System.out.println(besitzerZeile.toString());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
