package OERC.data;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletContext;

import OERC.Dashboard.DashboardServlet;
import OERC.Dashboard.OERCUI;

public class DBConnectionNeu {

	public final static DBConnectionNeu INSTANCE = new DBConnectionNeu();;
	private final static String DB_USER = "_tst";
	private static Connection connect;

	protected DBConnectionNeu() {
		// Setup the connection with the DB

		try {
			ServletContext sc = DashboardServlet.getCurrent().getServletContext();
			String dbPasswort = sc.getInitParameter("passwort");
			
			Class.forName("com.mysql.jdbc.Driver");
			String dbUrl = "";

			if (OERCUI.getUseLocalUrl()) {
				dbUrl = "breedmaster";
			} else if (OERCUI.getUseProdUrl()) {
				dbUrl = "prod";

			}

			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/" + dbUrl + "?user=" + dbUrl + DB_USER + "&password=" + dbPasswort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connect;
	}

}
