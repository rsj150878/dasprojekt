package OERC.data;

import java.sql.Connection;
import java.sql.DriverManager;

import OERC.Dashboard.AbstractOERCUi;

public class DBConnectionMicrosoftNeu {

	public final static DBConnectionMicrosoftNeu INSTANCE = new DBConnectionMicrosoftNeu();;
	private static Connection connect;

	protected DBConnectionMicrosoftNeu() {
		// Setup the connection with the DB

		try {
		
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			String dbUrl = "";
			String dataBase = "";
			String dbUser = "";
			String dbPasswort = "";

//			if (AbstractOERCUi.getUseLocalUrl()) {
//				dbUrl = "192.168.178.25:1433";
//				dataBase = "OERC";
//				dbUser = "OERC";
//				dbPasswort = "1q2w3e4r";
			if (AbstractOERCUi.getUseLocalUrl()) {
				dbUrl = "VMEAF861C\\MSSQLSERVER2016";
				dataBase = "admin_oerc";
				dbUser = "admin";
				dbPasswort = "LVwbf2005";
			} else if (AbstractOERCUi.getUseProdUrl()) {
			
//			} else if (AbstractOERCUi.getUseProdUrl()) {
				dbUrl = "www.retrieverdata.at:51363";
				dataBase = "admin_oerc";
				dbUser = "admin";
				dbPasswort = "LVwbf2005";
			}

			connect = DriverManager.getConnection("jdbc:sqlserver://" + dbUrl + ";databaseName=" + dataBase + ";user="
					+ dbUser + ";password=" + dbPasswort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connect;
	}

}
