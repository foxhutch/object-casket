package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlChar extends SqlObjectImpl {

	SqlChar(Object obj) throws ConnectorException {
		super(obj, SqlObject.Type.CHAR);
	}

	protected String val;

	@Override
	public String toString() {
		return this.val;
	}

	@Override
	public Object get() {
		return this.val == null ? null : this.val.charAt(0);
	}

	@Override
	public void setVal(Object obj) throws ConnectorException {
		try {
			this.val = obj == null ? null : Character.toString(obj.toString().charAt(0));
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
			String x = (obj instanceof Character) ? Character.toString((Character) obj) : obj.toString();
			return (this.val == null) ? -1 : this.val.compareTo(x);
		} catch (ClassCastException e) {
			ObjectException.Error.Incompatible.build();
		}
		return 0;
	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			return new SqlChar(obj);
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.CHAR);
			} else {
				preparedStatement.setString(pos, this.val);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}