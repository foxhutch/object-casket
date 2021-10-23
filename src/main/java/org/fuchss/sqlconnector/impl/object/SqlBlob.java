package org.fuchss.sqlconnector.impl.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import org.fuchss.sqlconnector.port.ConnectorException;
import org.fuchss.sqlconnector.port.SqlObject;

public class SqlBlob extends SqlObjectImpl {

	protected Serializable val;

	SqlBlob(Serializable obj) throws ConnectorException {
		super(SqlObject.Type.BLOB);
		try {
			this.val = SqlBlob.deepCopy(obj);
		} catch (Exception e) {
			ConnectorException.build(e);
		}
	}

	// Create a deep copy for BLOBs
	private static Serializable deepCopy(Serializable s) {
		try {
			byte[] bytes = null;
			try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); //
					ObjectOutputStream oStream = new ObjectOutputStream(byteStream)) {
				oStream.writeObject(s);
				oStream.flush();
				bytes = byteStream.toByteArray();
			}
			try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes); //
					ObjectInputStream iStream = new ObjectInputStream(byteStream)) {
				return (Serializable) iStream.readObject();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> type) {
		if ((this.val != null) && type.isAssignableFrom(this.val.getClass())) {
			// DeepCopy is needed because of references
			return (T) SqlBlob.deepCopy(this.val);
		}
		return null;
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws ConnectorException {
		try {
			if (this.val == null) {
				preparedStatement.setNull(pos, java.sql.Types.BLOB);
			} else {
				try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); //
						ObjectOutputStream oStream = new ObjectOutputStream(byteStream)) {
					oStream.writeObject(this.val);
					oStream.flush();
					byte[] base64 = Base64.getEncoder().encode(byteStream.toByteArray());
					preparedStatement.setObject(pos, base64, java.sql.Types.BLOB);
				}
			}
		} catch (SQLException | IOException exc) {
			ConnectorException.build(exc);
		}
	}

	@Override
	public int compareTo(Object val) throws ConnectorException {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	static class SqlBuilder implements SqlObjectBuilder {

		@Override
		public SqlObjectImpl mkSqlObjectFromJava(Object obj) throws ConnectorException {
			if (obj == null) {
				return new SqlBlob(null);
			}
			if (obj instanceof Serializable) {
				return new SqlBlob((Serializable) obj);
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}

		@Override
		public SqlObjectImpl mkSqlObjectFromSQL(Object obj) throws ConnectorException {
			if (obj == null) {
				return new SqlBlob(null);
			}
			if (obj instanceof byte[]) {
				byte[] bytes = Base64.getDecoder().decode((byte[]) obj);
				try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes); //
						ObjectInputStream iStream = new ObjectInputStream(byteStream)) {
					return new SqlBlob((Serializable) iStream.readObject());
				} catch (IOException | ClassNotFoundException exc) {
					ConnectorException.build(exc);
				}
			}
			ObjectException.Error.Incompatible.build();
			return null;
		}
	}

}
