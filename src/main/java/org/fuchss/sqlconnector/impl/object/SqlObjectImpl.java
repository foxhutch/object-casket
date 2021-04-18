package org.fuchss.sqlconnector.impl.object;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public abstract class SqlObjectImpl implements SqlObject {

	protected SqlObject.Type sqlType;

	@Override
	public String toSqlString() {
		return this.toString();
	}

	protected SqlObjectImpl(Object obj, SqlObject.Type sqlType) throws ConnectorException {
		this.sqlType = sqlType;
		this.setVal(obj);
	}

	protected abstract void setVal(Object obj) throws ConnectorException;

	@Override
	public <T> T get(Class<T> type) {
		return this.sqlType.get(this, type);
	}

	protected static class ObjectException extends ConnectorException {

		private static final long serialVersionUID = 1L;

		private ObjectException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			Incompatible("incompatible types");

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ConnectorException {
				ConnectorException.build(new ObjectException(this, arg));
			}

		}
	}

}
