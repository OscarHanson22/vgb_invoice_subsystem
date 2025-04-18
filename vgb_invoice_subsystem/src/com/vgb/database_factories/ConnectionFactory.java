package com.vgb.database_factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class that abstracts the creation of database connections. 
 */
public class ConnectionFactory {
	static final Connection conn;
    private static final Logger logger = LogManager.getLogger(ConnectionFactory.class);

	static {
		try {
			connection = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			logger.error("SQLException encountered while connecting to the database.");
			throw new RuntimeException(e);
		}
	}
    
    /**
     * Holds information needed to connect to the MySQL invoicing database.
     */
	public class DatabaseInfo {
		public static final String USERNAME = "ohanson5";
		public static final String PASSWORD = "UXied8jeeXee";
		public static final String PARAMETERS = "";
		public static final String SERVER = "cse-linux-01.unl.edu";
		public static final String URL = String.format("jdbc:mysql://%s/%s?%s", SERVER, USERNAME, PARAMETERS);
	}
	
	/**
	 * Returns a connection to the invoicing database.
	 */
	public static Connection connect() {
		return conn;
	}

	public static void closeConnection() {
		conn.close();
	}
}
