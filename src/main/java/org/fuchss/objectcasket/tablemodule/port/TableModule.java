package org.fuchss.objectcasket.tablemodule.port;

import java.io.Serializable;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;

/**
 * An abstract representation of a database. Using a table module one can
 * access, create, alter and delete {@link Table tables} inside the assigned
 * database.
 *
 * @see TableModuleFactory#newTableModule(ModuleConfiguration)
 *
 */
public interface TableModule {

	/**
	 * This operation checks whether a {@link Table table} exists in the assigned
	 * database.
	 *
	 * @param tableName
	 *            - the name of the table inside the assigned database.
	 * @return true iff a table with this name exists.
	 * @throws CasketException
	 *             on error.
	 */
	boolean tableExists(String tableName) throws CasketException;

	/**
	 * This operation creates a new table inside die assigned database and returns
	 * an object of type {@link Table} to access the newly created table.
	 *
	 * @param tableName
	 *            - the name of the new table.
	 * @param pkName
	 *            - the name of the primary key column.
	 * @param signature
	 *            - the column definitions. For each column name the assigned Java
	 *            type.
	 * @param autoIncrement
	 *            - indicates whether the primary key should be generated
	 *            automatically. Only possible for Java class {@link Integer}.
	 * @return a representation (Object of type {@link Table table}) of the newly
	 *         created table.
	 * @throws CasketException
	 *             on error.
	 */
	Table createTable(String tableName, String pkName, Map<String, Class<? extends Serializable>> signature, boolean autoIncrement) throws CasketException;

	/**
	 * This operation creates a new representation of an existing table inside the
	 * database. In each table module only one representation of an existing
	 * database table is possible. So one can create a table and also a view. But
	 * not two views.
	 * <p>
	 * A proper assignment should at least map the primary key.
	 *
	 * @param tableName
	 *            - the name of the table inside the database.
	 * @param pkName
	 *            - the name of the primary key column.
	 * @param signature
	 *            - the column definitions. For each assigned column the name and a
	 *            compatible Java type.
	 * @param autoIncrement
	 *            - indicates whether the primary key should be generated
	 *            automatically. Only possible for Java class {@link Integer}.
	 * @return a representation (Object of type {@link Table table}) of the assigned
	 *         table.
	 * @throws CasketException
	 *             on error.
	 */
	Table mkView(String tableName, String pkName, Map<String, Class<? extends Serializable>> signature, boolean autoIncrement) throws CasketException;

	/**
	 * This operation modifies an existing table and returns the corresponding
	 * representation. The table now matches the new column definitions exactly.
	 * <p>
	 * This operation cannot be undone!
	 *
	 * @param tableName
	 *            - the name of the table inside the database.
	 * @param pkName
	 *            - the name of the primary key column.
	 * @param signature
	 *            - the target column definitions. For each assigned column the name
	 *            and a compatible Java type.
	 * @param autoIncrement
	 *            - indicates whether the primary key should be generated
	 *            automatically. Only possible for Java class {@link Integer}.
	 * @return a representation (Object of type {@link Table table}) of the modified
	 *         table.
	 * @throws CasketException
	 *             on error.
	 */
	Table adjustTable(String tableName, String pkName, Map<String, Class<? extends Serializable>> signature, boolean autoIncrement) throws CasketException;

	/**
	 * This operation deletes an existing table inside the assigned database.
	 * <p>
	 * This operation cannot be undone!
	 *
	 * @param tableName
	 *            - the name of the table inside the database.
	 * @throws CasketException
	 *             on error.
	 */
	void dropTable(String tableName) throws CasketException;

	/**
	 * This operation checks whether the table module is closed or not.
	 *
	 * @return true iff it is closed.
	 * @see TableModuleFactory#closeModule(TableModule)
	 * @see TableModuleFactory#closeAllModules(ModuleConfiguration)
	 */
	boolean isClosed();

	/**
	 * This operation returns a transaction voucher. It blocks until all other
	 * transactions are closed. The voucher is necessary to work with a {@link Table
	 * table} inside the table module.
	 *
	 * @return a voucher for a new running transaction.
	 *
	 */
	Object beginTransaction();

	/**
	 * Close the running transaction indicated by the given voucher and commit all
	 * pending changes.
	 *
	 * @param voucher
	 *            - the voucher.
	 * @throws CasketException
	 *             on error.
	 */
	void endTransaction(Object voucher) throws CasketException;

	/**
	 * This operation rolls back a running transaction and undoes any pending
	 * changes.
	 *
	 * @param voucher
	 *            - the voucher.
	 * @throws CasketException
	 *             on error.
	 */
	void rollback(Object voucher) throws CasketException;

}
