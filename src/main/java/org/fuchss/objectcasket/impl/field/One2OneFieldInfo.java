package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class One2OneFieldInfo extends FieldInfo {
	private Class<?> foreignClass;
	private String foreignTableName;

	private String remoteFkColumnName;
	private Field remoteFkField;
	private Class<?> myPkType;

	public One2OneFieldInfo(Field field, Class<?> entityClass, Class<?> myPkType, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, Kind.ONE2ONE, session);
		this.tableName = tableName;
		this.myPkType = myPkType;
		this.checkField();
		Field pkField = this.session.getEntityFactory(this.field.getType()).getPkField();
		if (pkField == null) {
			FieldException.Error.MissingPrimaryKey.build(entityClass.getSimpleName());
		}
		this.columnType = pkField.getType();
		this.foreignClassAndTable();
		this.columnNameAndFlags();
		this.remoteFK();
	}

	private void checkField() throws ObjectCasketException {
		if (!this.field.getType().isAnnotationPresent(Entity.class)) {
			String foreignClassName = this.field.getType().getSimpleName();
			String fieldName = this.field.getName();
			String entityClassName = this.field.getDeclaringClass().getSimpleName();
			FieldException.Error.WrongEntity.build(foreignClassName, fieldName, entityClassName);
		}
	}

	private void foreignClassAndTable() {
		this.foreignClass = this.field.getType();
		Table table = this.foreignClass.getAnnotation(Table.class);
		this.foreignTableName = (table == null || (this.foreignTableName = table.name()).isEmpty()) ? this.foreignClass.getSimpleName() : this.foreignTableName;

	}

	private void columnNameAndFlags() {
		Column column = this.field.getAnnotation(Column.class);
		this.columnName = (column == null || (this.columnName = column.name()).isEmpty()) ? "id_" + this.foreignTableName + "_" + this.field.getName() : this.columnName;
		this.addFlag(SqlPrototype.Flag.UNIQUE);
		if (column != null && !column.nullable()) {
			this.addFlag(SqlPrototype.Flag.NOT_NULL);
		}
	}

	private void remoteFK() throws ObjectCasketException {
		JoinColumn joinColumn = this.field.getAnnotation(JoinColumn.class);
		this.remoteFkColumnName = (joinColumn == null || (this.remoteFkColumnName = joinColumn.name()).isEmpty()) ? "id_" + this.tableName + "_" + this.field.getName() : this.remoteFkColumnName;
		this.remoteFkField = this.remoteFKfield();
		if (this.remoteFkField == null) {
			Set<FieldInfo> anonymousFKfieldInfos = this.session.getAnonymousFKfieldInfosMap().get(this.foreignClass);
			if (anonymousFKfieldInfos == null) {
				this.session.getAnonymousFKfieldInfosMap().put(this.foreignClass, anonymousFKfieldInfos = new HashSet<>());
			}
			anonymousFKfieldInfos.add(this);
		} else {
			if (!this.remoteFkField.isAnnotationPresent(OneToOne.class)) {
				FieldException.Error.WrongForeignAnnotation.build(this.remoteFkField.getName(), this.foreignClass.getSimpleName(), this.field.getName(), this.columnType.getSimpleName());
			}
		}

	}

	private Field remoteFKfield() {
		for (Field field : this.foreignClass.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			String col = (column == null || (col = column.name()).isEmpty()) ? "id_" + this.tableName + "_" + field.getName() : col;
			if (this.remoteFkColumnName.equals(col)) {
				field.setAccessible(true);
				return field;
			}
		}
		return null;
	}

	public Class<?> getForeignClass() {
		return this.foreignClass;
	}

	public String getRemoteFkColumnName() {
		return this.remoteFkColumnName;
	}

	public Field getRemoteFkField() {
		return this.remoteFkField;
	}

	public Class<?> getMyPkType() {
		return this.myPkType;
	}

}