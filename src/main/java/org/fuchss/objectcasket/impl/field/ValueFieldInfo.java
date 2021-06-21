package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;

import javax.persistence.Column;

import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.sqlconnector.port.SqlObject;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class ValueFieldInfo extends FieldInfo {
	private SqlObject.Type columnSqlType;

	public ValueFieldInfo(Field field, Class<?> entityClass, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, tableName, Kind.VALUE, session);
		this.ownColumnInfo.setJavaType(field.getType());
		this.mkValueInfo();
	}

	private void mkValueInfo() throws ObjectCasketException {
		String fieldName = this.ownColumnInfo.getField().getName();
		String entityClassName = this.ownColumnInfo.getField().getDeclaringClass().getSimpleName();

		Column column = this.ownColumnInfo.getField().getAnnotation(Column.class);
		String columnDefinition = ((column == null) || (columnDefinition = column.columnDefinition()).isEmpty()) ? null : columnDefinition.toUpperCase();
		this.columnSqlType = SqlObject.Type.getDefaultType(this.ownColumnInfo.getJavaType(), columnDefinition);

		this.setFlags(column);

		this.checkValidity(fieldName, entityClassName, columnDefinition);
	}

	private void checkValidity(String fieldName, String entityClassName, String columnDefinition) throws ObjectCasketException {
		if ((this.columnSqlType == null) && (columnDefinition != null)) {
			FieldInfo.FieldException.Error.WrongColumnDefinition.build(columnDefinition, fieldName, entityClassName);
		}
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
		if (this.ownColumnInfo.isPK()) {
			this.addFlag(SqlPrototype.Flag.PRIMARY_KEY);
		}
		if (this.ownColumnInfo.isGenerated()) {
			this.addFlag(SqlPrototype.Flag.AUTOINCREMENT);
		}
	}

	private boolean checkPrimaryKey() {
		if (this.flags.contains(SqlPrototype.Flag.AUTOINCREMENT)) {
			return this.flags.contains(SqlPrototype.Flag.PRIMARY_KEY) ? SqlObject.Type.AUTOINCREMENT_JAVA_TYPES.contains(this.ownColumnInfo.getJavaType()) : false;
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
		return this.ownColumnInfo.isPK();
	}

	public SqlObject.Type getColumnSqlType() {
		return this.columnSqlType;
	}

}
