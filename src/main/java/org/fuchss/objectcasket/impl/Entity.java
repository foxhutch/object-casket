package org.fuchss.objectcasket.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.fuchss.objectcasket.impl.field.FieldInfo;
import org.fuchss.objectcasket.impl.field.Many2ManyFieldInfo;
import org.fuchss.objectcasket.impl.field.Many2OneFieldInfo;
import org.fuchss.objectcasket.impl.field.One2ManyFieldInfo;
import org.fuchss.objectcasket.impl.field.One2OneFieldInfo;
import org.fuchss.objectcasket.impl.writer.AnonymousClearer;
import org.fuchss.objectcasket.impl.writer.Loader;
import org.fuchss.objectcasket.impl.writer.M2M_Clearer;
import org.fuchss.objectcasket.impl.writer.M2M_ValueWriter;
import org.fuchss.objectcasket.impl.writer.M2O_Clearer;
import org.fuchss.objectcasket.impl.writer.M2O_ValueWriter;
import org.fuchss.objectcasket.impl.writer.O2M_Clearer;
import org.fuchss.objectcasket.impl.writer.O2M_ValueWriter;
import org.fuchss.objectcasket.impl.writer.O2O_Clearer;
import org.fuchss.objectcasket.impl.writer.O2O_ValueWriter;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Row;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.Transaction;

public class Entity {
	private Row row;
	private Object obj;
	private EntityFactory ef;
	private Object pk;
	private SessionImpl session;
	private boolean isJoinTableEntity = false;

	private Map<Field, Object> o2o_Map = new HashMap<>();
	private Map<Field, Set<Object>> o2m_Map = new HashMap<>();
	private Map<Field, Object> m2o_Map = new HashMap<>();
	private Map<Field, Set<Object>> m2m_Map = new HashMap<>();
	private Map<Field, Map<Object, Object>> m2m_JoinTableMap = new HashMap<>();

	public Entity(Object obj, EntityFactory ef, SessionImpl session) throws ObjectCasketException {
		try {
			this.obj = obj;
			this.ef = ef;
			this.session = session;
			this.pk = this.ef.getPkField().get(this.obj);
		} catch (Exception exc) {
			ObjectCasketException.build(exc);
		}
	}

	public void setJoinTableEntity() {
		this.isJoinTableEntity = true;
	}

	public boolean isJoinTableEntity() {
		return this.isJoinTableEntity;
	}

	public void load(Transaction transaction) throws ObjectCasketException {
		Loader loader = new Loader(this.obj, this.row, transaction);
		try {
			for (Field field : this.ef.getAllValueFields()) {
				loader.loadValueField(field, this.ef.getFieldInfo(field));
			}
			for (Field field : this.ef.getAllMany2OneFields()) {
				this.m2o_Map.put(field, loader.load_M2O_Field(field, (Many2OneFieldInfo) this.ef.getFieldInfo(field), this.session));
			}
			for (Field field : this.ef.getAllOne2OneFields()) {
				this.o2o_Map.put(field, loader.load_O2O_Field(field, (One2OneFieldInfo) this.ef.getFieldInfo(field), this.session));
			}
			for (Field field : this.ef.getAllOne2ManyFields()) {
				this.o2m_Map.put(field, loader.load_O2M_Field(this.pk, field, (One2ManyFieldInfo) this.ef.getFieldInfo(field), this.session));
			}
			for (Field field : this.ef.getAllMany2ManyFields()) {
				this.m2m_Map.put(field, loader.load_M2M_Field(this.pk, field, (Many2ManyFieldInfo) this.ef.getFieldInfo(field), this.session, this.m2m_JoinTableMap));
			}
		} catch (Exception exc) {
			ObjectCasketException.build(exc);
		}
	}

	public void create(Transaction transaction) throws ObjectCasketException {
		try {
			for (Field field : this.ef.getAllValueFields()) {
				this.writeValueField(field, transaction);
			}
			Field pkField = this.ef.getPkField();
			FieldInfo pkFieldInfo = this.ef.getFieldInfo(pkField);
			Object pkVal = pkField.get(this.obj);
			if (pkVal != null) {
				this.row.write(transaction, pkFieldInfo.getColumnName(), pkVal);
			}
			pkField.set(this.obj, this.pk = this.row.getPK(transaction, pkFieldInfo.getColumnType()));
		} catch (Exception exc) {
			ObjectCasketException.build(exc);
		}
	}

	public void persist(Transaction transaction) throws ObjectCasketException {
		try {
			for (Field field : this.ef.getAllValueFields()) {
				this.writeValueField(field, transaction);
			}
			for (Field field : this.ef.getAllMany2OneFields()) {
				new M2O_ValueWriter(field, transaction, this.session, this, this.ef).persist();
			}
			for (Field field : this.ef.getAllOne2OneFields()) {
				new O2O_ValueWriter(field, transaction, this.session, this, this.ef).persist();
			}
			for (Field field : this.ef.getAllOne2ManyFields()) {
				new O2M_ValueWriter(field, transaction, this.session, this, this.ef).persist();
			}
			for (Field field : this.ef.getAllMany2ManyFields()) {
				new M2M_ValueWriter(field, transaction, this.session, this).persist();
			}
			this.ef.getPkField().set(this.obj, this.pk);
		} catch (TableModuleException | IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
	}

	public Row clear(Transaction transaction) throws ObjectCasketException {
		try {
			for (FieldInfo info : this.ef.getAnonymousFkFieldInfos()) {
				new AnonymousClearer(info, transaction, this.session, this).clear();
			}
			for (Field field : this.ef.getAllMany2OneFields()) {
				new M2O_Clearer(field, transaction, this.session, this, this.ef).clear();
			}
			for (Field field : this.ef.getAllOne2OneFields()) {
				new O2O_Clearer(field, transaction, this.session, this, this.ef).clear();
			}
			for (Field field : this.ef.getAllOne2ManyFields()) {
				new O2M_Clearer(field, transaction, this.session, this, this.ef).clear();
			}
			for (Field field : this.ef.getAllMany2ManyFields()) {
				new M2M_Clearer(field, transaction, this.session, this).clear();
			}
			return this.row;
		} catch (IllegalArgumentException exc) {
			ObjectCasketException.build(exc);
		}
		return null;
	}

	private void writeValueField(Field field, Transaction transaction) throws TableModuleException, IllegalArgumentException, IllegalAccessException {
		FieldInfo info = this.ef.getFieldInfo(field);
		Object val = field.get(this.obj);
		if (Objects.equals(val, this.row.read(transaction, info.getColumnName(), info.getColumnType(), field))) {
			return;
		}
		this.row.write(transaction, info.getColumnName(), val);
	}

	public Object getJoinTableEntity(Object remote, String joinTableName) throws ObjectCasketException {

		Field joinField = null;
		for (Field field : this.ef.getAllMany2ManyFields()) {
			Many2ManyFieldInfo info = (Many2ManyFieldInfo) this.ef.getFieldInfo(field);
			joinField = (info.getJoinTableName().equals(joinTableName)) ? field : joinField;
		}
		if (joinField == null) {
			EntityException.Error.MissingJoinTableField.build(joinTableName, this.obj.getClass().getSimpleName());
		}

		return this.m2m_JoinTableMap.get(joinField).get(remote);

	}

	public void setRow(Row row) {
		this.row = row;
	}

	public Object getPk() {
		return this.pk;
	}

	public Many2OneFieldInfo getM2OFieldInfo(Field field) {
		return (Many2OneFieldInfo) this.ef.getFieldInfo(field);
	}

	public One2OneFieldInfo getO2OFieldInfo(Field field) {
		return (One2OneFieldInfo) this.ef.getFieldInfo(field);
	}

	public Object getRemoteObjectO2O(Field field) {
		return this.getRemoteObjectX2O(field);
	}

	public Object getStoredRemoteObjectO2O(Field field, String columnName, Transaction transaction) throws ObjectCasketException {
		if (field != null) {
			return this.o2o_Map.get(field);
		}
		try {
			FieldInfo fkInfo = this.ef.getAnonymousFkFieldInfo(columnName);
			Object fk = this.row.read(transaction, columnName, ((One2OneFieldInfo) fkInfo).getMyPkType(), null);
			if (fk == null) {
				return null;
			}
			EntityFactory fkFactory = this.session.getEntityFactory(fkInfo.getEntityClass());
			return fkFactory.getObjByPk(fk, transaction);
		} catch (TableModuleException exc) {
			ObjectCasketException.build(exc);
		}
		return null;
	}

	public One2ManyFieldInfo getO2MFieldInfo(Field field) {
		return (One2ManyFieldInfo) this.ef.getFieldInfo(field);
	}

	public Set<Object> getRemoteObjectsO2M(Field field) {
		return this.getRemoteObjectsX2M(field);
	}

	public Object getRemoteObjectM2O(Field field) {
		return this.getRemoteObjectX2O(field);
	}

	public Object getStoredRemoteObjectM2O(Field fkField, String columnName, Transaction transaction) throws ObjectCasketException {
		if (fkField != null) {
			return this.m2o_Map.get(fkField);
		}
		try {
			FieldInfo fkInfo = this.ef.getAnonymousFkFieldInfo(columnName);
			Object fk = null;
			Class<?> fkClass;
			if (fkInfo.is(FieldInfo.Kind.MANY2MANY)) {
				fk = this.row.read(transaction, columnName, ((Many2ManyFieldInfo) fkInfo).getPkType(columnName), null);
				fkClass = (fk != null) ? ((Many2ManyFieldInfo) fkInfo).getEntityClass(columnName) : null;
			} else {
				fk = this.row.read(transaction, columnName, ((One2ManyFieldInfo) fkInfo).getMyPkType(), null);
				fkClass = (fk != null) ? ((One2ManyFieldInfo) fkInfo).getEntityClass() : null;
			}
			if (fk == null) {
				return null;
			}
			EntityFactory fkFactory = this.session.getEntityFactory(fkClass);
			return fkFactory.getObjByPk(fk, transaction);
		} catch (TableModuleException exc) {
			ObjectCasketException.build(exc);
		}
		return null;
	}

	public Set<Object> getStoredRemoteObjectsO2M(Field field) {
		Set<Object> storedRemoteObjects = this.o2m_Map.get(field);
		if (storedRemoteObjects == null) {
			this.o2m_Map.put(field, storedRemoteObjects = new HashSet<>());
		}
		return storedRemoteObjects;
	}

	public Many2ManyFieldInfo getM2MFieldInfo(Field field) {
		return (Many2ManyFieldInfo) this.ef.getFieldInfo(field);
	}

	public Set<Object> getRemoteObjectsM2M(Field field) {
		return this.getRemoteObjectsX2M(field);
	}

	public Set<Object> getStoredRemoteObjectsM2M(Field field) {
		Set<Object> storedRemoteObjects = this.m2m_Map.get(field);
		if (storedRemoteObjects == null) {
			this.m2m_Map.put(field, storedRemoteObjects = new HashSet<>());
		}
		return storedRemoteObjects;
	}

	public Map<Object, Object> getJoinTableM2M(Field field) {
		Map<Object, Object> joinTable = this.m2m_JoinTableMap.get(field);
		if (joinTable == null) {
			this.m2m_JoinTableMap.put(field, joinTable = new HashMap<>());
		}
		return joinTable;
	}

	public void clear_o2o_Field(Field field, String columnName, Transaction transaction) throws ObjectCasketException {
		try {
			this.row.write(transaction, columnName, null);
			if (field != null) {
				this.o2o_Map.remove(field);
				field.set(this.obj, null);
			}
		} catch (TableModuleException | IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
	}

	public void set_o2o_Field(Entity entity, Field field, String columnName, Transaction transaction) throws ObjectCasketException {
		try {
			this.row.write(transaction, columnName, (entity == null) ? null : entity.pk);
			if (field != null) {
				if (entity != null) {
					this.o2o_Map.put(field, entity.obj);
				} else {
					this.o2o_Map.remove(field);
				}
				field.set(this.obj, (entity == null) ? null : entity.obj);
			}
		} catch (TableModuleException | IllegalArgumentException |

				IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
	}

	public void clear_m2o_Field(Field field, String columnName, Transaction transaction) throws ObjectCasketException {
		try {
			this.row.write(transaction, columnName, null);
			if (field != null) {
				this.m2o_Map.remove(field);
				field.set(this.obj, null);
			}
		} catch (TableModuleException | IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
	}

	public void set_m2o_Field(Entity entity, Field field, String columnName, Transaction transaction) throws ObjectCasketException {
		try {
			this.row.write(transaction, columnName, entity.pk);
			if (field != null) {
				this.m2o_Map.put(field, entity.obj);
				field.set(this.obj, entity.obj);
			}
		} catch (TableModuleException | IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
	}

	public Object getMe() {
		return this.obj;
	}

	@SuppressWarnings("unchecked")
	private Set<Object> getRemoteObjectsX2M(Field field) {
		try {
			field.setAccessible(true);
			return (Set<Object>) field.get(this.obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object getRemoteObjectX2O(Field field) {
		try {
			field.setAccessible(true);
			return field.get(this.obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class EntityException extends ObjectCasketException {

		private static final long serialVersionUID = 1L;

		private EntityException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			MissingJoinTableField("Missing field with @JoinTable anotation %s in %s.\n"); //

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ObjectCasketException {
				ObjectCasketException.build(new EntityException(this, arg));
			}

		}

	}
}
