package org.fuchss.objectcasket.objectpacker.impl;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Table;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.IntolerantHashMap;
import org.fuchss.objectcasket.common.IntolerantMap;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;

class SessionImpl extends SyncedSession implements Session {

	protected IntolerantMap<Class<?>, ClassInfo<?>> classInfoMap = new IntolerantHashMap<>();
	protected IntolerantMap<Class<?>, String> classTableNameMap = new IntolerantHashMap<>();
	private IntolerantMap<String, Class<?>> tableNameClassMap = new IntolerantHashMap<>();

	protected IntolerantMap<M2MInfo<?, ?>, JoinTableBuilder<?, ?>> joinTabFactoryFactoryMap = new IntolerantHashMap<>();

	protected boolean check; // enables checking for reserved symbols in names
	protected Boolean assignOnly; // true = assignOnly, false = createOnly, null = assign or create

	private SessionImpl(TableModule tabMod, ConfigurationImpl config) {
		super(tabMod, config);
	}

	static SessionImpl editDomainBuilder(TableModuleFactory modFac, ConfigurationImpl config) throws CasketException {
		SessionImpl session = new SessionImpl(modFac.newTableModule(config.getConfig()), config);
		session.assignOnly = true;
		session.init(modFac);
		session.assignOnly = null;
		return session;
	}

	static SessionImpl mkDomainBuilder(TableModuleFactory modFac, ConfigurationImpl config) throws CasketException {
		SessionImpl session = new SessionImpl(modFac.newTableModule(config.getConfig()), config);
		session.assignOnly = false;
		session.init(modFac);
		return session;
	}

	static SessionImpl createSession(TableModuleFactory modFac, ConfigurationImpl config) throws CasketException {
		SessionImpl session = new SessionImpl(modFac.newTableModule(config.getConfig()), config);
		session.assignOnly = true;
		session.init(modFac);
		return session;
	}

	private void init(TableModuleFactory modFac) throws CasketException {
		try {
			this.declareClass(M2OInfo.class);
			this.declareClass(M2MInfo.class);
			this.check = true;
		} catch (Exception exc) {
			modFac.closeModule(this.tableModule);
			throw CasketException.build(exc);
		}

	}

	@Override
	public synchronized void declareClass(Class<?>... clazz) throws CasketException {
		Set<Class<?>> processedClasses = new HashSet<>();
		Set<M2MInfo<?, ?>> processedM2MInfo = new HashSet<>();
		if (this.transaction != null)
			throw CasketError.TRANSACTION_RUNNING.build();
		try {
			for (Class<?> singleClass : clazz)
				this.forwardDeclareClass(singleClass, processedClasses);
			for (Class<?> singleClass : clazz) {
				ObjectBuilder<?> builder = new ObjectBuilder<>(this, this.tableModule, this.classInfoMap.getIfExists(singleClass));
				this.objectFactoryMap.putIfNew(singleClass, builder);
				for (M2MInfo<?, ?> info : builder.m2mFieldInfoMap.values()) {
					processedM2MInfo.add(info);
					this.joinTabFactoryFactoryMap.putIfNew(info, new JoinTableBuilder<>(this, this.tableModule, info));
				}
			}
		} catch (Exception exc) {
			this.undoDeclare(processedClasses, processedM2MInfo);
			throw CasketException.build(exc);
		}
	}

	private void undoDeclare(Set<Class<?>> processedClasses, Set<M2MInfo<?, ?>> processedM2MInfo) {
		for (Class<?> singleClass : processedClasses) {
			this.classInfoMap.remove(singleClass);
			String tableName = this.classTableNameMap.remove(singleClass);
			this.tableNameClassMap.remove(tableName);
			this.objectFactoryMap.remove(singleClass);
		}
		for (M2MInfo<?, ?> info : processedM2MInfo)
			this.joinTabFactoryFactoryMap.remove(info);
	}

	private void forwardDeclareClass(Class<?> clazz, Set<Class<?>> processedClasses) throws CasketException {
		Util.objectsNotNull(clazz);
		String tableName = this.checkClassAndGetTableName(clazz);
		ClassInfo<?> info = new ClassInfo<>(clazz, tableName);
		this.classTableNameMap.put(clazz, tableName);
		this.tableNameClassMap.put(tableName, clazz);
		processedClasses.add(clazz);
		this.classInfoMap.put(clazz, info);
	}

	private String checkClassAndGetTableName(Class<?> clazz) throws CasketException {
		String tableName = this.computeTableName(clazz);
		if (Boolean.TRUE.equals(this.assignOnly) && !this.tableModule.tableExists(tableName))
			throw CasketError.MISSING_TABLE.build();
		if (Boolean.FALSE.equals(this.assignOnly) && this.tableModule.tableExists(tableName))
			throw CasketError.CLASS_ALREADY_DECLARED.build();
		if (this.classInfoMap.containsKey(clazz) || this.tableNameClassMap.containsKey(tableName))
			throw CasketError.CLASS_ALREADY_DECLARED.build();
		return tableName;
	}

	private String computeTableName(Class<?> clazz) throws CasketException {
		if (!Modifier.isFinal(clazz.getModifiers())) {
			throw CasketError.NON_FINAL_CLASS.build();
		}
		if (clazz.getAnnotation(javax.persistence.Entity.class) == null) {
			throw CasketError.MISSING_ENTITY_ANNOTATION.build();
		}
		Table table = clazz.getAnnotation(Table.class);
		String tableName = (table != null) ? table.name() : "";
		tableName = tableName.isEmpty() ? clazz.getSimpleName() : tableName;
		if (this.check && tableName.contains("@"))
			throw CasketError.INVALID_NAME.build();
		return tableName;
	}

	<T> boolean isManaged(T obj) throws CasketException {
		Util.objectsNotNull(obj);
		@SuppressWarnings("unchecked")
		ObjectBuilder<T> objFactory = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(obj.getClass());
		return objFactory.objectRowMap.containsKey(obj);
	}

	<T> void addClient(T supplier) throws CasketException {
		Util.objectsNotNull(supplier);
		@SuppressWarnings("unchecked")
		ObjectBuilder<T> objFac = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(supplier.getClass());
		objFac.addClient(supplier, this.transaction);
	}

	<T> void removeClient(Class<T> clazz, Serializable pk) throws CasketException {
		Util.objectsNotNull(clazz);
		if (pk == null)
			return;
		@SuppressWarnings("unchecked")
		ObjectBuilder<T> objFac = (ObjectBuilder<T>) this.objectFactoryMap.getIfExists(clazz);
		objFac.removeClient(pk, this.transaction);
	}

}
