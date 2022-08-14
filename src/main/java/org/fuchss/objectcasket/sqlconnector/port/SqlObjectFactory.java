package org.fuchss.objectcasket.sqlconnector.port;

import java.io.Serializable;

import org.fuchss.objectcasket.common.CasketException;

/**
 * Main interface to create objects that can be stored in cells of tables inside
 * a database.
 *
 */

public interface SqlObjectFactory {

	/**
	 * This operation creates an {@link SqlColumnSignature} with a proper Java
	 * default value.
	 *
	 * @param <T>
	 *            - only {@link Serializable serializable} values can be stored in a
	 *            cell.
	 * @param type
	 *            - the {@link StorageClass SQL type} of the column.
	 * @param javaType
	 *            - the Java type which should be stored in this column.
	 * @param defaultValue
	 *            - the default value, which should be used if an new row will be
	 *            created.
	 * @return a {@link SqlColumnSignature signature object} which can be
	 *         individualized further.
	 * @throws CasketException
	 *             on error. Maybe SQL type and Java type are incompatible. See
	 *             {@link StorageClass} for further information on compatible types.
	 *
	 */
	<T extends Serializable> SqlColumnSignature mkColumnSignature(StorageClass type, Class<T> javaType, T defaultValue) throws CasketException;

	/**
	 * This operation converts a given Java object into an {@link SqlObject}.
	 *
	 * @param <T>
	 *            - the type of the Java object.
	 * @param type
	 *            - the corresponding SQL type.
	 * @param obj
	 *            - the object to convert.
	 * @return the created SQL object.
	 * @throws CasketException
	 *             on error.
	 */
	<T extends Serializable> SqlObject mkSqlObject(StorageClass type, T obj) throws CasketException;

	/**
	 * This operation duplicates an existing {@link SqlObject}.
	 *
	 * @param sqlObj
	 *            - the {@link SqlObject}
	 * @return a copy of the {@link SqlObject}. Especially the internally stored
	 *         value was duplicated. So any modification of the original value has
	 *         no effect on the duplicate. duplicated.
	 * @throws CasketException
	 *             on error.
	 */
	SqlObject duplicate(SqlObject sqlObj) throws CasketException;

}
