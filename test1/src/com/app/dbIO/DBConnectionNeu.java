package com.app.dbIO;

import java.sql.Connection;
import java.sql.DriverManager;

import com.app.DashBoard.DashboardUI;

public class DBConnectionNeu {

	public final static DBConnectionNeu INSTANCE = new DBConnectionNeu();;
	private final static String DB_USER = "_rdata";
	private final static String DB_PASSWORD = "waterloo123";
	private static Connection connect;

	protected DBConnectionNeu() {
		// Setup the connection with the DB

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String dbUrl = "";

			if (DashboardUI.getUseLocalUrl()) {
				dbUrl = "test1";
			} else if (DashboardUI.getUseProdUrl()) {
				dbUrl = "prod";

			}

			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/" + dbUrl + "?user=" + dbUrl + DB_USER + "&password=" + DB_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connect;
	}

}