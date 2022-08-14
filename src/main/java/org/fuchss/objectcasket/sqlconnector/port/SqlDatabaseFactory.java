package org.fuchss.objectcasket.sqlconnector.port;

import org.fuchss.objectcasket.common.CasketException;

/**
 * Main interface to access or create a database.
 *
 *
 */
public interface SqlDatabaseFactory {

	/**
	 * This operation closes the connection to the {@link SqlDatabase database}. So
	 * no one can read and write any more.
	 *
	 * @param db
	 *            - the SqlDatabase object to close.
	 * @throws CasketException
	 *             on error.
	 */
	void closeDatabase(SqlDatabase db) throws CasketException;

	/**
	 * This operation creates a {@link DBConfiguration configuration} to access a
	 * database.
	 *
	 * @return a new configuration.
	 */
	DBConfiguration createConfiguration();

	/**
	 * This operation creates and opens an {@link SqlDatabase } object according to
	 * the {@link DBConfiguration configuration}. It should be noted that if the
	 * same configuration is used twice, the same (identical) database is created.
	 *
	 * @param config
	 *            - the configuration.
	 * @return the SqlDatabase object.
	 * @throws CasketException
	 *             on error.
	 */
	SqlDatabase openDatabase(DBConfiguration config) throws CasketException;

}
