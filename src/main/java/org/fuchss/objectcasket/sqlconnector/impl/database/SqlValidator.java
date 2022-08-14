package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

class SqlValidator {

	@FunctionalInterface
	private interface DefaultValueExtracter {
		Object apply(ResultSet t) throws SQLException;
	}

	private static final Map<StorageClass, DefaultValueExtracter> extracterMap = new EnumMap<>(StorageClass.class);
	static {
		SqlValidator.extracterMap.put(StorageClass.INTEGER, (r -> r.getObject(13) == null ? null : (Long) r.getLong(13)));
		SqlValidator.extracterMap.put(StorageClass.REAL, (r -> r.getObject(13) == null ? null : (Double) r.getDouble(13)));
		SqlValidator.extracterMap.put(StorageClass.TEXT, (r -> r.getObject(13) == null ? null : (String) r.getString(13)));
		SqlValidator.extracterMap.put(StorageClass.BLOB, (r -> r.getObject(13) == null ? null : (String) r.getString(13)));
	}

	private String columnName;
	private boolean isPk;
	private StorageClass type;
	private Object defaultObj;

	SqlValidator(ResultSet resultSet, String pkName, SqlDialect dialect) throws SQLException {
		this.columnName = resultSet.getString("COLUMN_NAME");
		String typeString = resultSet.getString("TYPE_NAME");
		this.type = dialect.baseTypeStorageClass(typeString);
		this.defaultObj = SqlValidator.extracterMap.get(this.type).apply(resultSet);
		if ((this.defaultObj != null) && ((this.type == StorageClass.TEXT) || (this.type == StorageClass.BLOB))) {
			String quoted = (String) this.defaultObj;
			this.defaultObj = quoted.substring(1, quoted.length() - 1);
		}
		this.isPk = this.columnName.equals(pkName);
	}

	boolean isPK() {
		return this.isPk;
	}

	boolean validate(SqlColumnSignature sqlPrototype) throws CasketException {
		if (!this.isPk && (sqlPrototype == null))
			return true;
		boolean isValid = false;
		if (sqlPrototype instanceof SqlColumnSignatureImpl sqlPrototypeImpl) {
			isValid = sqlPrototypeImpl.isPrimaryKey() == this.isPk;
			isValid = isValid && (sqlPrototypeImpl.getType() == this.type);
			isValid = isValid && (sqlPrototypeImpl.getDefaultValue().compareTo(this.defaultObj) == 0);
		}
		return isValid;
	}

	String getColumnName() {
		return this.columnName;
	}
}
