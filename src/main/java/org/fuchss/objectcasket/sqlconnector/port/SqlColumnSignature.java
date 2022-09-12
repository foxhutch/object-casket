package org.fuchss.objectcasket.sqlconnector.port;

import org.fuchss.objectcasket.common.CasketException;

import java.io.Serializable;

/**
 * This interface provides all information to create and work on columns. This
 * includes types, values, and flags and also information about primary keys.
 */

public interface SqlColumnSignature {

	/**
	 * This operation returns the {@link StorageClass SQL type} associated with the
	 * column.
	 *
	 * @return The SQL type.
	 */
	StorageClass getType();

	/**
	 * This operation returns the Java type associated with the column.
	 *
	 * @return The assigned Java type.
	 */
	Class<? extends Serializable> getJavaType();

	// Values

	/**
	 * This operation returns the default value associated with the column.
	 *
	 * @return The default value.
	 */
	SqlObject getDefaultValue();

	/**
	 * This operation sets the value for the column. The {@link StorageClass SQL
	 * type} of the parameter and {@link SqlColumnSignature} object must be
	 * identical. Null-Objects are not allowed.
	 *
	 * @param value - the value for the column.
	 * @throws CasketException on error.
	 * @see SqlObject
	 */

	void setValue(SqlObject value) throws CasketException;

	/**
	 * This operation returns the {@link SqlObject SQL value} used by this
	 * {@link SqlColumnSignature}.
	 *
	 * @return the {@link SqlObject sql value}.
	 */
	SqlObject getValue();

	/**
	 * This operation will delete the value. So
	 * {@link SqlColumnSignature#getValue()} returns null.
	 */
	void clear();

	/**
	 * This operation sets the {@link Flag flags} associated with this column.
	 *
	 * @param flag the flag to set.
	 * @throws CasketException on error.
	 */
	void setFlag(Flag flag) throws CasketException;

	/**
	 * This operation resets the {@link Flag flags} associated with this column.
	 *
	 * @param flag - the flag to reset.
	 * @throws CasketException on error.
	 */
	void resetFlag(Flag flag) throws CasketException;

	/**
	 * This operation checks whether the column is a primary key column or not.
	 *
	 * @return true if this column is a primary key.
	 */
	boolean isPrimaryKey();

	/**
	 * This operation checks whether the column is a primary key column with an
	 * automatically generated value. This is only possible if the SQL type of the
	 * column is {@link StorageClass#LONG}.
	 *
	 * @return true if this column is a primary key, with an automatically created
	 * value.
	 */
	boolean isAutoIncrementedPrimaryKey();

	/**
	 * This operation checks whether the column can be null or not.
	 *
	 * @return true if this not possible to store Null inside the column.
	 */
	boolean notNull();

	/**
	 * The following SQL column annotations (flags) are currently supported.
	 *
	 * @see Flag#AUTOINCREMENT
	 * @see Flag#NOT_NULL
	 * @see Flag#PRIMARY_KEY
	 */

	enum Flag {
		/**
		 * The flag is used to annotate a primary key column.
		 */
		PRIMARY_KEY,
		/**
		 * The flag is used to annotate a primary key column as automatically generated.
		 * This is only possible if the type of the column is
		 * {@link StorageClass#INTEGER}.
		 */
		AUTOINCREMENT,
		/**
		 * This flag is used to declare that this column always has a non-null value.
		 * The flag "NOT_NULL" requires a not null default value. The flag "NOT NULL" is
		 * not allowed for primary keys.
		 */
		NOT_NULL
	}

}
