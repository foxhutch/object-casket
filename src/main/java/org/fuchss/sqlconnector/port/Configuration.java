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
	 * @return success or not. This means a new class and/or driver name were
	 *         registered.
	 * @throws ConnectorException
	 *             if parameters are wrong
	 */
	boolean setDriver(Class<? extends Driver> driver, String driverName) throws ConnectorException;

	/**
	 * Set the URI to the database.
	 *
	 * @param uri
	 *            the URI
	 * @return success or not. This means the registered uri for the database was
	 *         set or has changed.
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean setUri(String uri) throws ConnectorException;

	/**
	 * Set the username for the database.
	 *
	 * @param name
	 *            the username
	 * @return success or not. This means the registered user name was set or has
	 *         changed.
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean setUser(String name) throws ConnectorException;

	/**
	 * Set the password for the database.
	 *
	 * @param passwd
	 *            the password
	 * @return success or not. This means the registered password was set or has
	 *         changed.
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean setPasswd(String passwd) throws ConnectorException;

	/**
	 * Set {@link Flag Flags} for this configuration.
	 *
	 * @param flags
	 *            the flags
	 * @return success or not. This means the set of flags has changed.
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean setFlag(Flag... flags) throws ConnectorException;

	/**
	 * Set {@link Flag Flags} for this configuration.
	 *
	 * @param flags
	 *            the flags
	 * @return success or not. This means the set of flags has changed.
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean removeFlag(Flag... flags) throws ConnectorException;

	/**
	 * Are the {@link Flag Flags} set.
	 *
	 * @param flags
	 *            the flags
	 * @return true iff all flags are set.
	 * @throws ConnectorException
	 *             if parameter is wrong
	 */
	boolean containsAll(Flag... flags) throws ConnectorException;

	enum Flag {
		/**
		 * Allow file creation.
		 */
		CREATE,
		/**
		 * Allow DB modification.
		 */
		MODIFY,
		/**
		 * Allow multiple sessions.
		 */
		SESSIONS;
	}
}
