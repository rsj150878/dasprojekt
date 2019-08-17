package com.app.dbio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.app.email.EmailList;

public class DBMail {
	public List<EmailList> getAllEmails() throws Exception {
		Connection conn = DBConnectionNeu.INSTANCE.getConnection();
		PreparedStatement st;
		st = conn.prepareStatement("select * from email "
				// + "where datum >= curdate() "
				+ "order by idkurs desc");

		List<EmailList> resultList = new ArrayList<EmailList>();

		ResultSet rs = st.executeQuery();

		while (rs.next()) {
			EmailList zw= new EmailList();
			zw.setEmail(rs.getString("emailadresse"));
			zw.setNewsLetter(rs.getString("newsletter"));
			zw.setId(rs.getInt("id"));
			zw.setVersion(rs.getInt("version"));
			resultList.add(zw);
		}

		return resultList;
	}

}
