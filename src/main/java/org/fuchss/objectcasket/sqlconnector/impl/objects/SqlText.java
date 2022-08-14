package org.fuchss.objectcasket.sqlconnector.impl.objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

/**
 * This class represents SQL TEXTs.
 *
 */
public class SqlText extends SqlObj {

	private String val;

	private SqlText(String obj) {
		this.val = obj == null ? obj : "" + obj;
	}

	@Override
	public SqlText duplicate() {
		return new SqlText(this.val);
	}

	@Override
	public StorageClass getStorageClass() {
		return StorageClass.TEXT;
	}

	@Override
	public String toString() {
		return this.val;
	}

	@Override
	public int compareTo(Object obj) throws CasketException {
		String y = null;
		if (obj instanceof String aString) {
			y = aString;
		}
		if (obj instanceof Character) {
			y = "" + (obj);
		}
		if ((y == null) && (obj != null)) {
			throw CasketError.INCOMPATIBLE_TYPES.build();
		}
		return Util.compare(this.val, y);
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws CasketException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.VARCHAR);
			} else {
				preparedStatement.setObject(pos, this.val, java.sql.Types.VARCHAR);
			}
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	@Override
	public String getVal() {
		return this.val;
	}

	/**
	 * This operation generates TEXT form a Java object.
	 *
	 * @see StorageClass#TEXT
	 * @param obj
	 *            - the object to store as TEXT.
	 * @return the {@link SqlObj}.
	 */
	public static SqlObj mkSqlObjectFromJava(Object obj) {
		return SqlText.mkSqlObject(obj);
	}

	/**
	 * his operation generates a TEXT-Object from the stored value in the database.
	 *
	 * @param obj
	 *            - the read value.
	 * @return the {@link SqlObj}.
	 */
	public static SqlObj mkSqlObjectFromSQL(Object obj) {
		return SqlText.mkSqlObject(obj);
	}

	private static SqlObj mkSqlObject(Object obj) {
		if (obj == null) {
			return new SqlText(null);
		}
		if (obj instanceof String aString) {
			return new SqlText(aString);
		}
		if (obj instanceof Character) {
			return new SqlText("" + (obj));
		}
		return null;

	}

}
