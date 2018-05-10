package OERC.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import OERC.Domain.Hund;
import OERC.enums.RetrieverRassen;

public class HundDao {

	public Hund getHundForZuchtbuch(RetrieverRassen rasse, String zuchtbuchnummer) throws Exception {
		Hund returnHund = null;
		Connection con = DBConnectionMicrosoftNeu.INSTANCE.getConnection();

		PreparedStatement st = con.prepareStatement(
				"select * from tabHunde where idRasse = ? and zuchtbuchnummer like ? and zuchtbuch = 'ÖHZB'");
		st.setInt(1, rasse.getIdRasse());
		st.setString(2, "%" + zuchtbuchnummer.trim() + "%");
		ResultSet rs = st.executeQuery();

		if (rs.next()) {
			returnHund = new Hund();
			returnHund.setIdHund(rs.getString("IDHunde"));
			returnHund.setChipNummer(rs.getString("Chipnummer"));
			returnHund.setName(rs.getString("name"));
			returnHund.setWurfTag(rs.getDate("wurfdatum"));
			returnHund.setIdGeschlecht(rs.getInt("IdHundGeschlecht"));
			returnHund.setZbnr(rs.getString("zuchtbuchnummer"));

		}

		return returnHund;
	}

}
