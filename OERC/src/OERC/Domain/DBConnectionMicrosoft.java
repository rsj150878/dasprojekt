package OERC.Domain;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import com.vaadin.v7.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.v7.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

import OERC.Dashboard.DashboardServlet;
import OERC.Dashboard.OERCUI;

public class DBConnectionMicrosoft {
	private final static String DRIVER_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	private final static String DB_USER = "dbuser";
	private final static String DB_PASSWORD = "dbuser";
	private Connection conn;

	public final static DBConnectionMicrosoft INSTANCE = new DBConnectionMicrosoft();

	protected DBConnectionMicrosoft() {

		ServletContext sc = DashboardServlet.getCurrent().getServletContext();

		String dbUrl = "";
		if (OERCUI.getUseLocalUrl()) {
			dbUrl = sc.getInitParameter("DBURLLocal");
		} else if (OERCUI.getUseProdUrl()) {
			dbUrl = sc.getInitParameter("DBURLProd");

		} else {
			dbUrl = sc.getInitParameter("DBURL");
		}
		
		String dataBase = sc.getInitParameter("Database");
		System.out.println("database: " + dataBase);
		dataBase = "jdbc:sqlserver://192.168.178.25:1433;database=OERCTest";

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	         conn = DriverManager.getConnection(dataBase,
	                  DB_USER, DB_PASSWORD);
	         conn.getMetaData();
	         conn.getCatalog();
	         conn.getSchema();
	         

	
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

}
