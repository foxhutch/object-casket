package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class ValueFieldInfo extends FieldInfo {
	private boolean isPrimaryKey;
	private SqlObject.Type columnSqlType;

	public ValueFieldInfo(Field field, Class<?> entityClass, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, Kind.VALUE, session);
		this.tableName = tableName;
		this.columnType = this.field.getType();
		this.mkValueInfo();
	}

	private void mkValueInfo() throws ObjectCasketException {
		String fieldName = this.field.getName();
		String entityClassName = this.field.getDeclaringClass().getSimpleName();
		Column column = this.field.getAnnotation(Column.class);
		this.columnName = ((column == null) || (this.columnName = column.name()).isEmpty()) ? this.field.getName() : this.columnName;
		String columnDefinition = ((column == null) || (columnDefinition = column.columnDefinition()).isEmpty()) ? null : columnDefinition.toUpperCase();
		this.setFlags(column);
		this.columnSqlType = (columnDefinition == null) ? SqlObject.Type.getDefaultType(this.columnType) : SqlObject.Type.valueOf(columnDefinition);
		if (this.columnSqlType == null) {
			FieldInfo.FieldException.Error.WrongValueField.build(fieldName, entityClassName);
		}
		if (!this.checkPrimaryKey()) {
			FieldInfo.FieldException.Error.IllegalPrimaryKey.build(fieldName, entityClassName, ValueFieldInfo.pkClasses(), ValueFieldInfo.autoincremtenClasses(), ValueFieldInfo.pkSqlTypes());
		}
	}

	private void setFlags(Column column) {
		if ((column != null) && !column.nullable()) {
			this.addFlag(SqlPrototype.Flag.NOT_NULL);
		}
		if ((column != null) && column.unique()) {
			this.addFlag(SqlPrototype.Flag.UNIQUE);
		}
		if (this.isPrimaryKey = (this.field.getAnnotation(Id.class) != null)) {
			this.addFlag(SqlPrototype.Flag.PRIMARY_KEY);
		}
		if (this.field.getAnnotation(GeneratedValue.class) != null) {
			this.addFlag(SqlPrototype.Flag.AUTOINCREMENT);
		}
	}

	private boolean checkPrimaryKey() {
		if (this.flags.contains(SqlPrototype.Flag.AUTOINCREMENT)) {
			return this.flags.contains(SqlPrototype.Flag.PRIMARY_KEY) ? SqlObject.Type.AUTOINCREMENT_JAVA_TYPES.contains(this.columnType) : false;
		}
		if (this.flags.contains(SqlPrototype.Flag.PRIMARY_KEY)) {
			return SqlObject.Type.PK_SQL_TYPES.contains(this.columnSqlType);
		}
		return true;
	}

	private static String pkClasses() {

		String res = "";
		for (Class<?> pkClass : SqlObject.Type.PK_JAVA_TYPES) {
			res += ((res.equals("")) ? "[" : ", ") + pkClass.getSimpleName();
		}
		return res + "]";
	}

	private static String pkSqlTypes() {

		String res = "";
		for (SqlObject.Type type : SqlObject.Type.PK_SQL_TYPES) {
			res += ((res.equals("")) ? "[" : ", ") + type.name();
		}
		return res + "]";
	}

	private static String autoincremtenClasses() {
		String res = "";
		for (Class<?> aiClass : SqlObject.Type.AUTOINCREMENT_JAVA_TYPES) {
			res += ((res.equals("")) ? "[" : ", ") + aiClass.getSimpleName();
		}
		return res + "]";
	}

	public boolean isPK() {
		return this.isPrimaryKey;
	}

	public SqlObject.Type getColumnSqlType() {
		return this.columnSqlType;
	}

}
