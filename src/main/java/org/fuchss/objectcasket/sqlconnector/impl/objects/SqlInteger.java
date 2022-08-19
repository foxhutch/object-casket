package org.fuchss.objectcasket.sqlconnector.impl.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.fuchss.objectcasket.common.CasketError.CE3;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

/**
 * This class represents SQL INTEGERs.
 */
public class SqlInteger extends SqlObj {

	private final Long val;

	private SqlInteger(Long obj) {
		this.val = obj;
	}

	@Override
	public SqlInteger duplicate() {
		return new SqlInteger(this.val);
	}

	@Override
	public StorageClass getStorageClass() {
		return StorageClass.INTEGER;
	}

	@Override
	public Long getVal() {
		return this.val;
	}

	@Override
	public String toString() {
		return (this.val == null) ? null : "" + this.val;
	}

	@Override
	public int compareTo(Object obj) throws CasketException {
		Long y = null;
		if (Util.isNumber(obj)) {
			y = ((Number) obj).longValue();
		}
		if ((obj instanceof Boolean aBoolean)) {
			y = Boolean.TRUE.equals(aBoolean) ? 1L : 0L;
		}
		if ((obj instanceof Date aDate)) {
			y = aDate.getTime();
		}
		if ((y == null) && (obj != null)) {
			throw CE3.INCOMPATIBLE_SQL_TYPE.defaultBuild(StorageClass.INTEGER, obj, obj.getClass());
		}
		return Util.compare(this.val, y);
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws CasketException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.INTEGER);
			} else {
				preparedStatement.setObject(pos, this.val, java.sql.Types.INTEGER);
			}
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	/**
	 * This operation generates an INTEGER-Object form a Java object.
	 *
	 * @param obj
	 *            - the object to store as an INTEGER.
	 * @return the {@link SqlObj}.
	 * @see StorageClass#INTEGER
	 */
	public static SqlObj mkSqlObjectFromJava(Object obj) {
		return SqlInteger.mkSqlObject(obj);
	}

	/**
	 * This operation generates an INTEGER-Object from the stored value in the
	 * database.
	 *
	 * @param obj
	 *            - the read value.
	 * @return the {@link SqlObj}.
	 */
	public static SqlObj mkSqlObjectFromSQL(Object obj) {
		return SqlInteger.mkSqlObject(obj);
	}

	private static SqlObj mkSqlObject(Object obj) {
		if (obj == null) {
			return new SqlInteger(null);
		}
		if ((obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Short) || (obj instanceof Byte)) {
			return new SqlInteger(((Number) obj).longValue());
		}
		if (obj instanceof Boolean aBoolean) {
			return new SqlInteger(Boolean.TRUE.equals(aBoolean) ? 1L : 0L);
		}
		if (obj instanceof Date aDate) {
			return new SqlInteger(aDate.getTime());
		}
		return null;
	}

}