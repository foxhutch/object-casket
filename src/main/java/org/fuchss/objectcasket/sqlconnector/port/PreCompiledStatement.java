package org.fuchss.objectcasket.sqlconnector.port;

import org.fuchss.objectcasket.common.CasketException;

import java.util.Set;

/**
 * A generated pre-compiled statement to access the database. For further
 * information:
 *
 * @see SqlDatabase#mkDeleteStmt(TableAssignment, Set, SqlArg.OP)
 * @see SqlDatabase#mkNewRowStmt(TableAssignment)
 * @see SqlDatabase#mkSelectStmt(TableAssignment, Set, SqlArg.OP)
 * @see SqlDatabase#mkUpdateRowStmt(TableAssignment, Set)
 */
public interface PreCompiledStatement extends AutoCloseable {

	/**
	 * This operation releases the object. Further use is not possible.
	 *
	 * @throws CasketException on error
	 */
	@Override
	void close() throws CasketException;

	/**
	 * This operation returns the name of the primary key column.
	 *
	 * @return the name.
	 */
	String pkName();

	/**
	 * This operation returns the name of the assigned table.
	 *
	 * @return the name.
	 */
	String tableName();

	/**
	 * This operation checks whether the primary key is generated automatically or
	 * not.
	 *
	 * @return true iff the key is generated automatically.
	 */
	boolean pkIsAutoIncremented();

}
