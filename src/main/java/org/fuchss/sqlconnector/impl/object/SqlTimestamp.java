package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlTimestamp extends SqlObjectImpl {

	// private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd
	// HH:mm:ss:SSS");

	SqlTimestamp(Object obj) throws ConnectorException {
		super(obj, SqlObject.Type.TIMESTAMP);

	}

	protected Long val;

	@Override
	public String toString() {
		if (this.val == null)
			return null;
		DateFormat defaultFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date date = new Date(this.val);
		return defaultFormatter.format(date);
	}

	@Override
	public String toSqlString() {
		return (this.val == null) ? "" : ("" + this.val);
	}

	private Date getTIMESTAMP() {
		return (this.val == null) ? null : new Date(this.val);
	}

	@Override
	public Object get() {
		return this.getTIMESTAMP();
	}

	@Override
	public void setVal(Object obj) throws ConnectorException {
		try {
			if (obj == null) {
				this.val = null;
			} else {
				if (obj instanceof Date) {
					this.val = ((Date) obj).getTime();
					return;
				}
				if (obj instanceof Long) {
					this.val = ((Long) obj).longValue();
					return;
				}
				ObjectException.Error.Incompatible.build();
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
			if (obj instanceof Date) {
				return this.val == null ? 1 : Long.compare(this.val, ((Date) obj).getTime());
			}
			if (obj instanceof Long) {
				return this.val == null ? 1 : Long.compare(this.val, ((Date) obj).getTime());
			}

			ObjectException.Error.Incompatible.build();

		} catch (ClassCastException e) {
			ObjectException.Error.Incompatible.build();
		}
		return 0;
	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObject(Object obj) throws ConnectorException {
			return new SqlTimestamp(obj);
		}

	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.TIMESTAMP);
			} else {
				preparedStatement.setDate(pos, new java.sql.Date(this.val));
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}