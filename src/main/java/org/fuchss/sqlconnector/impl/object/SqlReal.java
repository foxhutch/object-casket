package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlReal extends SqlObjectImpl {

	SqlReal(Object obj) throws ConnectorException {
		super(obj, SqlObject.Type.REAL);
	}

	protected Double val;

	@Override
	public String toString() {
		return (this.val == null) ? null : ("" + this.val);
	}

	@Override
	public Object get() {
		return this.val;
	}

	@Override
	public void setVal(Object obj) throws ConnectorException {
		try {
			this.val = (Double) obj;
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
			Double x = (Double) obj;
			return (this.val == null) ? x.compareTo(0.0) : this.val.compareTo(x);

		} catch (ClassCastException e) {
			ObjectException.Error.Incompatible.build();
		}
		return 0;

	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			return new SqlReal(obj);
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.REAL);
			} else {
				preparedStatement.setDouble(pos, this.val);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}
