package org.fuchss.objectcasket.sqlconnector.port;

import java.util.Set;

import org.fuchss.objectcasket.common.CasketException;

/**
 *
 * A generated pre-compiled statement to access the database. For further
 * informations:
 *
 * @see SqlDatabase#mkDeleteStmt(TableAssignment, Set, SqlArg.OP)
 * @see SqlDatabase#mkNewRowStmt(TableAssignment)
 * @see SqlDatabase#mkSelectStmt(TableAssignment, Set, SqlArg.OP)
 * @see SqlDatabase#mkUpdateRowStmt(TableAssignment, Set)
 *
 */
public interface PreCompiledStatement extends AutoCloseable {

	/**
	 * This operation releases the object. Further use is not possible.
	 *
	 * @throws CasketException
	 *             on error
	 */
	@Override
	public void close() throws CasketException;

	/**
	 * This operation returns the name of the primary key column.
	 *
	 * @return the name.
	 */
	public String pkName();

	/**
	 * This operation returns the name of the assigned table.
	 *
	 * @return the name.
	 */
	public String tableName();

	/**
	 * This operation checks whether the primary key is generated automatically or
	 * not.
	 *
	 * @return true iff the key is generated automatically.
	 */
	public boolean pkIsAutoincremented();

}
