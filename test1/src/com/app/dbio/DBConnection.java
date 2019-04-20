package com.app.dbio;

import javax.servlet.ServletContext;

import com.app.dashboard.DashboardServlet;
import com.app.dashboard.DashboardUI;
import com.vaadin.v7.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.v7.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class DBConnection {

	private final static String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

	private final static String DB_USER = "_rdata";

	private static JDBCConnectionPool connectionPool;

	public final static DBConnection INSTANCE = new DBConnection();

	protected DBConnection() {

		ServletContext sc = DashboardServlet.getCurrent().getServletContext();
		String dbPasswort = sc.getInitParameter("passwort");
		
		String dbUrl = "";

		if (DashboardUI.getUseLocalUrl()) {
			dbUrl = "test1";
		} else if (DashboardUI.getUseProdUrl()) {
			dbUrl = "prod";

		}

		String dataBase = "jdbc:mysql://localhost:3306/" + dbUrl + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

		try {

			connectionPool = new SimpleJDBCConnectionPool(DRIVER_NAME, dataBase,
					dbUrl + DB_USER, dbPasswort, 2, 5);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public JDBCConnectionPool getConnectionPool() {
		return this.connectionPool;
	}

}
