package org.fuchss.sqlconnector.impl.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlTimestamp extends SqlObjectImpl {

	private static final DateFormat FORMATTER = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	SqlTimestamp(Object obj) throws ConnectorException {
		super(obj, SqlObject.Type.TIMESTAMP);

	}

	protected String val;

	@Override
	public String toString() {
		return this.val;
	}

	@Override
	public String toSqlString() {
		return this.val == null ? null : "'" + this.val + "'";
	}

	private Date getTIMESTAMP() {
		try {
			return (this.val == null) ? null : SqlTimestamp.FORMATTER.parse(this.val);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
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
				if (obj instanceof String) {
					this.setStringVal((String) obj);
					return;
				}
				if (obj instanceof Date) {
					this.setDateVal((Date) obj);
					return;
				}
				if (obj instanceof Long) {
					this.setLongVal((Long) obj);
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
			String x = null;

			if (obj instanceof String) {
				x = (String) obj;
			}
			if (obj instanceof Date) {
				x = SqlTimestamp.FORMATTER.format((Date) obj);
				x = (x == null) ? null : "'" + x + "'";
			}
			if (obj instanceof Long) {
				x = SqlTimestamp.FORMATTER.format(new Date((Long) obj));
				x = (x == null) ? null : "'" + x + "'";
			}

			return (this.val == null) ? -1 : this.toSqlString().compareTo(x);

		} catch (ClassCastException e) {
			ObjectException.Error.Incompatible.build();
		}
		return 0;
	}

	private void setStringVal(String date) {
		this.val = "" + date;
	}

	private void setLongVal(Long ldate) {
		Date date = new Date(ldate);
		this.setDateVal(date);
	}

	private void setDateVal(Date ddate) {
		this.val = SqlTimestamp.FORMATTER.format(ddate);
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
				preparedStatement.setDate(pos, new java.sql.Date(this.getTIMESTAMP().getTime()));
			}
		} catch (SQLException exc) {
			ConnectorException.build(exc);
		}
	}

}