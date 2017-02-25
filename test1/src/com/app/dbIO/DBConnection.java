package com.app.dbIO;

import javax.servlet.ServletContext;

import com.app.DashBoard.DashboardServlet;
import com.app.DashBoard.DashboardUI;
import com.vaadin.v7.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.v7.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class DBConnection {

	private final static String DRIVER_NAME = "com.mysql.jdbc.Driver";

	private final static String DB_USER = "retrieverdata.at";
	private final static String DB_PASSWORD = "waterloo123";

	private static JDBCConnectionPool connectionPool;

	public final static DBConnection INSTANCE = new DBConnection();

	protected DBConnection() {

		ServletContext sc = DashboardServlet.getCurrent().getServletContext();

		String dbUrl = "";
		if (DashboardUI.getUseLocalUrl()) {
			dbUrl = sc.getInitParameter("DBURLLocal");
		} else if (DashboardUI.getUseProdUrl()) {
			dbUrl = sc.getInitParameter("DBURLProd");

		} else {
			dbUrl = sc.getInitParameter("DBURL");
		}
		
		String dataBase = sc.getInitParameter("Database");
		System.out.println("database: " + dataBase);
		dataBase = "jdbc:mysql://localhost:3306" + dataBase;

		try {

			connectionPool = new SimpleJDBCConnectionPool(DRIVER_NAME, dataBase,
					DB_USER, DB_PASSWORD, 2, 5);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public JDBCConnectionPool getConnectionPool() {
		return this.connectionPool;
	}

}
