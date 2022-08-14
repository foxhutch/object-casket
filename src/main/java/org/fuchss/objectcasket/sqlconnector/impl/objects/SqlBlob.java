package org.fuchss.objectcasket.sqlconnector.impl.objects;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

/**
 * This class represents SQL BLOBs.
 */

public class SqlBlob extends SqlObj {

	private final Serializable val;

	private SqlBlob(Serializable obj) {
		this.val = obj == null ? null : SqlBlob.deepCopy(obj);
	}

	@Override
	public SqlBlob duplicate() {
		return new SqlBlob(this.val);
	}

	@Override
	public StorageClass getStorageClass() {
		return StorageClass.BLOB;
	}

	@Override
	public Serializable getVal() {
		return this.val;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Serializable> T get(Class<T> type) {
		if ((this.val != null) && type.isAssignableFrom(this.val.getClass())) {
			// DeepCopy is needed because of references
			return (T) SqlBlob.deepCopy(this.val);
		}
		return null;
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
		} catch (Exception exc) {
			throw new IllegalArgumentException(exc);
		}
	}

	@Override
	public void prepareStatement(int pos, PreparedStatement preparedStatement) throws CasketException {
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
			throw CasketException.build(exc);
		}
	}

	@Override
	public int compareTo(Object obj) throws CasketException {
		Serializable y = null;
		if (obj instanceof Serializable serializableObject) {
			y = serializableObject;
		}
		if ((y == null) && (obj != null)) {
			throw CasketError.INCOMPATIBLE_TYPES.build();
		}
		if (this.val == null)
			return (y == null ? 0 : -1);
		if (y == null)
			return 1;
		return (this.val.equals(y) ? 0 : -1);
	}

	/**
	 * This operation generates a BLOB-Object form a Java object.
	 *
	 * @param obj - the object to store as a BLOB.
	 * @return the {@link SqlObj}.
	 * @see StorageClass#BLOB
	 */
	public static SqlObj mkSqlObjectFromJava(Object obj) {
		if (obj == null) {
			return new SqlBlob(null);
		}
		if (obj instanceof Serializable serializableObj) {
			return new SqlBlob(serializableObj);
		}
		return null;
	}

	/**
	 * This operation generates a BLOB-Object from the stored value in the database.
	 *
	 * @param obj - the read value.
	 * @return the {@link SqlObj}.
	 */
	public static SqlObj mkSqlObjectFromSQL(Object obj) {
		if (obj == null) {
			return new SqlBlob(null);
		}
		if (obj instanceof byte[] byteArray) {
			byte[] bytes = Base64.getDecoder().decode(byteArray);
			try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes); //
					ObjectInputStream iStream = new ObjectInputStream(byteStream)) {
				return new SqlBlob((Serializable) iStream.readObject());
			} catch (IOException | ClassNotFoundException exc) {
				return null;
			}
		}
		return null;
	}

}
