package org.fuchss.objectcasket.sqlconnector.port;

import java.sql.Driver;

import org.fuchss.objectcasket.common.CasketException;

/**
 * A set of informations to access an existing database or create a new one.
 *
 *
 */
public interface DBConfiguration {
	/**
	 * This operation sets driver, driver prefix, and dialect specific names.<br>
	 * E.g. driver = <em>org.sqlite.JDBC.class</em> and driverPrefix =
	 * <em>jdbc:sqlite:</em>
	 * <p>
	 * One can use one of the predefined dialects {@link DialectH2} or
	 * {@link DialectSqlite}, or create one's own.
	 *
	 * @param driver
	 *            - the driver class.
	 * @param driverPrefix
	 *            - the driver prefix for the URL.
	 * @param dialect
	 *            - dialect specific names for SQL types and annotations.
	 * @return success or not. This means a new class and/or driver prefix were
	 *         registered.
	 * @throws CasketException
	 *             on error.
	 */
	boolean setDriver(Class<? extends Driver> driver, String driverPrefix, SqlDialect dialect) throws CasketException;

	/**
	 * This operation sets the database URI.
	 *
	 * @param uri
	 *            - the URI.
	 * @return success or not. This means the registered URI was set or has changed.
	 * @throws CasketException
	 *             on error.
	 */
	boolean setUri(String uri) throws CasketException;

	/**
	 * This operation sets the user name for the database. If database and user
	 * exist, the user's permissions should be sufficient to work with the database.
	 * Object Casket does not yet support a role concept.
	 *
	 * @param name
	 *            - the user name
	 * @return success or not. This means the registered user name was set or has
	 *         changed.
	 * @throws CasketException
	 *             on error.
	 */
	boolean setUser(String name) throws CasketException;

	/**
	 * This operation sets the password for the database. Whether access is granted
	 * or not will be known as soon as a connection can be established or not.
	 *
	 * @see SqlDatabaseFactory#openDatabase(DBConfiguration)
	 *
	 *
	 * @param password
	 *            - the password.
	 * @return success or not. This means the registered password was set or has
	 *         changed - not more.
	 * @throws CasketException
	 *             on error.
	 */
	boolean setPassword(String password) throws CasketException;

	/**
	 * This operation sets {@link Flag Flags} for this configuration.
	 *
	 * @param flags
	 *            - the flags.
	 * @return success or not. This means the set of flags has changed.
	 * @throws CasketException
	 *             on error.
	 */
	boolean setFlag(Flag... flags) throws CasketException;

	/**
	 * This operation removes {@link Flag flags} for this configuration.
	 *
	 * @param flags
	 *            - the flags
	 * @return success or not. This means the set of flags has changed.
	 * @throws CasketException
	 *             on error.
	 */
	boolean removeFlag(Flag... flags) throws CasketException;

	/**
	 * This operations checks whether {@link Flag flags} are set.
	 *
	 * @param flags
	 *            - the flags.
	 * @return true iff all flags are set.
	 * @throws CasketException
	 *             on error.
	 */
	boolean containsAll(Flag... flags) throws CasketException;

	/**
	 * This operation checks whether the configuration is already in use or not.
	 *
	 * @return true iff the configuration is in use.
	 */
	boolean isInUse();

	/**
	 * This operation checks whether the configuration contains all necessary
	 * informations.
	 *
	 * @return true iff the configuration contains all necessary informations.
	 */
	boolean isComplete();

	/**
	 *
	 * Different flags to configure the connection to an SQL database.
	 *
	 */
	enum Flag {
		/**
		 * Allow file and table creation.
		 */
		CREATE,
		/**
		 * Allow DB modification.
		 */
		MODIFY;
	}

}