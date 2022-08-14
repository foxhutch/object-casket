package org.fuchss.objectcasket.sqlconnector;

import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabaseFactory;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;

/**
 * The access point to the databases in use. Through the static
 * {@link SqlPort#SQL_PORT} object one obtains two factories for accessing these
 * {@link SqlDatabase databases}. One {@link SqlDatabaseFactory factory} for the
 * databases themselves and one {@link SqlObjectFactory factory} for the
 * {@link SqlObject SqlObjects} that can be stored in cells of tables.
 *
 */

public interface SqlPort {
	/**
	 * The facade to access the SQL connector.
	 */
	SqlPort SQL_PORT = new SqlConnectorImpl();

	/**
	 * This operation returns the {@link SqlObjectFactory} to build objects that can
	 * be stored in cells of tables.
	 *
	 * @return the {@link SqlObjectFactory}
	 */
	SqlObjectFactory sqlObjectFactory();

	/**
	 * This operation returns the {@link SqlDatabaseFactory} to build an alter
	 * databases.
	 *
	 * @return the {@link SqlDatabaseFactory}
	 */
	SqlDatabaseFactory sqlDatabaseFactory();

}
