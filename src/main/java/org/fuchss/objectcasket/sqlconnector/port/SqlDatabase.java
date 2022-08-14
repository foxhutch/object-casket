package org.fuchss.objectcasket.sqlconnector.port;

import org.fuchss.objectcasket.common.CasketException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The main database interface providing operations to access, create, delete,
 * and alter tables.
 */

public interface SqlDatabase {

	/**
	 * This operation creates a table according to the given column definitions (at
	 * least two columns are required). The created table can be accessed via the
	 * returned assignment. This operation is only possible if no transaction is
	 * running.
	 *
	 * @param tableName - the name of the table to create.
	 * @param colSigMap - the column definitions of the new table.
	 * @return a new TableAssignment to access the table.
	 * @throws CasketException on error.
	 */
	TableAssignment createTable(String tableName, Map<String, SqlColumnSignature> colSigMap) throws CasketException;

	/**
	 * This operation assigns a set of column definitions to the named table in the
	 * database. The column definition should at least contains the primary key
	 * definition. By this operation it is possible to access only a partition of
	 * the whole table. Only possible if no transaction is running.
	 *
	 * @param tableName - the name of the table.
	 * @param colSigMap - the column definitions which should be assigned.
	 * @return a new TableAssignment to access the defined part of the table.
	 * @throws CasketException on error.
	 */
	TableAssignment createView(String tableName, Map<String, SqlColumnSignature> colSigMap) throws CasketException;

	/**
	 * This operation removes all missing columns from the table. And adds all
	 * mentioned columns which are not part of the table yet. This is only possible
	 * if there are no table assignments to the addressed table. If a table
	 * assignments exists an Exception is thrown. After all the table should at
	 * least contain a primary key and one more column. Only possible if no
	 * transaction is running.
	 *
	 * @param tableName - the name of the table.
	 * @param colSigMap - the target column definitions which should be assigned.
	 * @return a new TableAssignment to access the table.
	 * @throws CasketException on error.
	 */
	TableAssignment adjustTable(String tableName, Map<String, SqlColumnSignature> colSigMap) throws CasketException;

	/**
	 * This operation removes the entire table from the database. This is only
	 * possible if there are no table assignments to the addressed table.
	 *
	 * @param tableName - the table to remove.
	 * @throws CasketException on error.
	 */
	void dropTable(String tableName) throws CasketException;

	/**
	 * This operation checks whether the given table is part of the database. Only
	 * possible if no transaction is running.
	 *
	 * @param tableName - the table to check.
	 * @return true if the table exists.
	 * @throws CasketException on error.
	 */
	boolean tableExists(String tableName) throws CasketException;

	/**
	 * This operation returns the transaction voucher or null if another transaction
	 * is running. If shouldWait is true the method blocks until all other
	 * transactions are closed and a voucher is received.
	 *
	 * @param shouldWait - if true the method blocks until a new transaction is possible.
	 * @return a voucher or null if shouldWait is false and another transaction is
	 * running.
	 */
	Object beginTransaction(boolean shouldWait);

	/**
	 * This operation closes the running transaction indicated by the given voucher
	 * and commit all pending changes.
	 *
	 * @param voucher - the voucher of the current transaction.
	 * @throws CasketException on error.
	 */
	void endTransaction(Object voucher) throws CasketException;

	/**
	 * This operation performs a rollback on the running transaction and undo all
	 * pending changes.
	 *
	 * @param voucher - the voucher of the current transaction.
	 * @throws CasketException on error.
	 */
	void rollback(Object voucher) throws CasketException;

	/**
	 * This operation builds a {@link PreCompiledStatement pre-compiled statement}
	 * to create a new row for the specific {@link TableAssignment table
	 * assignment}.
	 *
	 * @param tabAssignment - the {@link TableAssignment table assignment}.
	 * @return the {@link PreCompiledStatement pre-compiled statement}.
	 * @throws CasketException on error.
	 * @see SqlDatabase#newRow(PreCompiledStatement, Map, Object)
	 */
	PreCompiledStatement mkNewRowStmt(TableAssignment tabAssignment) throws CasketException;

	/**
	 * This operation creates a new row and returns an SqlObject containing the
	 * value of the primary key.
	 *
	 * @param preStat - the {@link PreCompiledStatement pre-compiled statement}.
	 * @param values  - the values for the named columns. The map can only contain
	 *                values for columns which are defined in the pre-compiled
	 *                statement. If the primary key is auto incremented, then the
	 *                assigned SqlObject will be ignored.
	 * @param voucher - the voucher of the current transaction..
	 * @return the value of the primary key of the newly created row.
	 * @throws CasketException on error.
	 * @see SqlDatabase#mkNewRowStmt(TableAssignment)
	 */

	Map<String, SqlObject> newRow(PreCompiledStatement preStat, Map<String, SqlObject> values, Object voucher) throws CasketException;

	/**
	 * This operation builds a pre-compiled statement to alter an existing row in
	 * the assigned table.
	 *
	 * @param tabAssignment - the {@link TableAssignment table assignment}.
	 * @param columns       - the names of the columns to alter.
	 * @return the {@link PreCompiledStatement pre-compiled statement}.
	 * @throws CasketException on error.
	 * @see SqlDatabase#updateRow(PreCompiledStatement, SqlObject, Map, Object)
	 */
	PreCompiledStatement mkUpdateRowStmt(TableAssignment tabAssignment, Set<String> columns) throws CasketException;

	/**
	 * This operation updates the content of a row.
	 *
	 * @param preStat - the {@link PreCompiledStatement pre-compiled statement}.
	 * @param pk      - the primary key, to identify the row.
	 * @param values  - the new values. The map can only contain values for columns
	 *                which are defined in the pre-compiled statement. If the primary
	 *                key is part of the values, then the assigned SqlObject will be
	 *                ignored.
	 * @param voucher - the voucher of the current transaction.
	 * @throws CasketException on error.
	 * @see SqlDatabase#mkUpdateRowStmt(TableAssignment, Set)
	 */
	void updateRow(PreCompiledStatement preStat, SqlObject pk, Map<String, SqlObject> values, Object voucher) throws CasketException;

	/**
	 * This operation builds a pre-compiled statement to select all rows matching
	 * the first order conditions given by the set of sqlArgs and the binding
	 * operator. To select all rows in the given table one uses an empty set.
	 *
	 * @param tabAssignment - the {@link TableAssignment table assignment}.
	 * @param args          - the set of arguments defining the selection process.
	 * @param op            -the binding {@link SqlArg.OP operator}, defining how to combine
	 *                      the different {@link SqlArg arguments}.
	 * @return the {@link PreCompiledStatement pre-compiled statement}.
	 * @throws CasketException on error.
	 * @see SqlDatabase#select(PreCompiledStatement, Map, Object)
	 */
	PreCompiledStatement mkSelectStmt(TableAssignment tabAssignment, Set<SqlArg> args, SqlArg.OP op) throws CasketException;

	/**
	 * This operation selects all rows matching the first order condition given by
	 * the set of sqlArgs and the operator used in the pre-compiled statement. If the
	 * voucher in null this is a dirty read. Else it is part of a running
	 * transaction indicated by the given voucher.
	 *
	 * @param preStat     - the {@link PreCompiledStatement pre-compiled statement} defining
	 *                    the selection.
	 * @param relatedObjs - the values for the selection.
	 * @param voucher     - the voucher of the current transaction.
	 * @return the list of results. Each result is mapping of column names to the
	 * corresponding values.
	 * @throws CasketException on error.
	 * @see SqlDatabase#mkSelectStmt(TableAssignment, Set, SqlArg.OP)
	 */
	List<Map<String, SqlObject>> select(PreCompiledStatement preStat, Map<SqlArg, SqlObject> relatedObjs, Object voucher) throws CasketException;

	/**
	 * This operation builds a pre-compiled statement to delete all rows matching
	 * the first order conditions given by the set of sqlArgs and the binding
	 * operator. To select all rows in the given table one uses an empty set.
	 *
	 * @param tabAssignment - the {@link TableAssignment table assignment}.
	 * @param args          - the set of arguments defining the selection process.
	 * @param op            - the binding {@link SqlArg.OP operator}, defining how to combine
	 *                      the different {@link SqlArg arguments}.
	 * @return the {@link PreCompiledStatement pre-compiled statement}-
	 * @throws CasketException on error.
	 * @see SqlDatabase#delete(PreCompiledStatement, Map, Object)
	 */
	PreCompiledStatement mkDeleteStmt(TableAssignment tabAssignment, Set<SqlArg> args, SqlArg.OP op) throws CasketException;

	/**
	 * This operation deletes all rows matching the first order condition given by
	 * the set of sqlArgs and the operator used in the pre-compiled statement. This
	 * operation returns the primary keys of all deleted rows.
	 *
	 * @param preStat     - the {@link PreCompiledStatement pre-compiled statement} defining
	 *                    the selection.
	 * @param relatedObjs - the values for the selection.
	 * @param voucher     - the voucher of the current transaction.
	 * @return the primary keys of the deleted rows.
	 * @throws CasketException on error.
	 * @see SqlDatabase#mkDeleteStmt(TableAssignment, Set, SqlArg.OP)}
	 */
	List<SqlObject> delete(PreCompiledStatement preStat, Map<SqlArg, SqlObject> relatedObjs, Object voucher) throws CasketException;

	/**
	 * This operation creates an argument definition for the given column of the
	 * assigned table.
	 *
	 * @param assignment - the {@link TableAssignment table assignment}.
	 * @param columnName - the effected column name.
	 * @param cmp        - the {@link SqlArg.CMP comparator}.
	 * @return a single {@link SqlArg argument} combining columns and values to
	 * build suitable arguments for the pre-compiled expressions.
	 * @throws CasketException on error.
	 */
	SqlArg mkSqlArg(TableAssignment assignment, String columnName, SqlArg.CMP cmp) throws CasketException;

	/**
	 * This operation assigns an observer to a defined {@link TableAssignment table
	 * assignment}. Whenever something changes in the existing database table, an
	 * observer will be informed.
	 *
	 * @param obs - the new observer.
	 * @param tab - the observed {@link TableAssignment table assignment}.
	 * @throws CasketException on error.
	 */

	void attach(DatabaseObserver obs, TableAssignment tab) throws CasketException;

	/**
	 * This operation removes a former
	 * {@link SqlDatabase#attach(DatabaseObserver, TableAssignment) attachment}.
	 *
	 * @param obs - the observer to remove.
	 * @param tab -the observed {@link TableAssignment table assignment}.
	 * @throws CasketException on error.
	 */

	void detach(DatabaseObserver obs, TableAssignment tab) throws CasketException;

}
