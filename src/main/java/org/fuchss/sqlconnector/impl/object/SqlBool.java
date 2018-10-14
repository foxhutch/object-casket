package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlBool extends SqlObjectImpl {

	SqlBool(Object obj) throws ConnectorException {
		super(obj, SqlObject.Type.BOOL);
	}

	protected Boolean val;

	@Override
	public String toString() {
		return (this.val == null) ? null : (this.val ? "1" : "0");
	}

	@Override
	public Object get() {
		return this.val;
	}

	@Override
	public void setVal(Object obj) throws ConnectorException {
		try {
			this.val = (obj instanceof Integer) ? (((Integer) obj) == 1) : (Boolean) obj;

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
			Boolean x = (obj instanceof Integer) ? (((Integer) obj) == 1) : (Boolean) obj;
			return (this.val == null) ? x.compareTo(false) : this.val.compareTo(x);

		} catch (ClassCastException e) {
			ObjectException.Error.Incompatible.build();
		}
		return 0;

	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			return new SqlBool(obj);
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.BOOLEAN);
			} else {
				preparedStatement.setBoolean(pos, this.val);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}
