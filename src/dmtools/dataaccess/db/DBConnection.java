/*
 * Created on Jun 13, 2005
 *
 * Writen by Robert Dominic McGinnis.
 * 
 */
package dmtools.dataaccess.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

import dmtools.dataaccess.exception.MissingConnectionInfoException;
import dmtools.logging.Logger;

/**
 * @author Dominic
 *
 * This is a singleton class that wil get a connection to 
 * a database supplied by the parameters passed into getConnection.  If 
 * these parameters are not supplied a MissingConnectionInfoException will be
 * thrown.  All other SQL related exceptions (Database connections, etc...) 
 * wll be caught and thrown.
 *  
 * 
 */
public class DBConnection {
	private DBConnection dbConnection = null; 
	private Logger logger = null;

	private DBConnection() {
		this.logger = new Logger(DBConnection.class.getName());
	}
	
	public DBConnection getInstance() {
		if(dbConnection != null) {
			return dbConnection;
		} else {
			return new DBConnection();
		}
	}

	/**
	 * @author Dominic
	 *
	 * Returns a database connection.  Expects you to pass in the database driver,
	 * location, username, password, and schema.
	 * 
	 * @param databaseLocation the location of the database
	 * @param driverName the driver to use while connecting
	 * @param username the username to use when logging in
	 * @param password the password to use when logging in
	 * @param schema the schema to use for selects
	 * 
	 */
	public Connection getConnection(String databaseLocation, String driverName, String username, String password, String schema) throws MissingConnectionInfoException, SQLException, Exception
	{
		Connection conn = null;
		try 
		{
			if(databaseLocation == null || driverName == null || username == null || password == null || schema == null) {
				String exceptionStr = "Recieved Connection details: ";
				exceptionStr += "databaseLocation = " + databaseLocation;
				exceptionStr += ", driverName = " + driverName;
				exceptionStr += ", username = " + username;
				exceptionStr += ", password = " + password;
				exceptionStr += ", schema = " + driverName;
				
				throw new MissingConnectionInfoException(exceptionStr);
			}

			String address = databaseLocation + "/" + schema + "?user=" + username + "&password=" + password;
			Class.forName(driverName).newInstance();

			logger.log("Attempting connection to: " + address);
			logger.log("User, Password, Driver: " + username + " " + password + " " + driverName);
			conn = DriverManager.getConnection(address,username,password);
			logger.log("Connection successful");

			return conn;
		} catch (MissingConnectionInfoException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
}