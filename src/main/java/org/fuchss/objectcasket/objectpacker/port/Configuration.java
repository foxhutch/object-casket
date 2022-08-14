package org.fuchss.objectcasket.objectpacker.port;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.DialectH2;
import org.fuchss.objectcasket.sqlconnector.port.DialectSqlite;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;

import java.sql.Driver;

/**
 * A set of information to access an existing database or create a new one.
 */
public interface Configuration {

	/**
	 * This operation sets driver and driver prefix.<br>
	 * E.g. driver = <em>org.sqlite.JDBC.class</em> and driverPrefix =
	 * <em>jdbc:sqlite:</em>
	 * <p>
	 * One can use one of the predefined dialects {@link DialectH2} or
	 * {@link DialectSqlite}, or create one's own.
	 *
	 * @param driver       - the driver class.
	 * @param driverPrefix - the driver prefix for the URL.
	 * @param dialect      - dialect specific names for SQL types and annotations.
	 * @return success or not. This means a new class and/or driver name were
	 * registered.
	 * @throws CasketException on error.
	 * @see SqlDialect
	 */
	boolean setDriver(Class<? extends Driver> driver, String driverPrefix, SqlDialect dialect) throws CasketException;

	/**
	 * This operation sets the database URI.
	 *
	 * @param uri -the URI.
	 * @return success or not.
	 * @throws CasketException on error.
	 */
	boolean setUri(String uri) throws CasketException;

	/**
	 * This operation sets the username for the database. If database and user
	 * exist, the user's permissions should be sufficient to work with the database.
	 * Object Casket does not yet support a role concept.
	 *
	 * @param name - the user name.
	 * @return success or not.
	 * @throws CasketException on error.
	 */
	boolean setUser(String name) throws CasketException;

	/**
	 * This operation sets the password for the database. Whether access is granted
	 * or not will be known as soon as a connection can be established or not.
	 *
	 * @param password - the password.
	 * @return success or not. This means the registered password was set or has
	 * changed - not more.
	 * @throws CasketException on error.
	 * @see SessionManager#session(Configuration)
	 */
	boolean setPassword(String password) throws CasketException;

	/**
	 * This operation sets {@link Flag flags} for this configuration.
	 *
	 * @param flags -the flags.
	 * @return success or not.
	 * @throws CasketException on error.
	 */
	boolean setFlag(Flag... flags) throws CasketException;

	/**
	 * This operation removes {@link Flag flags} for this configuration.
	 *
	 * @param flags - the flags to remove.
	 * @return success or not. This means the set of flags has changed.
	 * @throws CasketException on error.
	 */
	boolean removeFlag(Flag... flags) throws CasketException;

	/**
	 * These operations check whether {@link Flag flags} are set?
	 *
	 * @param flags - the flags.
	 * @return true iff all flags are set.
	 * @throws CasketException on error.
	 */
	boolean containsAll(Flag... flags) throws CasketException;

	/**
	 * Different flags to configure the possibilities within a session.
	 *
	 * @see Flag#CREATE
	 * @see Flag#ALTER
	 * @see Flag#WRITE
	 * @see Flag#SESSIONS
	 */
	enum Flag {
		/**
		 * Allows database and table creation. So one can store objects of new classes.
		 */
		CREATE,
		/**
		 * Allows table modification. Thus, one can modify existing classes by adding
		 * attributes or removing attributes.
		 */
		ALTER,
		/**
		 * Objects can be stored and updated.
		 */
		WRITE,
		/**
		 * Allows multiple sessions.
		 */
		SESSIONS
	}

}
