package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public class DatabaseInfo {
		public static final String USERNAME = "ohanson5";
		public static final String PASSWORD = "UXied8jeeXee";
		public static final String PARAMETERS = "";
		public static final String SERVER = "cse-linux-01.unl.edu";
		public static final String URL = String.format("jdbc:mysql://%s/%s?%s", SERVER, USERNAME, PARAMETERS);
	}
	
	public static Connection connect() throws SQLException {
		return DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
	}
}
