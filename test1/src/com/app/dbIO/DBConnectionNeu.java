package com.app.dbIO;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionNeu {

	public final static DBConnectionNeu INSTANCE = new DBConnectionNeu();;
	private final static String DB_USER = "retrieverdata.at";
	private final static String DB_PASSWORD = "waterloo123";
	private static Connection connect;

	protected DBConnectionNeu() {
		// Setup the connection with the DB

		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/test?user=" + DB_USER + "&password=" + DB_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return this.connect;
	}

}
