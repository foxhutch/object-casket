package org.fuchss.objectcasket.sqlconnector.impl.objects;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class represents SQL REALs.
 */

public class SqlReal extends SqlObj {

	private final Double val;

	private SqlReal(Double obj) {
		this.val = obj;
	}

	@Override
	public SqlReal duplicate() {
		return new SqlReal(this.val);
	}

	@Override
	public StorageClass getStorageClass() {
		return StorageClass.REAL;
	}

	@Override
	public Double getVal() {
		return this.val;
	}

	@Override
	public String toString() {
		return this.val == null ? null : ("" + this.val);
	}

	@Override
	@SuppressWarnings("java:S3358")
	public int compareTo(Object obj) throws CasketException {
		Double y = null;
		if ((obj instanceof Double) || (obj instanceof Float)) {
			y = ((Number) obj).doubleValue();
		}
		if ((y == null) && (obj != null)) {
			throw CasketError.INCOMPATIBLE_TYPES.build();
		}
		return (this.val == null) ? (y == null ? 0 : -1) : (y == null ? 1 : this.val.compareTo(y));
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws CasketException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.REAL);
			} else {
				preparedStatement.setObject(pos, this.val, java.sql.Types.REAL);
			}
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	/**
	 * his operation generates a REAL-Object form a Java object.
	 *
	 * @param obj - the object to store as a REAL.
	 * @return the {@link SqlObj}
	 * @see StorageClass#REAL
	 */
	public static SqlObj mkSqlObjectFromJava(Object obj) {
		return SqlReal.mkSqlObject(obj);
	}

	/**
	 * This operation generates a REAL-Object from the stored value in the database.
	 *
	 * @param obj - the read value.
	 * @return the {@link SqlObj}.
	 */
	public static SqlObj mkSqlObjectFromSQL(Object obj) {
		return SqlReal.mkSqlObject(obj);
	}

	private static SqlObj mkSqlObject(Object obj) {

		if (obj == null) {
			return new SqlReal(null);
		}
		if ((obj instanceof Float) || (obj instanceof Double)) {
			return new SqlReal(((Number) obj).doubleValue());
		}
		return null;
	}

}