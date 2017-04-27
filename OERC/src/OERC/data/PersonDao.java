package OERC.data;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import OERC.Domain.DBConnectionMicrosoft;
import OERC.Domain.User;

public class PersonDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6247804754246796604L;

	public User loadUserById(String userId) throws Exception {

		Connection conn = DBConnectionMicrosoft.INSTANCE.getConnection();

		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		sb.append("tabMitglieder where Nr = ");
		sb.append(userId);
		System.out.println(sb.toString());

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(sb.toString());

		User user = null;
	
		if (rs.next()) {
			user = new User();
			user.setRole("admin");
			return user;

		} else {
			throw new Exception("User unbekannt");

		}
		
	}

}
