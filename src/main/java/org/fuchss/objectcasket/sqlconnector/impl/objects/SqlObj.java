package org.fuchss.objectcasket.sqlconnector.impl.objects;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectMaps;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.function.UnaryOperator;

/**
 * The base for the implementation of {@link SqlObject}.
 */
public abstract class SqlObj extends SqlObjectMaps {

	/**
	 * This operation creates a deep copy of the SqlObj.
	 *
	 * @return the copy.
	 */
	public abstract SqlObj duplicate();

	/**
	 * These operations return the value stored in this SqlObject.
	 *
	 * @return the stored object
	 */
	public abstract Serializable getVal();

	/**
	 * This operation inserts the value of the SqlObject into a
	 * {@link PreparedStatement} at the give position. The first parameter is 1, the
	 * second is 2, ...
	 *
	 * @param pos               - the position inside the preparedStatement.
	 * @param preparedStatement - the prepared statement.
	 * @throws CasketException on error.
	 */
	public abstract void prepareStatement(int pos, PreparedStatement preparedStatement) throws CasketException;

	/**
	 * This operation compares to SqlObjects of the same class.
	 *
	 * @param val - the object to compare.
	 * @return -1 if <strong>val</strong> is greater than this object. +1 if
	 * <strong>val</strong> is less than this object, 0 if both are equal.
	 * @throws CasketException on error.
	 */
	public abstract int compareTo(Object val) throws CasketException; // x.compareTo(y) //

	/**
	 * This operation checks whether the stored value is null or not.
	 *
	 * @return true iff the stored value inside the SqlObject is null.
	 */
	public boolean isNull() {
		return this.getVal() == null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T get(Class<T> type) {
		UnaryOperator<Object> cast = SqlObjectMaps.CAST.get(type);
		if (cast == null) {
			return null;
		}
		return (T) SqlObjectMaps.CAST.get(type).apply(this.getVal());
	}
}
