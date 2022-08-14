package org.fuchss.objectcasket.sqlconnector.port;

import java.io.Serializable;

/**
 * A SqlObject represent the data stored in a cell of an SQL table.
 *
 */
public interface SqlObject {

	/**
	 * This operation converts an element storable in a database to a suitable
	 * object of the corresponding Java class or type.
	 *
	 * @param <T>
	 *            - only {@link Serializable serializable} values can be stored in a
	 *            cell.
	 * @param type
	 *            - the Java class or type to which the object should be converted.
	 * @return the converted object. Or null if no conversion is possible.
	 */
	<T extends Serializable> T get(Class<T> type);

	/**
	 * This operation returns the {@link StorageClass SQL type} of the object. This
	 * type defines the possible conversions.
	 *
	 * @return the {@link StorageClass SQL type} of the object.
	 */
	public StorageClass getStorageClass();

}
