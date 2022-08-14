package org.fuchss.objectcasket.sqlconnector.port;

import java.util.Set;

/**
 * This interface provides all information to access an SQL table or a part of
 * a table.
 *
 * @see SqlDatabase
 */
public interface TableAssignment {

	/**
	 * This operation returns the name of the table.
	 *
	 * @return the name of the table inside the database.
	 */
	String tableName();

	/**
	 * This operation returns the name of the primary key column.
	 *
	 * @return the name of the column which is the primary key.
	 */
	String pkName();

	/**
	 * This operation returns the names of the columns inside the table.
	 *
	 * @return the name of all columns.
	 */
	Set<String> columnNames();

	/**
	 * These operations return the {@link StorageClass SQL type} of the given
	 * column.
	 *
	 * @param columnName - the name of the column.
	 * @return the {@link StorageClass SQL type}.
	 */
	StorageClass storageClass(String columnName);
}
