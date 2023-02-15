package org.fuchss.objectcasket.objectpacker.impl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import org.fuchss.objectcasket.common.CasketError.CE1;
import org.fuchss.objectcasket.common.CasketError.CE3;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.port.Session.Exp;
import org.fuchss.objectcasket.tablemodule.port.TableModule;

@SuppressWarnings("java:S3011")
class ObjectBuilderCore<T> {

	protected static final String REF_COUNTER = "ref@counter";
	protected static final String SUPPLIER_COUNTER = "supplier@counter";
	protected static final String DEFAULT_FK_COLUMN = "id@_%s_%s"; // table name followed by column name
	protected static final String DEFAULT_JOIN_TABLE = "table@_%s_%s_%s"; // client tab. name followed by client col. name followed by supplier tab. name

	protected SessionImpl session;
	protected TableModule tabMod;

	protected ClassInfo<T> classInfo;
	private final Class<T> myClass;

	protected String tableName;
	protected Constructor<T> defaultConstructor;

	private final Set<String> allColumnNames = new HashSet<>();
	protected List<Field> valueFields = new ArrayList<>();
	protected List<Field> many2OneFields = new ArrayList<>();
	protected List<Field> many2ManyFields = new ArrayList<>();

	protected Map<Field, String> fieldColumnMap = new HashMap<>();
	protected Map<Field, Class<Serializable>> fieldTypeMap = new HashMap<>();
	protected Map<Field, M2MInfo<T, ?>> m2mFieldInfoMap = new HashMap<>();
	private final Map<Field, M2OInfo> many2OneFieldInfoMap = new HashMap<>();

	protected ObjectBuilderCore(SessionImpl session, TableModule tabMod, ClassInfo<T> info) throws CasketException {
		try {
			this.session = session;
			this.tabMod = tabMod;
			this.classInfo = info;
			this.myClass = info.myClass;
			this.init();
		} catch (NoSuchMethodException | SecurityException exc) {
			throw CE1.MISSING_DEFAULT_CONSTRUCTOR.defaultBuild(info.myClass);
		}
	}

	private void init() throws NoSuchMethodException, SecurityException, CasketException {
		this.tableName = this.classInfo.getTableName();
		this.defaultConstructor = this.myClass.getDeclaredConstructor();
		this.defaultConstructor.setAccessible(true);
		this.initValueFields();
		this.initMany2OneFields();
		this.initM2MFields();
	}

	private void initValueFields() throws CasketException {
		for (Field field : this.classInfo.allFields) {
			if (!(field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(ManyToMany.class))) {
				this.mkValueField(field);
			}
		}
	}

	private void initMany2OneFields() throws CasketException {
		for (Field field : this.classInfo.allFields) {
			if (field.isAnnotationPresent(ManyToOne.class)) {
				this.mkMany2OneField(field);
			}
		}
	}

	private void initM2MFields() throws CasketException {
		for (Field field : this.classInfo.allFields) {
			if (field.isAnnotationPresent(ManyToMany.class)) {
				this.mkM2MField(field);
			}
		}
	}

	private void mkValueField(Field field) throws CasketException {
		String columnName = ClassInfo.mkColumnName(field, REF_COUNTER, SUPPLIER_COUNTER);
		if (!this.allColumnNames.add(columnName))
			throw CE3.COLUMN_EXISTS.defaultBuild(columnName, field, this.myClass);
		this.valueFields.add(field);
		this.fieldColumnMap.put(field, columnName);
		this.fieldTypeMap.put(field, ClassInfo.mkValueType(field));
	}

	private void mkMany2OneField(Field field) throws CasketException {

		ClassInfo<?> theClassInfo = this.session.classInfoMap.getIfExists(field.getType());
		String columnName = mkFkColumnName(this.session, this.myClass, field, REF_COUNTER, SUPPLIER_COUNTER);
		if (!this.allColumnNames.add(columnName))
			throw CE3.COLUMN_EXISTS.defaultBuild(columnName, field, this.myClass);
		M2OInfo info = this.updateMany2OneInfo(columnName, theClassInfo.getTableName());
		this.many2OneFields.add(field);
		this.many2OneFieldInfoMap.put(field, info);
		this.fieldColumnMap.put(field, columnName);
		this.fieldTypeMap.put(field, theClassInfo.getType());
	}

	private M2OInfo updateMany2OneInfo(String columnName, String fkTableName) throws CasketException {
		Set<Exp> args = new HashSet<>();
		args.add(new Exp(M2OInfo.FIELD_CLIENT_TABLE_NAME, "==", this.tableName));
		args.add(new Exp(M2OInfo.FIELD_CLIENT_COLUMN_NAME, "==", columnName));
		this.session.beginTransaction();
		try {
			return this.loadOrPersist(columnName, fkTableName, args);
		} finally {
			this.session.endTransaction();
		}
	}

	private M2OInfo loadOrPersist(String columnName, String fkTableName, Set<Exp> args) throws CasketException {
		M2OInfo info = null;
		Set<M2OInfo> relevantInfos = this.session.getObjects(M2OInfo.class, args);
		if (!relevantInfos.isEmpty() && !(info = relevantInfos.iterator().next()).getSupplierTableName().equals(fkTableName))
			throw CE3.WRONG_CLASS_IN_MANY_TO_X_DECLARATION.defaultBuild(info.getSupplierTableName(), columnName, this.myClass);
		if (relevantInfos.isEmpty()) {
			this.session.persist(info = new M2OInfo(this.tableName, columnName, fkTableName));
		}
		return info;
	}

	@SuppressWarnings("unchecked")
	private <S> void mkM2MField(Field field) throws CasketException {
		Type fieldType = field.getGenericType();
		if (!fieldType.getTypeName().startsWith(Set.class.getTypeName()))
			throw CE3.WRONG_CONTAINER_IN_MANY_TO_MANY_DECLARATION.defaultBuild(fieldType.getTypeName(), field, this.myClass);
		Class<S> remoteClass = (Class<S>) ((ParameterizedType) fieldType).getActualTypeArguments()[0];
		ClassInfo<S> remoteClassInfo = (ClassInfo<S>) this.session.classInfoMap.getIfExists(remoteClass);
		M2MInfo<T, ?> info = this.updateMany2ManyInfo(field, remoteClassInfo);
		this.many2ManyFields.add(field);
		this.m2mFieldInfoMap.put(field, info);
	}

	private <S> M2MInfo<T, S> updateMany2ManyInfo(Field field, ClassInfo<S> classInfo) throws CasketException {
		String columnName = mkFkColumnName(this.session, this.myClass, field, REF_COUNTER, SUPPLIER_COUNTER);
		String joinTableName = mkJoinTabName(this.session, this.myClass, field, REF_COUNTER, SUPPLIER_COUNTER);
		Set<Exp> args = new HashSet<>();
		args.add(new Exp(M2MInfo.FIELD_CLIENT_TABLE_NAME, "==", this.tableName));
		args.add(new Exp(M2MInfo.FIELD_CLIENT_COLUMN_NAME, "==", columnName));
		this.session.beginTransaction();
		try {
			return this.loadOrPersist(classInfo, columnName, joinTableName, args);
		} finally {
			this.session.endTransaction();
		}
	}

	@SuppressWarnings("unchecked")
	private <S> M2MInfo<T, S> loadOrPersist(ClassInfo<S> classInfo, String columnName, String joinTableName, Set<Exp> args) throws CasketException {
		M2MInfo<T, S> info = null;
		@SuppressWarnings("rawtypes")
		Set<M2MInfo> relevantInfos = this.session.getObjects(M2MInfo.class, args);
		if (!relevantInfos.isEmpty() && !(info = relevantInfos.iterator().next()).getSupplierTableName().equals(classInfo.getTableName()) && !info.getJoinTableName().equals(joinTableName))
			throw CE3.WRONG_CLASS_IN_MANY_TO_X_DECLARATION.defaultBuild(info.getSupplierTableName(), columnName, this.myClass);

		args.clear();
		args.add(new Exp(M2MInfo.FIELD_JOIN_TABLE_NAME, "==", joinTableName));
		@SuppressWarnings("rawtypes")
		Set<M2MInfo> controlInfos = this.session.getObjects(M2MInfo.class, args);
		if (relevantInfos.size() != controlInfos.size())
			throw CE3.WRONG_CLASS_IN_MANY_TO_X_DECLARATION.defaultBuild(joinTableName, columnName, this.myClass);

		if (relevantInfos.isEmpty()) {
			this.session.persist(info = new M2MInfo<>(this.tableName, columnName, classInfo.getTableName(), joinTableName));
		}
		info.init(this.classInfo, classInfo);
		return info;
	}

	private static String mkFkColumnName(SessionImpl session, Class<?> client, Field field, String... prohibited) throws CasketException {
		String clientTableName = session.classTableNameMap.getIfExists(client);
		Column column = field.getAnnotation(Column.class);
		String defaultName = String.format(DEFAULT_FK_COLUMN, clientTableName, field.getName());

		String fkColumnName = null;
		fkColumnName = ((column == null) || (fkColumnName = column.name()).isEmpty()) ? defaultName : fkColumnName;
		if (Arrays.asList(prohibited).contains(fkColumnName))
			throw CE1.INVALID_NAME.defaultBuild(fkColumnName);
		return fkColumnName;
	}

	private static String mkJoinTabName(SessionImpl session, Class<?> client, Field field, String... prohibited) throws CasketException {
		String clientColumnName = ClassInfo.mkColumnName(field, prohibited);
		String clientTableName = session.classTableNameMap.getIfExists(client);
		Class<?> remoteClass = (Class<?>) ((ParameterizedType) (field.getGenericType())).getActualTypeArguments()[0];
		String supplierTableName = session.classTableNameMap.getIfExists(remoteClass);

		String defaultName = String.format(DEFAULT_JOIN_TABLE, clientTableName, clientColumnName, supplierTableName);

		JoinTable joinTable = field.getAnnotation(JoinTable.class);
		String joinTableName = null;
		joinTableName = ((joinTable == null) || (joinTableName = joinTable.name()).isEmpty()) ? defaultName : joinTableName;
		if (Arrays.asList(prohibited).contains(joinTableName))
			throw CE1.INVALID_NAME.defaultBuild(joinTableName);
		return joinTableName;
	}

}
