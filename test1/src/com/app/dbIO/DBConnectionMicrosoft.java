package com.app.dbIO;

import javax.servlet.ServletContext;

import com.app.DashBoard.DashboardServlet;
import com.app.DashBoard.DashboardUI;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class DBConnectionMicrosoft {
	private final static String DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private final static String DB_USER = "Karl-Heinz";
	private final static String DB_PASSWORD = "";

	private static JDBCConnectionPool connectionPool;

	public final static DBConnectionMicrosoft INSTANCE = new DBConnectionMicrosoft();

	protected DBConnectionMicrosoft() {

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
		dataBase = "jdbc:sqlserver://192.168.178.25:1433;databaseName=TestJavaConnection;loginTimeout=200;";

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
