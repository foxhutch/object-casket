package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;

public class One2ManyFieldInfo extends FieldInfo {

	private ColumnInfo foreignColumnInfo;

	private Class<?> myPkType;

	public One2ManyFieldInfo(Field field, Class<?> entityClass, Class<?> myPkType, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, tableName, Kind.ONE2MANY, session);
		this.myPkType = myPkType;
		this.checkField();

		this.mkForeignColumnInfo();
	}

	private void checkField() throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();
		if (!Set.class.isAssignableFrom(field.getType())) {
			FieldException.Error.WrongOne2ManyField.build(field.getName(), field.getDeclaringClass().getSimpleName());
		}
	}

	private void mkForeignColumnInfo() throws ObjectCasketException {

		Class<?> foreignClass = this.findForeignClass();
		String remoteFkColumnName = this.mkRemoteFkColumnName();
		Field remoteFkField = this.findRemoteFkField(foreignClass, remoteFkColumnName);

		String foreignTableName = foreignClass.getAnnotation(Table.class).name();
		if (foreignTableName.isBlank())
			foreignTableName = foreignClass.getSimpleName();

		this.foreignColumnInfo = ColumnInfo.mkForeignColumnInfo(foreignClass, remoteFkField, remoteFkColumnName, foreignTableName, Kind.MANY2ONE);
	}

	private Class<?> findForeignClass() throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();
		Class<?> foreignClass = (Class<?>) ((ParameterizedType) (field.getGenericType())).getActualTypeArguments()[0];
		if (!foreignClass.isAnnotationPresent(Entity.class)) {
			FieldException.Error.WrongOne2ManyEntity.build(foreignClass.getSimpleName(), field.getName(), field.getDeclaringClass().getSimpleName());
		}
		if (this.session.getEntityFactory(foreignClass) == null) {
			FieldException.Error.MissingEntityFactory.build(field.getName(), field.getDeclaringClass().getSimpleName(), foreignClass.getSimpleName());
		}
		return foreignClass;
	}

	private String mkRemoteFkColumnName() {
		Field field = this.ownColumnInfo.getField();
		JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
		return FieldInfo.fkJoinColumnName(field, this.ownColumnInfo.getTableName(), joinColumn);
	}

	private Field findRemoteFkField(Class<?> foreignClass, String remoteFkColumnName) {
		Field remoteFkField = null;
		List<Field> possibleFields = new ArrayList<>();
		FieldInfo.findPossibleFields(foreignClass, possibleFields, ManyToOne.class);
		for (Field possibleField : possibleFields) {
			if (this.isFkField(possibleField, remoteFkColumnName)) {
				remoteFkField = possibleField;
				remoteFkField.setAccessible(true);
				break;
			}
		}
		if (remoteFkField == null) {
			Set<FieldInfo> anonymousFKfieldInfos = this.session.getAnonymousFKfieldInfosMap().get(foreignClass);
			if (anonymousFKfieldInfos == null) {
				this.session.getAnonymousFKfieldInfosMap().put(foreignClass, anonymousFKfieldInfos = new HashSet<>());
			}
			anonymousFKfieldInfos.add(this);
		}
		return remoteFkField;
	}

	private boolean isFkField(Field possibleField, String remoteFkColumnName) {
		if (!this.getEntityClass().equals(possibleField.getType())) {
			return false;
		}
		String col = FieldInfo.fkColumnName(possibleField, this.ownColumnInfo.getTableName());
		return remoteFkColumnName.equals(col);
	}

	public String getRemoteFkColumnName() {
		return this.foreignColumnInfo.getName();
	}

	public Class<?> getMyPkType() {
		return this.myPkType;
	}

	public Field getRemoteFkField() {
		return this.foreignColumnInfo.getField();
	}

	public Class<?> getForeignClass() {
		return this.foreignColumnInfo.getEntityClass();
	}

}
