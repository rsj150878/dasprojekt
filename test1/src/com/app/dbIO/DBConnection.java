package com.app.dbIO;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class DBConnection {
	
	private final static String DRIVER_NAME = "com.mysql.jdbc.Driver";
	//private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/usr_web408_1";
	//private final static String DATABASE_URL = "jdbc:mysql://localhost:3306/test";
	
	private final static String DATABASE_URL = "jdbc:mysql://profi1.de:3306/test";
	//private final static String DB_USER = "root";
	//private final static String DB_PASSWORD = "waterloo";
	//private final static String DB_USER = "usr_web408_1";
	//private final static String DB_PASSWORD = "koVzMlAZ";
	private final static String DB_USER = "retrieverdata.at";
	private final static String DB_PASSWORD = "waterloo123";
	
	private static JDBCConnectionPool connectionPool;
	
	public final static DBConnection INSTANCE = new DBConnection();
	
	protected DBConnection() {
		try {
			connectionPool = new SimpleJDBCConnectionPool(
                  	DRIVER_NAME,
                  	DATABASE_URL, DB_USER, DB_PASSWORD, 2, 5);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public JDBCConnectionPool getConnectionPool() {
		return this.connectionPool;
	}
	
	
	
	

}
