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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;

public class Many2ManyFieldInfo extends FieldInfo {
	private Class<?> remoteClass;
	private String remoteTableName;
	private Field remoteField;

	private Class<?> joinTableClass;
	private String joinTableName;

	private String remoteFKColumnName;
	private Field remoteFKfield;
	private Class<?> remotePkType;

	private String ownFKColumnName;
	private Field ownFKfield;
	private Class<?> ownPkType;

	private boolean anonymousOwnFK;
	private boolean anonymousRemoteFK;

	public Many2ManyFieldInfo(Field field, Class<?> entityClass, Class<?> ownPkType, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, Kind.MANY2MANY, session);
		this.tableName = tableName;
		this.ownPkType = ownPkType;
		JoinTable tab = this.setJoinTableNameAndOwnForeignKey();
		this.checkFieldAndSetRemoteClass();
		this.remotePkType = this.session.getEntityFactory(this.remoteClass).getPkField().getType();
		this.findRemoteFieldAndSetRemoteForeignKey();
		this.mkJoinTable(tab);

	}

	private JoinTable setJoinTableNameAndOwnForeignKey() throws ObjectCasketException {
		JoinTable table = this.field.getAnnotation(JoinTable.class);
		if (table == null) {
			FieldException.Error.MissingJoinTable.build(this.field.getName(), this.field.getDeclaringClass().getSimpleName());
		}
		this.joinTableName = table.name();

		JoinColumn[] remoteJoinColumns = table.joinColumns();
		JoinColumn[] ownJoinColumns = table.inverseJoinColumns();
		this.ownFKColumnName = FieldInfo.fkColumnName(this.field, this.tableName, (ownJoinColumns.length == 1) ? ownJoinColumns[0].name() : null);
		this.remoteFKColumnName = (remoteJoinColumns.length == 1) ? remoteJoinColumns[0].name() : null;
		return table;
	}

	private void checkFieldAndSetRemoteClass() throws ObjectCasketException {
		String fieldName = this.field.getName();
		String entityClassName = this.field.getDeclaringClass().getSimpleName();
		if (!Set.class.isAssignableFrom(this.field.getType())) {
			FieldException.Error.WrongMany2ManyField.build(fieldName, entityClassName);
		}
		this.remoteClass = (Class<?>) ((ParameterizedType) (this.field.getGenericType())).getActualTypeArguments()[0];
		if (!this.remoteClass.isAnnotationPresent(Entity.class)) {
			String remoteClassName = this.remoteClass.getSimpleName();
			FieldException.Error.WrongMany2ManyEntity.build(remoteClassName, fieldName, entityClassName);
		}
		this.remoteTableName = (this.remoteTableName = this.remoteClass.getAnnotation(Table.class).name()).isEmpty() ? this.remoteClass.getSimpleName() : this.remoteTableName;
	}

	private void findRemoteFieldAndSetRemoteForeignKey() throws ObjectCasketException {
		List<Field> possibleFields = new ArrayList<>();
		FieldInfo.findPossibleFields(this.remoteClass, possibleFields, ManyToMany.class);
		for (Field possibleField : possibleFields) {
			if (this.checkAndSetRemoteField(possibleField)) {
				break;
			}
		}
	}

	private boolean checkAndSetRemoteField(Field possibleField) throws ObjectCasketException {
		if (this.field.equals(possibleField)) {
			return (this.remoteField = possibleField) != null;
		}
		if (!Set.class.isAssignableFrom(possibleField.getType())) {
			return false;
		}
		JoinTable table = possibleField.getAnnotation(JoinTable.class);
		if ((table == null) || !this.joinTableName.equals(table.name())) {
			return false;
		}

		JoinColumn[] remoteJoinColumns = table.joinColumns();
		JoinColumn[] ownJoinColumns = table.inverseJoinColumns();
		String ownFkColumnName = FieldInfo.fkColumnName(possibleField, this.remoteTableName, (ownJoinColumns.length == 1) ? ownJoinColumns[0].name() : null);
		String remoteFKColumnName = (ownJoinColumns.length == 1) ? remoteJoinColumns[0].name() : null;

		if ((remoteFKColumnName != null) && !this.ownFKColumnName.equals(remoteFKColumnName)) {
			FieldException.Error.IncompatibleMany2ManyEntity.build(this.field.getName(), this.field.getDeclaringClass().getSimpleName(), possibleField.getName(), possibleField.getDeclaringClass().getSimpleName());
		}
		if ((this.remoteFKColumnName != null) && !ownFkColumnName.equals(this.remoteFKColumnName)) {
			FieldException.Error.IncompatibleMany2ManyEntity.build(this.field.getName(), this.field.getDeclaringClass().getSimpleName(), possibleField.getName(), possibleField.getDeclaringClass().getSimpleName());
		}
		this.remoteFKColumnName = ownFkColumnName;

		this.remoteField = possibleField;
		this.remoteField.setAccessible(true);
		return true;

	}

	private void mkJoinTable(JoinTable table) throws ObjectCasketException {
		this.joinTableClass = this.session.getTableName2ClassMap().get(this.joinTableName);
		if (this.joinTableClass == null) {
			FieldException.Error.MissingRegisteredJoinTable.build(this.joinTableName, this.field.getName(), this.field.getDeclaringClass().getSimpleName());
		}

		this.setJoinTableFKfields();
		if ((this.anonymousRemoteFK = (this.remoteFKfield == null)) | (this.anonymousOwnFK = (this.ownFKfield == null))) {
			Set<FieldInfo> anonymousFKfieldInfos = this.session.getAnonymousFKfieldInfosMap().get(this.joinTableClass);
			if (anonymousFKfieldInfos == null) {
				this.session.getAnonymousFKfieldInfosMap().put(this.joinTableClass, anonymousFKfieldInfos = new HashSet<>());
			}
			anonymousFKfieldInfos.add(this);
		}
	}

	private void setJoinTableFKfields() {
		List<Field> possibleFields = new ArrayList<>();
		FieldInfo.findPossibleFields(this.joinTableClass, possibleFields, OneToMany.class);
		for (Field field : possibleFields) {
			if (this.setOwnFKfield(field)) {
				break;
			}
		}
		for (Field field : possibleFields) {
			if (this.setRemoteFKfield(field)) {
				break;
			}
		}
	}

	private boolean setRemoteFKfield(Field field) {
		Column column = field.getAnnotation(Column.class);
		String col = FieldInfo.fkColumnName(this.field, this.remoteTableName, (column != null) ? column.name() : null);
		if (this.remoteFKColumnName.equals(col)) {
			field.setAccessible(true);
			this.remoteFKfield = field;
			return true;
		}
		return false;
	}

	private boolean setOwnFKfield(Field field) {
		Column column = field.getAnnotation(Column.class);
		String col = FieldInfo.fkColumnName(this.field, this.tableName, (column != null) ? column.name() : null);
		if (this.ownFKColumnName.equals(col)) {
			field.setAccessible(true);
			this.ownFKfield = field;
			return true;
		}
		return false;
	}

	public boolean isAnonymousOwnFK() {
		return this.anonymousOwnFK;
	}

	public boolean isAnonymousRemoteFK() {
		return this.anonymousRemoteFK;
	}

	public String getRemoteFKColumnName() {
		return this.remoteFKColumnName;
	}

	public String getOwnFKColumnName() {
		return this.ownFKColumnName;
	}

	public Field getRemoteField() {
		return this.remoteField;
	}

	public Class<?> getJoinTableClass() {
		return this.joinTableClass;
	}

	public Field getRemoteFKfield() {
		return this.remoteFKfield;
	}

	public Field getOwnFKfield() {
		return this.ownFKfield;
	}

	public Class<?> getPkType(String columnName) {
		if (this.ownFKColumnName.equals(columnName)) {
			return this.ownPkType;
		}
		if (this.remoteFKColumnName.equals(columnName)) {
			return this.getRemotePkType();
		}
		return null;
	}

	public Class<?> getMyPkType() {
		return this.ownPkType;
	}

	public Class<?> getRemotePkType() {
		return this.remotePkType;
	}

	public Class<?> getRemoteClass() {
		return this.remoteClass;
	}

	public String getJoinTableName() {
		return this.joinTableName;
	}

	public Class<?> getEntityClass(String fkColumnName) {
		if (this.ownFKColumnName.equals(fkColumnName)) {
			return this.entityClass;
		}
		return this.remoteClass;
	}

}
