package org.fuchss.objectcasket.sqlconnector.impl.database;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

class SqlValidator {

	@FunctionalInterface
	private interface DefaultValueExtractor {
		Object apply(ResultSet t) throws SQLException;
	}

	private static final Map<StorageClass, DefaultValueExtractor> extractorMap = new EnumMap<>(StorageClass.class);

	static {
		SqlValidator.extractorMap.put(StorageClass.LONG, (r -> r.getObject(13) == null ? null : (Long) r.getLong(13)));
		SqlValidator.extractorMap.put(StorageClass.DOUBLE, (r -> r.getObject(13) == null ? null : (Double) r.getDouble(13)));
		SqlValidator.extractorMap.put(StorageClass.TEXT, (r -> r.getObject(13) == null ? null : (String) r.getString(13)));
		SqlValidator.extractorMap.put(StorageClass.BLOB, (r -> r.getObject(13) == null ? null : (String) r.getString(13)));
	}

	private final String columnName;
	private final boolean isPk;
	private final StorageClass type;
	private Object defaultObj;

	SqlValidator(ResultSet resultSet, String pkName, SqlDialect dialect) throws SQLException {
		this.columnName = resultSet.getString("COLUMN_NAME");
		String typeString = resultSet.getString("TYPE_NAME");
		this.type = dialect.baseTypeStorageClass(typeString);
		this.defaultObj = SqlValidator.extractorMap.get(this.type).apply(resultSet);
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
