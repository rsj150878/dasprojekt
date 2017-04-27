package OERC.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import OERC.Domain.DBConnectionMicrosoft;
import OERC.Domain.Veranstaltungen;

public class VeranstaltungDao {
	
	public Collection<Veranstaltungen> getAllVeranstaltungen() throws Exception {
		ArrayList<Veranstaltungen> resultList = new ArrayList<Veranstaltungen>();
		Connection conn = DBConnectionMicrosoft.INSTANCE.getConnection();

		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append("tabVeranstaltungen");
		sb.append(" order by AnfangDatum desc ");
		
		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(sb.toString());
		
		while (rs.next()) {
			Veranstaltungen zw = new Veranstaltungen();
			zw.setIdVeranstaltung(UUID.fromString(rs.getString("IDVeranstaltung")));
			zw.setIDVAKategorie(new Integer(rs.getInt("IDVAKategorie")));
			zw.setBezeichnung(rs.getString("Bezeichnung"));
			System.out.println("bez: " + rs.getString("Bezeichnung"));
			zw.setAnfangDatum(rs.getDate("AnfangDatum"));
			zw.setEndDatum(rs.getDate("EndDatum"));
			resultList.add(zw);
		}
		return resultList;
	}

}
