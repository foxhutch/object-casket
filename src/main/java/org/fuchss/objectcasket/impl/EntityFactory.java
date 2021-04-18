package org.fuchss.objectcasket.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.fuchss.objectcasket.impl.field.FieldInfo;
import org.fuchss.objectcasket.impl.field.FieldInfo.Kind;
import org.fuchss.objectcasket.impl.field.Many2ManyFieldInfo;
import org.fuchss.objectcasket.impl.field.Many2OneFieldInfo;
import org.fuchss.objectcasket.impl.field.One2ManyFieldInfo;
import org.fuchss.objectcasket.impl.field.One2OneFieldInfo;
import org.fuchss.objectcasket.impl.field.ValueFieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketCMP;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.sqlconnector.port.SqlArg;
import org.fuchss.tablemodule.port.Row;
import org.fuchss.tablemodule.port.RowPrototype;
import org.fuchss.tablemodule.port.Table;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.TablePrototype;
import org.fuchss.tablemodule.port.Transaction;

public class EntityFactory {
	private Map<Object, Entity> entityMap = new HashMap<>();
	private Map<Object, Object> pkObjectMap = new HashMap<>();

	private Table table;
	private TablePrototype tablePrototype;
	private String tableName;
	private Class<?> clazz;
	private Field pkField;
	private ValueFieldInfo pkInfo;

	private SessionImpl session;
	private List<FieldInfo> anonymousfkFieldInfos = new ArrayList<>();
	private Map<String, FieldInfo> columnNameFkFieldInfoMap = new HashMap<>();
	private List<Field> allFields = new ArrayList<>();
	private List<FieldInfo> allFieldInfos = new ArrayList<>();
	private Map<Field, FieldInfo> fieldInfoMap = new HashMap<>();

	private List<Field> valueFields = new ArrayList<>();
	private List<Field> one2OneFields = new ArrayList<>();
	private List<Field> one2ManyFields = new ArrayList<>();
	private List<Field> many2OneFields = new ArrayList<>();
	private List<Field> many2ManyFields = new ArrayList<>();

	private TableModuleException tableModuleException;
	private Constructor<?> defaultConstructor;

	private static Map<ObjectCasketCMP, SqlArg.CMP> compareMap = new HashMap<>() {
		private static final long serialVersionUID = 1L;

		{
			this.put(ObjectCasketCMP.EQUAL, SqlArg.CMP.EQUAL);
			this.put(ObjectCasketCMP.GREATER, SqlArg.CMP.GREATER);
			this.put(ObjectCasketCMP.LESS, SqlArg.CMP.LESS);
			this.put(ObjectCasketCMP.GREATEREQ, SqlArg.CMP.GREATEREQ);
			this.put(ObjectCasketCMP.LESSEQ, SqlArg.CMP.LESSEQ);
		}
	};

	public EntityFactory(String tableName, Class<?> clazz, SessionImpl session) throws ObjectCasketException {

		try {
			this.session = session;
			this.tableName = tableName;
			this.clazz = clazz;
			this.defaultConstructor = this.clazz.getDeclaredConstructor();
			this.defaultConstructor.setAccessible(true);
			this.session.register(clazz, this);
			this.mkValueFieldInfo();
		} catch (NoSuchMethodException e) {
			EntityFactoryException.Error.MissingDefaultConstructor.build(this.clazz.getName());
		} catch (SecurityException e) {
			ObjectCasketException.build(e);
		}

	}

	public void initFields() throws ObjectCasketException {
		for (Field field : this.allFields) {
			this.mkField(field, this.tableName);
		}
	}

	public void addAnonymousFKfields(Set<FieldInfo> set) {
		this.anonymousfkFieldInfos.addAll(set);
	}

	public Entity entity(Object obj, Transaction transaction) throws ObjectCasketException {
		Entity entity = null;
		if ((entity = this.entityMap.get(obj)) == null) {
			this.entityMap.put(obj, entity = this.mkNewEntity(obj, transaction));
			this.pkObjectMap.put(entity.getPk(), obj);
		}
		return entity;
	}

	private Entity mkNewEntity(Object obj, Transaction transaction) throws ObjectCasketException {
		Entity entity = null;
		try {
			entity = new Entity(obj, this, this.session);
			entity.setRow(this.table.mkRow(transaction));
			entity.create(transaction);
		} catch (TableModuleException exc) {
			ObjectCasketException.build(exc);
		}
		return entity;
	}

	public Object getObjByPk(Object pk, Transaction transaction) throws ObjectCasketException {
		Object obj = this.pkObjectMap.get(pk);
		if (obj == null) {
			Entity entity = this.entityByPk(pk, null, transaction);
			this.entityMap.put(obj = entity.getMe(), entity);
		}
		return obj;
	}

	private Object getObjByPkOrRow(Object pk, Row row, Transaction transaction) throws ObjectCasketException {
		Object obj = this.pkObjectMap.get(pk);
		if (obj == null) {
			Entity entity = this.entityByPk(pk, row, transaction);
			this.entityMap.put(obj = entity.getMe(), entity);
		}
		return obj;
	}

	public Set<Object> getAllObjs(Transaction transaction) throws ObjectCasketException {
		Set<Object> allObjects = new HashSet<>();
		try {
			List<Row> rows = this.table.allRows();
			for (Row row : rows) {
				allObjects.add(this.getObjByPkOrRow(row.getPK(transaction, this.pkInfo.getColumnType()), row, transaction));
			}
		} catch (TableModuleException exc) {
			ObjectCasketException.build(exc);
		}
		return allObjects;
	}

	public Set<Object> getObjsByPrototype(Object prototype, Map<String, ObjectCasketCMP> cmps, Transaction transaction) throws ObjectCasketException {
		Set<Object> allObjects = new HashSet<>();
		if (cmps == null) {
			return allObjects;
		}
		try {
			RowPrototype rowPrototype = this.table.mkRowPrototype();
			for (String fieldName : cmps.keySet()) {
				this.setPrototype(rowPrototype, prototype, this.clazz.getDeclaredField(fieldName), cmps);
			}
			List<Row> rows = this.table.allRowsByPrototype(rowPrototype);
			for (Row row : rows) {
				allObjects.add(this.getObjByPkOrRow(row.getPK(transaction, this.pkInfo.getColumnType()), row, transaction));
			}
		} catch (TableModuleException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException exc) {
			ObjectCasketException.build(exc);
		}
		return allObjects;
	}

	private void setPrototype(RowPrototype rowPrototype, Object prototype, Field field, Map<String, ObjectCasketCMP> cmps) throws TableModuleException, ObjectCasketException, IllegalArgumentException, IllegalAccessException {
		if (!this.valueFields.contains(field) && !Objects.equals(this.pkField, field)) {
			EntityFactoryException.Error.KISSLoad.build(field.getName());
		}
		field.setAccessible(true);
		FieldInfo info = this.fieldInfoMap.get(field);
		if (info == null) {
			EntityFactoryException.Error.UnknownField.build(field.getName(), this.clazz.getSimpleName());
		}
		ObjectCasketCMP cmp = (cmp = cmps.get(field.getName())) == null ? ObjectCasketCMP.EQUAL : cmp;
		rowPrototype.set(info.getColumnName(), field.get(prototype), EntityFactory.compareMap.get(cmp));
	}

	public Set<Object> getObjsByFk(Object fk, String columnName, Transaction transaction) throws ObjectCasketException {
		Set<Object> allObjects = new HashSet<>();
		try {
			RowPrototype prototype = this.table.mkRowPrototype();
			prototype.set(columnName, fk, SqlArg.CMP.EQUAL);
			List<Row> rows = this.table.allRowsByPrototype(prototype);
			for (Row row : rows) {
				allObjects.add(this.getObjByPkOrRow(row.getPK(transaction, this.pkInfo.getColumnType()), row, transaction));
			}
		} catch (TableModuleException exc) {
			ObjectCasketException.build(exc);
		}
		return allObjects;
	}

	private Entity entityByPk(Object pk, Row row, Transaction transaction) throws ObjectCasketException {
		Object obj = this.pkObjectMap.get(pk);
		try {
			if (obj != null) {
				return this.entityMap.get(obj);
			}

			obj = this.defaultConstructor.newInstance();
			this.pkField.set(obj, pk);
			this.pkObjectMap.put(pk, obj);
			return this.mkExistingEntity(obj, row, pk, transaction);
		} catch (ObjectCasketException | InstantiationException | IllegalAccessException | InvocationTargetException | TableModuleException exc) {
			this.pkObjectMap.remove(pk);
			ObjectCasketException.build(exc);
		}
		return null;
	}

	private Entity mkExistingEntity(Object obj, Row myRow, Object pk, Transaction transaction) throws TableModuleException, ObjectCasketException {
		Entity entity = null;

		entity = new Entity(obj, this, this.session);
		Row row = (myRow != null) ? myRow : this.loadRowByPK(pk);
		this.entityMap.put(obj, entity);
		entity.setRow(row);
		try {
			entity.load(transaction);
		} catch (ObjectCasketException exc) {
			this.entityMap.remove(obj);
			ObjectCasketException.build(exc);
		}
		return entity;
	}

	private Row loadRowByPK(Object pk) throws ObjectCasketException {
		try {
			RowPrototype prototype = this.table.mkRowPrototype();
			prototype.set(this.pkInfo.getColumnName(), pk, SqlArg.CMP.EQUAL);
			List<Row> rows = this.table.allRowsByPrototype(prototype);
			if (rows.size() != 1) {
				EntityFactoryException.Error.NonUniquePK.build(Objects.toString(pk), this.tableName);
			}
			return rows.get(0);
		} catch (TableModuleException exc) {
			ObjectCasketException.build(exc);
		}
		return null;
	}

	public Entity mkObjectAndEntity(Transaction transaction) throws ObjectCasketException {
		try {
			Object obj = this.defaultConstructor.newInstance();
			return this.entity(obj, transaction);
		} catch (Exception e) {
			ObjectCasketException.build(e);
		}
		return null;
	}

	public void delete(Object obj, Transaction transaction, boolean noJoinTableObject) throws ObjectCasketException {
		Entity entity = null;
		if ((entity = this.entityMap.get(obj)) == null) {
			return;
		}
		if (noJoinTableObject && entity.isJoinTableEntity()) {
			EntityFactoryException.Error.KISSJoinTable.build();
		}
		try {
			Row rowToDelete = entity.clear(transaction);
			this.table.delete(transaction, rowToDelete);
			this.pkObjectMap.remove(entity.getPk());
			this.entityMap.remove(obj);
		} catch (TableModuleException e) {
			ObjectCasketException.build(e);
		}

	}

	public void createOrAssignTable() throws ObjectCasketException {
		try {
			this.table = this.session.getModule().assignOrCreateTable(this.tablePrototype, null);
		} catch (TableModuleException exc) {
			ObjectCasketException.build(exc);
		}
	}

	public void initTablePrototype() throws ObjectCasketException {
		try {
			this.tablePrototype = this.session.getModule().mkTablePrototype(this.tableName);
		} catch (TableModuleException exc) {
			ObjectCasketException.build(exc);
		}
		try {
			List<ValueFieldInfo> valueFieldInfos = this.allFieldInfos(Kind.VALUE, ValueFieldInfo.class);
			valueFieldInfos.forEach(this::mkColumn);

			if (this.tableModuleException != null) {
				ObjectCasketException.build(this.tableModuleException);
			}
		} finally {
			this.tableModuleException = null;
		}
	}

	public void completeTablePrototype() throws ObjectCasketException {
		try {
			List<One2OneFieldInfo> fkO2OFieldInfos = this.allFieldInfos(Kind.ONE2ONE, One2OneFieldInfo.class);
			List<Many2OneFieldInfo> fkM2OFieldInfos = this.allFieldInfos(Kind.MANY2ONE, Many2OneFieldInfo.class);

			fkM2OFieldInfos.forEach(this::mkColumn);
			fkO2OFieldInfos.forEach(this::mkColumn);
			this.anonymousfkFieldInfos.forEach(this::mkAnonymousFK);

			if (this.tableModuleException != null) {
				ObjectCasketException.build(this.tableModuleException);
			}
		} finally {
			this.tableModuleException = null;
		}
	}

	public void removePrototype() throws ObjectCasketException {
		this.tablePrototype = null;
		this.table = null;
	}

	@SuppressWarnings("unchecked")
	private <X extends FieldInfo> List<X> allFieldInfos(FieldInfo.Kind kind, Class<X> clazz) {
		List<X> allInfos = new ArrayList<>();
		for (FieldInfo info : this.allFieldInfos) {
			if (info.is(kind)) {
				allInfos.add((X) info);
			}
		}
		return allInfos;
	}

	private void mkColumn(ValueFieldInfo info) {
		try {
			if (this.tableModuleException == null) {
				this.tablePrototype.addColumn(info.getColumnName(), info.getColumnType(), info.getColumnSqlType(), info.getFlags(), null);
			}
		} catch (TableModuleException exc) {
			this.tableModuleException = exc;
		}
	}

	private void mkColumn(Many2OneFieldInfo info) {
		try {
			if (this.tableModuleException == null) {
				this.tablePrototype.addColumn(info.getColumnName(), info.getColumnType(), null, info.getFlags(), null);
			}
		} catch (TableModuleException exc) {
			this.tableModuleException = exc;
		}

	}

	private void mkColumn(One2OneFieldInfo info) {
		try {
			if (this.tableModuleException == null) {
				this.tablePrototype.addColumn(info.getColumnName(), info.getColumnType(), null, info.getFlags(), null);
			}
		} catch (TableModuleException exc) {
			this.tableModuleException = exc;
		}

	}

	private void mkAnonymousFK(FieldInfo info) {
		try {
			if (this.tableModuleException != null) {
				return;
			}
			if (info.getKind() == Kind.ONE2ONE) {
				One2OneFieldInfo o2oInfo = (One2OneFieldInfo) info;
				this.tablePrototype.addColumn(o2oInfo.getRemoteFkColumnName(), o2oInfo.getMyPkType(), null, info.getFlags(), null);
				this.columnNameFkFieldInfoMap.put(o2oInfo.getRemoteFkColumnName(), o2oInfo);
			}
			if (info.getKind() == Kind.ONE2MANY) {
				One2ManyFieldInfo o2mInfo = (One2ManyFieldInfo) info;
				this.tablePrototype.addColumn(o2mInfo.getRemoteFkColumnName(), o2mInfo.getMyPkType(), null, o2mInfo.getFlags(), null);
				this.columnNameFkFieldInfoMap.put(o2mInfo.getRemoteFkColumnName(), o2mInfo);
			}
			if (info.getKind() == Kind.MANY2MANY) {
				Many2ManyFieldInfo m2mInfo = (Many2ManyFieldInfo) info;
				if (m2mInfo.isAnonymousRemoteFK() && !this.tablePrototype.getColumnNames().contains(m2mInfo.getRemoteFKColumnName())) {
					this.tablePrototype.addColumn(m2mInfo.getRemoteFKColumnName(), m2mInfo.getRemotePkType(), null, m2mInfo.getFlags(), null);
					this.columnNameFkFieldInfoMap.put(m2mInfo.getRemoteFKColumnName(), m2mInfo);
				}
				if (m2mInfo.isAnonymousOwnFK() && !this.tablePrototype.getColumnNames().contains(m2mInfo.getOwnFKColumnName())) {
					this.tablePrototype.addColumn(m2mInfo.getOwnFKColumnName(), m2mInfo.getMyPkType(), null, m2mInfo.getFlags(), null);
					this.columnNameFkFieldInfoMap.put(m2mInfo.getOwnFKColumnName(), m2mInfo);
				}
			}
		} catch (TableModuleException exc) {
			this.tableModuleException = exc;
		}

	}

	private void mkValueFieldInfo() throws ObjectCasketException {
		this.mkFields(this.clazz, this.tableName);
		for (Field field : this.allFields) {
			this.mkValueField(field, this.tableName);
		}
	}

	private void mkFields(Class<?> entityClass, String tableName) {
		List<Field> relevantFields = Arrays.asList(entityClass.getDeclaredFields()).stream().filter(field -> !field.isAnnotationPresent(Transient.class) && !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
		this.allFields.addAll(relevantFields);
		Class<?> superClass = entityClass.getSuperclass();
		if ((superClass == null) || superClass.equals(Object.class)) {
			return;
		}
		this.mkFields(superClass, tableName);
	}

	private void mkField(Field field, String tableName) throws ObjectCasketException {
		if (field.isAnnotationPresent(Transient.class) || this.fieldInfoMap.containsKey(field)) {
			return;
		}

		field.setAccessible(true);
		FieldInfo info = null;
		if ((info == null) && field.isAnnotationPresent(OneToOne.class)) {
			info = new One2OneFieldInfo(field, this.clazz, this.pkField.getType(), tableName, this.session);
			this.one2OneFields.add(field);
		}
		if ((info == null) && field.isAnnotationPresent(ManyToOne.class)) {
			info = new Many2OneFieldInfo(field, this.clazz, tableName, this.session);
			this.many2OneFields.add(field);
		}
		if ((info == null) && field.isAnnotationPresent(OneToMany.class)) {
			info = new One2ManyFieldInfo(field, this.clazz, this.pkField.getType(), tableName, this.session);
			this.one2ManyFields.add(field);
		}
		if ((info == null) && field.isAnnotationPresent(ManyToMany.class)) {
			info = new Many2ManyFieldInfo(field, this.clazz, this.pkField.getType(), tableName, this.session);
			this.many2ManyFields.add(field);
		}

		this.fieldInfoMap.put(field, info);
		this.allFieldInfos.add(info);
		return;

	}

	private void mkValueField(Field field, String tableName) throws ObjectCasketException {
		if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToMany.class)
				|| field.isAnnotationPresent(ManyToMany.class)) {
			return;
		}
		field.setAccessible(true);
		ValueFieldInfo vInfo = new ValueFieldInfo(field, this.clazz, tableName, this.session);

		if (vInfo.isPK()) {
			this.pkField = field;
			this.pkInfo = vInfo;
		} else {
			this.valueFields.add(field);
		}
		this.fieldInfoMap.put(field, vInfo);
		this.allFieldInfos.add(vInfo);
		return;

	}

	public List<Field> getAllValueFields() {
		return this.valueFields;
	}

	public List<Field> getAllMany2OneFields() {
		return this.many2OneFields;
	}

	public List<Field> getAllOne2OneFields() {
		return this.one2OneFields;
	}

	public List<Field> getAllOne2ManyFields() {
		return this.one2ManyFields;
	}

	public List<Field> getAllMany2ManyFields() {
		return this.many2ManyFields;
	}

	public Field getPkField() {
		return this.pkField;
	}

	public List<FieldInfo> getAllFieldInfos() {
		return this.allFieldInfos;

	}

	public List<Field> getAllFields() {
		return this.allFields;

	}

	public List<FieldInfo> getAnonymousFkFieldInfos() {
		return this.anonymousfkFieldInfos;

	}

	public FieldInfo getAnonymousFkFieldInfo(String columnName) {
		return this.columnNameFkFieldInfoMap.get(columnName);

	}

	public FieldInfo getFieldInfo(Field field) {
		return this.fieldInfoMap.get(field);
	}

	public Class<?> getType() {
		return this.clazz;
	}

	static class EntityFactoryException extends ObjectCasketException {

		private static final long serialVersionUID = 1L;

		private EntityFactoryException(Error error, String... arg) {
			super(error.format(arg));
		}

		public static enum Error {

			KISSJoinTable("Impossible to delete join table entity directly. Remove over many-to-many association."), //
			NonUniquePK("The pk object %s is not unique or unknown in table %s. The database is corruped!"), //
			KISSLoad("Impossible to use non value field %s for protoyping."), //
			UnknownField("The field %s is not a stored value field of class %s!"), //
			MissingDefaultConstructor("There is no default constructor for class %s!"); //

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ObjectCasketException {
				ObjectCasketException.build(new EntityFactoryException(this, arg));
			}

		}
	}

}
