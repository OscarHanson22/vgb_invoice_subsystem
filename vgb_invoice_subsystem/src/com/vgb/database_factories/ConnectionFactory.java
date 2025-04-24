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
    private static final Logger logger = LogManager.getLogger(ConnectionFactory.class);
	static final Connection connection;

	// initialize the connection to the database
	static {
		try {
			connection = DriverManager.getConnection(DatabaseInfo.URL, DatabaseInfo.USERNAME, DatabaseInfo.PASSWORD);
		} catch (SQLException e) {
			logger.error("SQLException encountered while connecting to the database.");
			throw new RuntimeException(e);
		}
		logger.info("Successfully connected to the database.");
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
	 * Returns the connection to the invoicing database.
	 */
	public static Connection getConnection() {
		return connection;
	}

	/** 
	 * Closes the connection to the invoicing database.
	 */
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			logger.error("SQLException encountered while closing the database.");
			throw new RuntimeException(e);
		}
		logger.info("Connection to database successfully closed.");
	}
}
