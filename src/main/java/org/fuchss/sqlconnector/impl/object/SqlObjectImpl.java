package org.fuchss.sqlconnector.impl.object;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public abstract class SqlObjectImpl implements SqlObject {

	protected SqlObject.Type sqlType;

	protected SqlObjectImpl(SqlObject.Type sqlType) throws ConnectorException {
		this.sqlType = sqlType;
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
