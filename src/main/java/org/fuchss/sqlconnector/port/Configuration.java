package org.fuchss.sqlconnector.port;

import java.sql.Driver;

public interface Configuration {

	/**
	 * Set driver and driver name.<br>
	 * E.g. driver = <em>org.sqlite.JDBC.class</em> and driverName =
	 * <em>jdbc:sqlite:</em>
	 *
	 * @param driver
	 *            the driver class
	 * @param driverName
	 *            the driver name for url
	 * @return success or not
	 * @throws ConnectorException
	 *             if parameters are wrong
	 */
	boolean setDriver(Class<? extends Driver> driver, String driverName) throws ConnectorException;

	/**
	 * Set the URI to the database.
	 *
	 * @param uri
	 *            the URI
	 * @return success or not
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean setUri(String uri) throws ConnectorException;

	/**
	 * Set the username for the database.
	 *
	 * @param name
	 *            the username
	 * @return success or not
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean setUser(String name) throws ConnectorException;

	/**
	 * Set the password for the database.
	 *
	 * @param passwd
	 *            the password
	 * @return success or not
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean setPasswd(String passwd) throws ConnectorException;

	/**
	 * Set {@link Flag Flags} for this configuration.
	 *
	 * @param flags
	 *            the flags
	 * @return success or not
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean setFlag(Flag... flags) throws ConnectorException;

	enum Flag {
		/**
		 * Allow file creation.
		 */
		CREATE,
		/**
		 * Allow DB modification.
		 */
		MODIFY;
	}
}
