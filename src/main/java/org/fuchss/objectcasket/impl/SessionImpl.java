package org.fuchss.objectcasket.impl;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Table;

import org.fuchss.objectcasket.impl.field.FieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketCMP;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.fuchss.tablemodule.port.TableModule;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.Transaction;

public class SessionImpl implements Session {
	private TableModule module;
	private Map<String, Class<?>> tableName2ClassMap = new HashMap<>();
	private Map<Class<?>, String> class2tableNameMap = new HashMap<>();
	private Map<Class<?>, EntityFactory> entityFactoryMap = new HashMap<>();
	private Map<Class<?>, Set<FieldInfo>> anonymousFKfieldInfosMap = new HashMap<>();

	private Set<Object> moreObjectsToDelete = new HashSet<>();

	private Boolean isOpen = null;

	public SessionImpl(TableModule module) {
		this.module = module;
	}

	@Override
	public void declareClass(Class<?>... clazz) throws ObjectCasketException {
		for (Class<?> theClazz : clazz) {
			this.forwardDeclareClass(theClazz);
		}

		for (Class<?> theClazz : clazz) {
			this.declareClass(theClazz);
		}

		for (Class<?> theClazz : clazz) {
			this.entityFactoryMap.get(theClazz).initFields();
		}

	}

	private void declareClass(Class<?> clazz) throws ObjectCasketException {
		if (this.entityFactoryMap.containsKey(clazz)) {
			return;
		}
		String tableName = this.class2tableNameMap.get(clazz);
		new EntityFactory(tableName, clazz, this);
	}

	public void register(Class<?> clazz, EntityFactory ef) {
		this.entityFactoryMap.put(clazz, ef);

	}

	private void forwardDeclareClass(Class<?> clazz) throws ObjectCasketException {
		if (this.class2tableNameMap.containsKey(clazz)) {
			return;
		}
		String tableName = this.checkClassAndGetTableName(clazz);
		this.tableName2ClassMap.put(tableName, clazz);
		this.class2tableNameMap.put(clazz, tableName);
	}

	@Override
	public void open() throws ObjectCasketException {
		if ((this.isOpen != null) && !this.isOpen) {
			SessionException.Error.NoSession2Open.build();
		}
		if ((this.isOpen != null) && this.isOpen) {
			return;
		}
		this.checkEntityFactorys();
		for (EntityFactory ef : this.entityFactoryMap.values()) {
			ef.initTablePrototype();
		}
		for (EntityFactory ef : this.entityFactoryMap.values()) {
			ef.completeTablePrototype();
		}
		for (EntityFactory ef : this.entityFactoryMap.values()) {
			ef.createOrAssignTable();
		}
		this.isOpen = true;
	}

	private void checkEntityFactorys() throws ObjectCasketException {
		for (Class<?> clazz : this.anonymousFKfieldInfosMap.keySet()) {
			EntityFactory ef = this.entityFactoryMap.get(clazz);
			if (ef == null) {
				SessionException.Error.NoEntityFactoryForClass.build(clazz.getSimpleName());
			}
			ef.addAnonymousFKfields(this.anonymousFKfieldInfosMap.get(clazz));

		}
	}

	public void terminate() throws ObjectCasketException {
		if (this.isOpen == null) {
			SessionException.Error.NoSession2Terminate.build();
		}
		for (EntityFactory ef : this.entityFactoryMap.values()) {
			ef.removePrototype();
		}
		this.module = null;
		this.tableName2ClassMap = null;
		this.class2tableNameMap = null;
		this.entityFactoryMap = null;
		this.anonymousFKfieldInfosMap = null;

		this.isOpen = false;
	}

	@Override
	public <T> Set<T> getAllObjects(Class<T> clazz) throws ObjectCasketException {
		this.checkSession();
		if (!this.entityFactoryMap.containsKey(clazz)) {
			SessionException.Error.NoEntityFactoryForClass.build(clazz == null ? "Null" : clazz.getSimpleName());
		}
		Set<T> objects = null;
		Transaction transaction = this.module.beginTransaction();
		try {
			objects = this.getAllObjects(clazz, transaction);
			this.module.commit(transaction);
		} catch (ObjectCasketException | TableModuleException exc) {
			try {
				this.module.rollback(transaction);
			} catch (TableModuleException e) {
				ObjectCasketException.build(e);
			}
			ObjectCasketException.build(exc);
		}
		return objects;
	}

	@SuppressWarnings("unchecked")
	private <T> Set<T> getAllObjects(Class<T> clazz, Transaction transaction) throws ObjectCasketException {
		Set<T> objects = new HashSet<>();
		EntityFactory ef = this.entityFactoryMap.get(clazz);
		Set<Object> entities = ef.getAllObjs(transaction);
		objects.addAll((Set<T>) entities);
		return objects;
	}

	@Override
	public <T> Set<T> getObjectsByPrototype(T prototype, Map<String, ObjectCasketCMP> cmps) throws ObjectCasketException {
		this.checkSession();
		if ((prototype == null) || !this.entityFactoryMap.containsKey(prototype.getClass())) {
			SessionException.Error.NoEntityFactoryForClass.build(prototype == null ? "Null" : prototype.getClass().getSimpleName());
		}
		Set<T> objects = null;
		Transaction transaction = this.module.beginTransaction();
		try {
			objects = this.getObjectsByPrototype(prototype, cmps, transaction);
			this.module.commit(transaction);
		} catch (ObjectCasketException | TableModuleException exc) {
			try {
				this.module.rollback(transaction);
			} catch (TableModuleException e) {
				ObjectCasketException.build(e);
			}
			ObjectCasketException.build(exc);
		}
		return objects;
	}

	@SuppressWarnings("unchecked")
	private <T> Set<T> getObjectsByPrototype(T prototype, Map<String, ObjectCasketCMP> cmps, Transaction transaction) throws ObjectCasketException {
		Set<T> objects = new HashSet<>();
		EntityFactory ef = this.entityFactoryMap.get(prototype.getClass());
		Set<Object> entities = ef.getObjsByPrototype(prototype, cmps, transaction);
		objects.addAll((Set<T>) entities);
		return objects;
	}

	/*
	 * @Override public Object joinTableEntity(Object me, Object remote, String
	 * joinTableName) throws ObjectCasketException { this.checkSession(); if ((me ==
	 * null) || !this.entityFactoryMap.containsKey(me.getClass())) {
	 * SessionException.Error.NoEntityFactoryForClass.build(me == null ? "Null" :
	 * me.getClass().getSimpleName()); } if ((joinTableName == null) ||
	 * !this.tableName2ClassMap.containsKey(joinTableName)) {
	 * SessionException.Error.NoEntityFactoryForClass.build(joinTableName == null ?
	 * "Null" : joinTableName); } Object joinTableEntity = null; Transaction
	 * transaction = this.module.beginTransaction(); try { joinTableEntity =
	 * this.joinTableEntity(me, remote, joinTableName, transaction); if
	 * (joinTableEntity == null) joinTableEntity = this.joinTableEntity(remote, me,
	 * joinTableName, transaction); this.module.commit(transaction); } catch
	 * (ObjectCasketException | TableModuleException exc) { try {
	 * this.module.rollback(transaction); } catch (TableModuleException e) {
	 * ObjectCasketException.build(e); } ObjectCasketException.build(exc); } return
	 * joinTableEntity; }
	 */

	@Override
	public <T> T joinTableEntity(Object me, Object remote, Class<T> joinTableClass) throws ObjectCasketException {
		this.checkSession();
		if ((me == null) || !this.entityFactoryMap.containsKey(me.getClass())) {
			SessionException.Error.NoEntityFactoryForClass.build(me == null ? "Null" : me.getClass().getSimpleName());
		}
		if ((remote == null) || !this.entityFactoryMap.containsKey(remote.getClass())) {
			SessionException.Error.NoEntityFactoryForClass.build(remote == null ? "Null" : remote.getClass().getSimpleName());
		}
		if ((joinTableClass == null) || !this.tableName2ClassMap.containsValue(joinTableClass)) {
			SessionException.Error.NoEntityFactoryForClass.build(joinTableClass == null ? "Null" : joinTableClass.getSimpleName());
		}
		T joinTableEntity = null;
		Transaction transaction = this.module.beginTransaction();
		try {
			joinTableEntity = this.joinTableEntity(me, remote, joinTableClass, transaction);
			if (joinTableEntity == null)
				joinTableEntity = this.joinTableEntity(remote, me, joinTableClass, transaction);
			this.module.commit(transaction);
		} catch (ObjectCasketException | TableModuleException exc) {
			try {
				this.module.rollback(transaction);
			} catch (TableModuleException e) {
				ObjectCasketException.build(e);
			}
			ObjectCasketException.build(exc);
		}
		return joinTableEntity;
	}

	@SuppressWarnings("unchecked")
	private <T> T joinTableEntity(Object me, Object remote, Class<T> joinTableClass, Transaction transaction) throws ObjectCasketException {
		EntityFactory ef = this.entityFactoryMap.get(me.getClass());
		Entity entity = ef.entity(me, transaction);
		return (T) entity.getJoinTableEntity(remote, this.class2tableNameMap.get(joinTableClass));
	}

	@Override
	public void persist(Object obj) throws ObjectCasketException {
		this.checkSession();
		Transaction transaction = this.module.beginTransaction();
		try {
			this.persist(obj, transaction);
			while (!this.moreObjectsToDelete.isEmpty()) {
				Set<Object> objectsToDelete = this.moreObjectsToDelete;
				this.moreObjectsToDelete = new HashSet<>();
				this.deleteObjects(objectsToDelete, transaction);
			}
			this.module.commit(transaction);
		} catch (ObjectCasketException | TableModuleException exc) {
			try {
				this.module.rollback(transaction);
			} catch (TableModuleException e) {
				ObjectCasketException.build(e);
			}
			ObjectCasketException.build(exc);
		}
	}

	private void persist(Object obj, Transaction transaction) throws ObjectCasketException {
		EntityFactory ef = this.entityFactoryMap.get(obj.getClass());
		Entity entity = ef.entity(obj, transaction);
		entity.persist(transaction);
		return;
	}

	@Override
	public void delete(Object obj) throws ObjectCasketException {
		this.checkSession();
		Transaction transaction = this.module.beginTransaction();
		try {
			this.delete(obj, transaction, true);
			while (!this.moreObjectsToDelete.isEmpty()) {
				Set<Object> objectsToDelete = this.moreObjectsToDelete;
				this.moreObjectsToDelete = new HashSet<>();
				this.deleteObjects(objectsToDelete, transaction);
			}
			this.module.commit(transaction);
		} catch (ObjectCasketException | TableModuleException exc) {
			try {
				this.module.rollback(transaction);
			} catch (TableModuleException e) {
				ObjectCasketException.build(e);
			}
			ObjectCasketException.build(exc);
		}
	}

	private void deleteObjects(Set<Object> localObjectToDelete, Transaction transaction) throws ObjectCasketException {
		for (Object object : localObjectToDelete) {
			this.delete(object, transaction, false);
		}

	}

	private void delete(Object obj, Transaction transaction, boolean noJoinTableObject) throws ObjectCasketException {
		EntityFactory ef = this.entityFactoryMap.get(obj.getClass());
		ef.delete(obj, transaction, noJoinTableObject);
		return;
	}

	private void checkSession() throws ObjectCasketException {
		if (this.isOpen == null) {
			SessionException.Error.NoSession2Open.build();
		}
		if (!this.isOpen) {
			SessionException.Error.SessionTerminated.build();
		}

	}

	private String checkClassAndGetTableName(Class<?> clazz) throws ObjectCasketException {
		if (!Modifier.isFinal(clazz.getModifiers())) {
			SessionException.Error.NonFinalEntity.build(clazz.getSimpleName());
		}
		if (clazz.getAnnotation(javax.persistence.Entity.class) == null) {
			SessionException.Error.NoEntity.build(clazz.getSimpleName());
		}
		Table table = clazz.getAnnotation(Table.class);
		String tableName = (table != null) ? table.name() : "";
		tableName = tableName.isEmpty() ? clazz.getSimpleName() : tableName;
		if (this.tableName2ClassMap.containsKey(tableName)) {
			SessionException.Error.TableNameInUse.build(tableName, clazz.getSimpleName(), this.tableName2ClassMap.get(tableName).getSimpleName());
		}
		return tableName;
	}

	public EntityFactory getEntityFactory(Class<?> clazz) {
		EntityFactory ef = this.entityFactoryMap.get(clazz);
		return ef;
	}

	public Map<Class<?>, Set<FieldInfo>> getAnonymousFKfieldInfosMap() {
		return this.anonymousFKfieldInfosMap;
	}

	public Map<String, Class<?>> getTableName2ClassMap() {
		return this.tableName2ClassMap;
	}

	public void someObjectToDelete(Object obj) {
		this.moreObjectsToDelete.add(obj);
	}

	public TableModule getModule() {
		return this.module;
	}

	private static class SessionException extends ObjectCasketException {

		private static final long serialVersionUID = 1L;

		private SessionException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			SessionTerminated("Session terminated, created a new one."), //
			NoOpenSession("Session created but not opend, open first."), //
			NoSession2Terminate("Couldn't terminated an unopened session. open first."), //
			NoSession2Open("Couldn't open a terminated session create a new one."), //
			NoEntity("No entity anotation pressend for class %s. Only entities can be stored."), //
			NoEntityFactoryForClass("Undeclared class %s.  Declare first."), //
			NonFinalEntity("Missing final modifier in class %s. Entities are final classes."), //
			TableNameInUse("The table name %s for class %s ist already in use for class %s. Choose an other one.");

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ObjectCasketException {
				ObjectCasketException.build(new SessionException(this, arg));
			}

		}
	}

}
