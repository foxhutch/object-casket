package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlInteger extends SqlObjectImpl {

	SqlInteger(Object obj) throws ConnectorException {
		super(obj, SqlObject.Type.INTEGER);
	}

	protected Long val;

	@Override
	public String toString() {
		return (this.val == null) ? null : ("" + this.val);
	}

	@Override
	public void setVal(Object obj) throws ConnectorException {
		try {
			Number tmp = (Number) obj;
			this.val = (obj == null) ? null : tmp.longValue();
		} catch (ClassCastException e) {
			ObjectException.Error.Incompatible.build();
		}
	}

	@Override
	public int compareTo(Object obj) throws ConnectorException {

		try {
			if (obj == null) {
				return this.val == null ? 0 : 1;
			}
			Long x = ((Number) obj).longValue();
			return (this.val == null) ? x.compareTo(0L) : this.val.compareTo(x);

		} catch (ClassCastException e) {
			ObjectException.Error.Incompatible.build();
		}
		return 0;
	}

	@Override
	public Object get() {
		return this.val;
	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			return new SqlInteger(obj);
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.INTEGER);
			} else {
				preparedStatement.setLong(pos, this.val);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}
