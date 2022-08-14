package org.fuchss.objectcasket.tablemodule.port;

import java.io.Serializable;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

/**
 * The abstraction of a row inside a database table.
 *
 *
 */
public interface Row {

	/**
	 * This operation retrieves the value of a cell inside the row.
	 *
	 * @param <T>
	 *            - only serializable Java types can be stored in a database.
	 * @param column
	 *            - the name of the column.
	 * @param clazz
	 *            - the Java type mapped to the SQL type of the column.
	 * @return the value of the cell.
	 * @throws CasketException
	 *             on error. E.g. SQL type and Java type are incompatible. See
	 *             {@link StorageClass} for possible mappings.
	 */
	<T extends Serializable> T getValue(String column, Class<T> clazz) throws CasketException;

	/**
	 * This operation obtains the value of the primary key.
	 *
	 * @param <T>
	 *            - only serializable Java types can be stored in a database.
	 * @param clazz
	 *            - the Java type mapped to the SQL type of the primary key.
	 * @return the value of the primary key.
	 * @throws CasketException
	 *             on error. E.g. if SQL type and Java type are incompatible. See
	 *             {@link StorageClass} for possible mappings.
	 */
	<T extends Serializable> T getPk(Class<T> clazz) throws CasketException;

	/**
	 * This operation checks whether the values inside the row are modified and not
	 * yet stored in the assigned database.
	 *
	 * @return true iff there are modified and not yet stored values.
	 */
	boolean isDirty();

}
