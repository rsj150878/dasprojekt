package com.app.dbIO;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import com.app.DashBoard.DashboardServlet;
import com.app.DashBoard.DashboardUI;

public class DBConnectionMicrosoftNeu {

	public final static DBConnectionMicrosoftNeu INSTANCE = new DBConnectionMicrosoftNeu();;
	private final static String DB_USER = "OERC";
	private static Connection connect;

	protected DBConnectionMicrosoftNeu() {
		// Setup the connection with the DB

		try {
			String dbPasswort = "1q2w3e4r";
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connect = DriverManager.getConnection(
					"jdbc:sqlserver://192.168.178.25:1433;databaseName=OERC;user=" + DB_USER + ";password=" + dbPasswort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connect;
	}

}
