package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;

public class One2ManyFieldInfo extends FieldInfo {
	private Class<?> foreignClass;
	private String foreignTableName;

	private String remoteFkColumnName;
	private Field remoteFkField;
	private Class<?> myPkType;

	public One2ManyFieldInfo(Field field, Class<?> entityClass, Class<?> myPkType, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, Kind.ONE2MANY, session);
		this.tableName = tableName;
		this.myPkType = myPkType;
		this.checkFieldAndSetForeignClass();
		this.remoteFK();
	}

	private void checkFieldAndSetForeignClass() throws ObjectCasketException {
		String fieldName = this.field.getName();
		String entityClassName = this.field.getDeclaringClass().getSimpleName();
		if (!Set.class.isAssignableFrom(this.field.getType())) {
			FieldException.Error.WrongOne2ManyField.build(fieldName, entityClassName);
		}
		this.foreignClass = (Class<?>) ((ParameterizedType) (this.field.getGenericType())).getActualTypeArguments()[0];
		if (!this.foreignClass.isAnnotationPresent(Entity.class)) {
			String foreignClassName = this.foreignClass.getSimpleName();
			FieldException.Error.WrongOne2ManyEntity.build(foreignClassName, fieldName, entityClassName);
		}
		this.foreignTableName = (this.foreignTableName = this.foreignClass.getAnnotation(Table.class).name()).isEmpty() ? this.foreignClass.getSimpleName() : this.foreignTableName;
	}

	private void remoteFK() throws ObjectCasketException {
		JoinColumn joinColumn = this.field.getAnnotation(JoinColumn.class);
		this.remoteFkColumnName = FieldInfo.fkColumnName(this.field, this.tableName, joinColumn == null ? null : joinColumn.name());
		this.setRemoteFkField();
		if (this.remoteFkField == null) {
			Set<FieldInfo> anonymousFKfieldInfos = this.session.getAnonymousFKfieldInfosMap().get(this.foreignClass);
			if (anonymousFKfieldInfos == null) {
				this.session.getAnonymousFKfieldInfosMap().put(this.foreignClass, anonymousFKfieldInfos = new HashSet<>());
			}
			anonymousFKfieldInfos.add(this);
		}
	}

	private void setRemoteFkField() {
		List<Field> possibleFields = new ArrayList<>();
		FieldInfo.findPossibleFields(this.foreignClass, possibleFields, ManyToOne.class);

		for (Field possibleField : possibleFields) {
			if (this.checkAndSetFkField(possibleField)) {
				break;
			}
		}
	}

	private boolean checkAndSetFkField(Field possibleField) {
		if (!this.getEntityClass().equals(possibleField.getType())) {
			return false;
		}
		Column column = possibleField.getAnnotation(Column.class);
		String col = FieldInfo.fkColumnName(this.field, this.tableName, column == null ? null : column.name());
		if (!this.remoteFkColumnName.equals(col)) {
			return false;
		}
		this.remoteFkField = possibleField;
		this.remoteFkField.setAccessible(true);
		return true;
	}

	public String getRemoteFkColumnName() {
		return this.remoteFkColumnName;
	}

	public Class<?> getMyPkType() {
		return this.myPkType;
	}

	public Field getRemoteFkField() {
		return this.remoteFkField;
	}

	public Class<?> getForeignClass() {
		return this.foreignClass;
	}

}
