package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class Many2OneFieldInfo extends FieldInfo {
	private Class<?> foreignClass;
	private String foreignTableName;

	private Field foreignField;
	private FieldInfo.Kind foreignFieldKind;

	public Many2OneFieldInfo(Field field, Class<?> entityClass, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, Kind.MANY2ONE, session);
		this.tableName = tableName;
		this.checkField();
		this.foreignClassAndTable();
		this.columnNameAndFlags();
		this.findForeignField();
		this.columnType = this.session.getEntityFactory(this.foreignClass).getPkField().getType();
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
		this.foreignTableName = (this.foreignTableName = this.foreignClass.getAnnotation(Table.class).name()).isEmpty() ? this.foreignClass.getSimpleName() : this.foreignTableName;
	}

	private void columnNameAndFlags() {
		Column column = this.field.getAnnotation(Column.class);
		this.columnName = FieldInfo.fkColumnName(this.field, this.foreignTableName, column == null ? null : column.name());
		if ((column != null) && !column.nullable()) {
			this.addFlag(SqlPrototype.Flag.NOT_NULL);
		}
	}

	private void findForeignField() throws ObjectCasketException {
		List<Field> possibleM2MFields = new ArrayList<>();
		FieldInfo.findPossibleFields(this.foreignClass, possibleM2MFields, ManyToMany.class);
		for (Field possibleField : possibleM2MFields) {
			if (this.checkAndSetForeignM2MField(possibleField)) {
				break;
			}
		}
		if (this.foreignField != null) {
			return;
		}
		List<Field> possibleO2MFields = new ArrayList<>();
		FieldInfo.findPossibleFields(this.foreignClass, possibleO2MFields, OneToMany.class);
		for (Field possibleField : possibleO2MFields) {
			if (this.checkAndSetForeignO2MField(possibleField)) {
				break;
			}
		}
	}

	private boolean checkAndSetForeignM2MField(Field possibleField) {
		JoinTable table = possibleField.getAnnotation(JoinTable.class);
		if (table == null) {
			return false;
		}
		JoinColumn[] ownJoinColumns = table.inverseJoinColumns();
		String ownFKColumnName = FieldInfo.fkColumnName(possibleField, this.foreignTableName, (ownJoinColumns.length == 1) ? ownJoinColumns[0].name() : null);
		if (this.columnName.equals(ownFKColumnName)) {
			possibleField.setAccessible(true);
			this.foreignField = possibleField;
			this.kind = FieldInfo.Kind.MANY2MANY;
			return true;
		}
		return false;
	}

	private boolean checkAndSetForeignO2MField(Field possibleField) {
		JoinColumn joinColumn = possibleField.getAnnotation(JoinColumn.class);
		String remoteFkColumnName = FieldInfo.fkColumnName(possibleField, this.foreignTableName, joinColumn == null ? null : joinColumn.name());
		if (this.columnName.equals(remoteFkColumnName)) {
			possibleField.setAccessible(true);
			this.foreignField = possibleField;
			this.foreignFieldKind = FieldInfo.Kind.ONE2MANY;
			return true;
		}
		return false;
	}

	public Class<?> getForeignClass() {
		return this.foreignClass;
	}

	public FieldInfo.Kind getForeignFieldKind() {
		return this.foreignFieldKind;
	}

	public Field getForeignField() {
		return this.foreignField;
	}

}
