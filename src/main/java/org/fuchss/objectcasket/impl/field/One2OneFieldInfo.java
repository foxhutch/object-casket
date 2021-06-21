package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.sqlconnector.port.SqlPrototype;

public class One2OneFieldInfo extends FieldInfo {
	private ColumnInfo foreignColumnInfo;

	private Class<?> myPkType;

	public One2OneFieldInfo(Field field, Class<?> entityClass, Class<?> myPkType, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, tableName, Kind.ONE2ONE, session);
		this.myPkType = myPkType;
		Field pkField = this.checkField().getPkField();
		if (pkField == null) {
			FieldException.Error.MissingPrimaryKey.build(entityClass.getSimpleName());
		}
		this.ownColumnInfo.setJavaType(pkField.getType());

		this.columnNameAndFlags();
		this.mkForeignColumnInfo();
	}

	private void mkForeignColumnInfo() throws ObjectCasketException {
		Class<?> foreignClass = this.ownColumnInfo.getField().getType();
		String foreignTableName = this.mkForeignTableName(foreignClass);
		String columnName = this.mkForeignColumnName();
		Field columnField = this.mkRemoteFK(foreignClass, columnName);

		this.foreignColumnInfo = ColumnInfo.mkForeignColumnInfo(foreignClass, columnField, columnName, foreignTableName, Kind.ONE2ONE);
	}

	private String mkForeignColumnName() {
		Field field = this.ownColumnInfo.getField();
		JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
		return FieldInfo.fkJoinColumnName(field, this.ownColumnInfo.getTableName(), joinColumn);
		// String remoteFkColumnName;
		// return ((joinColumn == null) || (remoteFkColumnName =
		// joinColumn.name()).isEmpty()) ? "id_R_" + this.columnInfo.getTableName() +
		// "_" + field.getName() : remoteFkColumnName;
	}

	private String mkForeignTableName(Class<?> foreignClass) {
		Table table = foreignClass.getAnnotation(Table.class);
		String foreignTableName;
		return foreignTableName = ((table == null) || (foreignTableName = table.name()).isEmpty()) ? foreignClass.getSimpleName() : foreignTableName;
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
		Column column = this.ownColumnInfo.getField().getAnnotation(Column.class);

		this.addFlag(SqlPrototype.Flag.UNIQUE);
		if ((column != null) && !column.nullable()) {
			this.addFlag(SqlPrototype.Flag.NOT_NULL);
		}
	}

	private Field mkRemoteFK(Class<?> foreignClass, String remoteFkColumnName) throws ObjectCasketException {
		Field remoteFkField = this.remoteFKfield(foreignClass, remoteFkColumnName);
		if (remoteFkField == null) {
			Set<FieldInfo> anonymousFKfieldInfos = this.session.getAnonymousFKfieldInfosMap().get(foreignClass);
			if (anonymousFKfieldInfos == null) {
				this.session.getAnonymousFKfieldInfosMap().put(foreignClass, anonymousFKfieldInfos = new HashSet<>());
			}
			anonymousFKfieldInfos.add(this);
		} else {
			if (!remoteFkField.isAnnotationPresent(OneToOne.class)) {
				FieldException.Error.WrongForeignAnnotation.build(remoteFkField.getName(), foreignClass.getSimpleName(), this.ownColumnInfo.getField().getName(), this.checkField().getPkField().getType().getSimpleName());
			}
		}
		return remoteFkField;
	}

	private Field remoteFKfield(Class<?> foreignClass, String remoteFkColumnName) {
		for (Field field : foreignClass.getDeclaredFields()) {
			String col = FieldInfo.fkColumnName(field, this.ownColumnInfo.getTableName());
			if (remoteFkColumnName.equals(col)) {
				field.setAccessible(true);
				return field;
			}
		}
		return null;
	}

	public Class<?> getForeignClass() {
		return this.foreignColumnInfo.getEntityClass();
	}

	public String getRemoteFkColumnName() {
		return this.foreignColumnInfo.getName();
	}

	public Field getRemoteFkField() {
		return this.foreignColumnInfo.getField();
	}

	public Class<?> getMyPkType() {
		return this.myPkType;
	}

}