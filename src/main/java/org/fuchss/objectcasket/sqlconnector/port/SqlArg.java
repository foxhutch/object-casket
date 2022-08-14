package org.fuchss.objectcasket.sqlconnector.port;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A data structure to easily select rows in tables. The objects are created
 * directly from the database itself via
 * {@link SqlDatabase#mkSqlArg(TableAssignment, String, CMP)}.
 *
 */
public interface SqlArg {

	/**
	 * For convenience: a unmodifiable empty set.
	 */
	final Set<SqlArg> EMPTY_SET = Collections.unmodifiableSet(new HashSet<>());

	/**
	 * For convenience: a unmodifiable empty map.
	 */
	final Map<SqlArg, SqlObject> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>());

	/**
	 * This operation returns the name of the table for which the argument is build.
	 *
	 * @return the table name
	 */
	String tableName();

	/**
	 * This operation returns the name of the column for which the argument is
	 * build.
	 *
	 * @return the column name
	 * @see SqlArg#tableName()
	 */
	String columnName();

	/**
	 * This operation returns the comparator which is used.
	 *
	 * @return the {@link SqlArg.CMP comparator}.
	 *
	 */
	CMP cmp();

	/**
	 * This operation returns the storage class for which the argument is build.
	 *
	 * @return the {@link StorageClass storage class}.
	 *
	 */

	StorageClass storageClass();

	/**
	 * All possible comparators:
	 *
	 * @see CMP#LESS
	 * @see CMP#GREATER
	 * @see CMP#EQUAL
	 * @see CMP#LESSEQ
	 * @see CMP#GREATEREQ
	 * @see CMP#UNEQUAL
	 */
	public enum CMP {
		/**
		 * less than
		 */
		LESS,
		/**
		 * greater than
		 */
		GREATER,
		/**
		 * equals to
		 */
		EQUAL,
		/**
		 * less than or equals to
		 */
		LESSEQ,
		/**
		 * greater than or equals to
		 */
		GREATEREQ,
		/**
		 * different to
		 */
		UNEQUAL;
	}

	/**
	 * To build more complex expressions arguments can be combined. Either with
	 * {@link OP#AND AND} or {@link OP#OR OR}.
	 *
	 */
	public enum OP {
		/**
		 * Every {@link SqlArg argument} must match.
		 */
		AND,
		/**
		 * Some {@link SqlArg arguments} must match.
		 */
		OR;
	}

}
