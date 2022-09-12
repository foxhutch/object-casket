package org.fuchss.objectcasket.sqlconnector.impl.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fuchss.objectcasket.common.CasketError.CE3;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

/**
 * This class represents SQL REALs.
 */

public class SqlDouble extends SqlObj {

	private final Double val;

	private SqlDouble(Double obj) {
		this.val = obj;
	}

	@Override
	public SqlDouble duplicate() {
		return new SqlDouble(this.val);
	}

	@Override
	public StorageClass getStorageClass() {
		return StorageClass.DOUBLE;
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
			throw CE3.INCOMPATIBLE_SQL_TYPE.defaultBuild(StorageClass.DOUBLE, obj, obj.getClass());
		}
		return (this.val == null) ? (y == null ? 0 : -1) : (y == null ? 1 : this.val.compareTo(y));
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws CasketException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.DOUBLE);
			} else {
				preparedStatement.setObject(pos, this.val, java.sql.Types.DOUBLE);
			}
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	/**
	 * his operation generates a REAL-Object form a Java object.
	 *
	 * @param obj
	 *            - the object to store as a REAL.
	 * @return the {@link SqlObj}
	 * @see StorageClass#DOUBLE
	 */
	public static SqlObj mkSqlObjectFromJava(Object obj) {
		return SqlDouble.mkSqlObject(obj);
	}

	/**
	 * This operation generates a REAL-Object from the stored value in the database.
	 *
	 * @param obj
	 *            - the read value.
	 * @return the {@link SqlObj}.
	 */
	public static SqlObj mkSqlObjectFromSQL(Object obj) {
		return SqlDouble.mkSqlObject(obj);
	}

	private static SqlObj mkSqlObject(Object obj) {

		if (obj == null) {
			return new SqlDouble(null);
		}
		if ((obj instanceof Float) || (obj instanceof Double)) {
			return new SqlDouble(((Number) obj).doubleValue());
		}
		return null;
	}

}