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

import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class Many2OneFieldInfo extends FieldInfo {

	private ColumnInfo foreignColumnInfo;

	public Many2OneFieldInfo(Field field, Class<?> entityClass, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, tableName, Kind.MANY2ONE, session);

		Field pkField = this.checkField().getPkField();
		if (pkField == null) {
			FieldException.Error.MissingPrimaryKey.build(entityClass.getSimpleName());
		}
		this.ownColumnInfo.setJavaType(pkField.getType());

		this.columnNameAndFlags();
		this.mkForeignColumnInfo();
	}

	private void mkForeignColumnInfo() throws ObjectCasketException {

		Class<?> foreignClass = this.findForeignClass();

		String foreignTableName = foreignClass.getAnnotation(Table.class).name();
		if (foreignTableName.isBlank())
			foreignTableName = foreignClass.getSimpleName();

		Field foreignM2MField = this.findForeignM2MField(foreignClass, foreignTableName);
		Field foreignO2MField = this.findForeignO2MField(foreignClass, foreignTableName);

		if (foreignM2MField != null) {
			this.foreignColumnInfo = ColumnInfo.mkForeignColumnInfo(foreignClass, foreignM2MField, null, foreignTableName, Kind.MANY2MANY);
		}
		if (foreignO2MField != null) {
			JoinColumn joinColumn = foreignO2MField.getAnnotation(JoinColumn.class);
			String remoteFkColumnName = FieldInfo.fkJoinColumnName(foreignO2MField, foreignTableName, joinColumn);

			this.foreignColumnInfo = ColumnInfo.mkForeignColumnInfo(foreignClass, foreignO2MField, remoteFkColumnName, foreignTableName, Kind.ONE2MANY);
		}
		if ((foreignM2MField == null) && (foreignO2MField == null))
			this.foreignColumnInfo = ColumnInfo.mkForeignColumnInfo(foreignClass, null, null, foreignTableName, Kind.ONE2MANY);

		if ((foreignM2MField != null) && (foreignO2MField != null))
			FieldException.Error.WrongMany2OneField.build(this.ownColumnInfo.getField().getName(), this.ownColumnInfo.getEntityClass().getSimpleName(), foreignM2MField.getName(), foreignO2MField.getName());
	}

	private Class<?> findForeignClass() throws ObjectCasketException {

		Field field = this.ownColumnInfo.getField();
		Class<?> foreignClass = field.getType();

		EntityFactory ef = this.session.getEntityFactory(foreignClass);
		if (ef == null)
			FieldException.Error.MissingEntityFactory.build(field.getName(), this.ownColumnInfo.getEntityClass().getSimpleName(), foreignClass.getSimpleName());
		return foreignClass;

	}

	private EntityFactory checkField() throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();
		String foreignClassName = field.getType().getSimpleName();
		String fieldName = field.getName();
		String entityClassName = field.getDeclaringClass().getSimpleName();
		if (!field.getType().isAnnotationPresent(Entity.class)) {
			FieldException.Error.WrongEntity.build(foreignClassName, fieldName, entityClassName);
		}
		EntityFactory ef = this.session.getEntityFactory(field.getType());
		if (ef == null)
			FieldException.Error.MissingEntityFactory.build(fieldName, entityClassName, foreignClassName);
		return ef;

	}

	private void columnNameAndFlags() {
		Field field = this.ownColumnInfo.getField();
		Column column = field.getAnnotation(Column.class);
		if ((column != null) && !column.nullable()) {
			this.addFlag(SqlPrototype.Flag.NOT_NULL);
		}
	}

	private Field findForeignM2MField(Class<?> foreignClass, String foreignTableName) throws ObjectCasketException {
		Field foreignField = null;
		List<Field> possibleM2MFields = new ArrayList<>();
		FieldInfo.findPossibleFields(foreignClass, possibleM2MFields, ManyToMany.class);
		for (Field possibleField : possibleM2MFields) {
			if (this.isForeignM2MField(possibleField, foreignTableName)) {
				foreignField = possibleField;
				foreignField.setAccessible(true);
				break;
			}
		}
		return foreignField;
	}

	private Field findForeignO2MField(Class<?> foreignClass, String foreignTableName) throws ObjectCasketException {
		Field foreignField = null;
		List<Field> possibleO2MFields = new ArrayList<>();
		FieldInfo.findPossibleFields(foreignClass, possibleO2MFields, OneToMany.class);
		for (Field possibleField : possibleO2MFields) {
			if (this.isForeignO2MField(possibleField, foreignTableName)) {
				foreignField = possibleField;
				foreignField.setAccessible(true);
				break;
			}
		}
		return foreignField;
	}

	private boolean isForeignM2MField(Field possibleField, String foreignTableName) {
		JoinTable table = possibleField.getAnnotation(JoinTable.class);
		if (table == null) {
			return false;
		}
		JoinColumn[] ownJoinColumns = table.inverseJoinColumns();
		String ownFKColumnName = FieldInfo.fkJoinColumnName(possibleField, foreignTableName, (ownJoinColumns.length == 1) ? ownJoinColumns[0] : null);
		return this.ownColumnInfo.getName().equals(ownFKColumnName);
	}

	private boolean isForeignO2MField(Field possibleField, String foreignTableName) {
		JoinColumn joinColumn = possibleField.getAnnotation(JoinColumn.class);
		String remoteFkColumnName = FieldInfo.fkJoinColumnName(possibleField, foreignTableName, joinColumn);
		return this.ownColumnInfo.getName().equals(remoteFkColumnName);
	}

	public Class<?> getForeignClass() {
		return this.foreignColumnInfo.getEntityClass();
	}

	public FieldInfo.Kind getForeignFieldKind() {
		return this.foreignColumnInfo.getKind();
	}

	public Field getForeignField() {
		return this.foreignColumnInfo.getField();
	}

}
