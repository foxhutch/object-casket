package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlFloat extends SqlObjectImpl {

	SqlFloat(Object obj) throws ConnectorException {
		super(obj, SqlObject.Type.FLOAT);
	}

	protected Float val;

	@Override
	public String toString() {
		return (this.val == null) ? null : ("" + this.val.floatValue());
	}

	@Override
	public Object get() {
		return this.val;
	}

	@Override
	public void setVal(Object obj) throws ConnectorException {
		try {
			if (obj == null) {
				this.val = null;
			} else {
				if (obj instanceof Double) {
					this.val = this.mkFloat((Double) obj);
				} else {
					this.val = (Float) obj;
				}
			}
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
			Float x = (obj instanceof Double) ? this.mkFloat((Double) obj) : (Float) obj;
			return (this.val == null) ? x.compareTo(0.0f) : this.val.compareTo(x);

		} catch (ClassCastException e) {
			ObjectException.Error.Incompatible.build();
		}
		return 0;
	}

	private Float mkFloat(Double d) {
		double dd = d.doubleValue();
		return (float) dd;
	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			return new SqlFloat(obj);
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.FLOAT);
			} else {
				preparedStatement.setFloat(pos, this.val);
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}