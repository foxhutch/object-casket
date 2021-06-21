package org.fuchss.objectcasket.impl.field;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;

public class Many2ManyFieldInfo extends FieldInfo {

	private JoinTable joinTable;
	private Class<?> joinTableClass;
	private String joinTableName;

	private Class<?> remoteClass;
	private String remoteTableName;
	private Field remoteField;

	private String remoteFKColumnName;
	private Field remoteFKfield;
	private Class<?> remotePkType;

	private String ownFKColumnName;
	private Field ownFKfield;
	private Class<?> ownPkType;

	private boolean anonymousOwnFK;
	private boolean anonymousRemoteFK;

	public Many2ManyFieldInfo(Field field, Class<?> entityClass, Class<?> ownPkType, String tableName, SessionImpl session) throws ObjectCasketException {
		super(field, entityClass, tableName, Kind.MANY2MANY, session);
		this.ownPkType = ownPkType;
		this.checkField();

		this.mkJoinTable();
		this.initializeFKs();
		this.updateFKbyRemoteField();
		this.finalzeFK();
	}

	private void checkField() throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();
		String fieldName = field.getName();
		String entityClassName = this.ownColumnInfo.getEntityClass().getSimpleName();
		if (!Set.class.isAssignableFrom(field.getType()))
			FieldException.Error.WrongMany2ManyField.build(fieldName, entityClassName);
	}

	private void mkJoinTable() throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();
		this.joinTable = field.getAnnotation(JoinTable.class);
		if (this.joinTable == null) {
			FieldException.Error.MissingJoinTable.build(field.getName(), this.ownColumnInfo.getEntityClass().getSimpleName());
		}
		this.joinTableName = this.joinTable.name();
		this.joinTableClass = this.session.getTableName2ClassMap().get(this.joinTableName);
		if (this.joinTableClass == null) {
			FieldException.Error.MissingRegisteredJoinTable.build(this.joinTableName, this.ownColumnInfo.getField().getName(), this.ownColumnInfo.getEntityClass().getSimpleName());
		}
	}

	private void initializeFKs() throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();
		JoinColumn[] remoteJoinColumns = this.joinTable.joinColumns();
		JoinColumn[] ownJoinColumns = this.joinTable.inverseJoinColumns();
		this.ownFKColumnName = FieldInfo.fkJoinColumnName(field, this.ownColumnInfo.getTableName(), (ownJoinColumns.length == 1) ? ownJoinColumns[0] : null);
		this.remoteFKColumnName = (remoteJoinColumns.length == 1) ? remoteJoinColumns[0].name() : null;

		this.remoteClass = this.findRemoteClass();
		this.remoteTableName = this.remoteClass.getAnnotation(Table.class).name();
		if (this.remoteTableName.isBlank())
			this.remoteTableName = this.remoteClass.getSimpleName();
		this.remotePkType = this.findPkType(this.remoteClass);
	}

	private Class<?> findRemoteClass() throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();
		String entityClassName = this.ownColumnInfo.getEntityClass().getSimpleName();
		Class<?> remoteClass = (Class<?>) ((ParameterizedType) (field.getGenericType())).getActualTypeArguments()[0];
		if (!remoteClass.isAnnotationPresent(Entity.class))
			FieldException.Error.WrongMany2ManyEntity.build(this.remoteClass.getSimpleName(), field.getName(), entityClassName);
		return remoteClass;
	}

	private Class<?> findPkType(Class<?> clazz) throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();
		String entityClassName = this.ownColumnInfo.getEntityClass().getSimpleName();

		EntityFactory ef = this.session.getEntityFactory(clazz);
		if (ef == null)
			FieldException.Error.MissingEntityFactory.build(field.getName(), entityClassName, clazz.getSimpleName());
		Field pkField = ef.getPkField();
		if (pkField == null)
			FieldException.Error.MissingPrimaryKey.build(clazz.getSimpleName());
		return pkField.getType();

	}

	private void updateFKbyRemoteField() throws ObjectCasketException {
		List<Field> possibleFields = new ArrayList<>();
		FieldInfo.findPossibleFields(this.remoteClass, possibleFields, ManyToMany.class);
		for (Field possibleField : possibleFields) {
			if (this.checkAndSetRemoteField(possibleField)) {
				this.remoteField = possibleField;
				this.remoteField.setAccessible(true);
				break;
			}
		}
		if (this.remoteFKColumnName == null) {
			JoinColumn[] remoteJoinColumns = this.joinTable.joinColumns();
			this.remoteFKColumnName = FieldInfo.fkJoinColumnName(this.ownColumnInfo.getField(), this.remoteTableName, (remoteJoinColumns.length == 1) ? remoteJoinColumns[0] : null);

		}
	}

	private boolean checkAndSetRemoteField(Field possibleField) throws ObjectCasketException {
		Field field = this.ownColumnInfo.getField();

		if (field.equals(possibleField)) {
			return true;
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
		String ownFkColumnName = FieldInfo.fkJoinColumnName(possibleField, this.remoteTableName, (ownJoinColumns.length == 1) ? ownJoinColumns[0] : null);
		String remoteFKColumnName = (ownJoinColumns.length == 1) ? remoteJoinColumns[0].name() : null;

		if ((remoteFKColumnName != null) && !this.ownFKColumnName.equals(remoteFKColumnName)) {
			FieldException.Error.IncompatibleMany2ManyEntity.build(field.getName(), this.ownColumnInfo.getEntityClass().getSimpleName(), possibleField.getName(), possibleField.getDeclaringClass().getSimpleName());
		}
		if ((this.remoteFKColumnName != null) && !ownFkColumnName.equals(this.remoteFKColumnName)) {
			FieldException.Error.IncompatibleMany2ManyEntity.build(field.getName(), this.ownColumnInfo.getEntityClass().getSimpleName(), possibleField.getName(), possibleField.getDeclaringClass().getSimpleName());
		}
		this.remoteFKColumnName = ownFkColumnName;
		return true;

	}

	private void finalzeFK() throws ObjectCasketException {

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
		FieldInfo.findPossibleFields(this.joinTableClass, possibleFields, ManyToOne.class); //
		for (Field field : possibleFields) {
			if (this.isOwnFKfield(field)) {
				field.setAccessible(true);
				this.ownFKfield = field;
				break;
			}
		}
		for (Field field : possibleFields) {
			if (this.isRemoteFKfield(field)) {
				field.setAccessible(true);
				this.remoteFKfield = field;
				break;
			}
		}
	}

	private boolean isRemoteFKfield(Field field) {
		String col = FieldInfo.fkColumnName(field, this.remoteTableName);
		return this.remoteFKColumnName.equals(col);
	}

	private boolean isOwnFKfield(Field field) {
		String col = FieldInfo.fkColumnName(field, this.ownColumnInfo.getTableName());
		return this.ownFKColumnName.equals(col);
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
			return this.ownColumnInfo.getEntityClass();
		}
		return this.remoteClass;
	}

}
