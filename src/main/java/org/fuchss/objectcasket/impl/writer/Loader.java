package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.impl.field.FieldInfo;
import org.fuchss.objectcasket.impl.field.Many2ManyFieldInfo;
import org.fuchss.objectcasket.impl.field.Many2OneFieldInfo;
import org.fuchss.objectcasket.impl.field.One2ManyFieldInfo;
import org.fuchss.objectcasket.impl.field.One2OneFieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Row;
import org.fuchss.tablemodule.port.TableModuleException;
import org.fuchss.tablemodule.port.Transaction;

public class Loader implements Writer {
	private Object object;
	private Row row;
	private Transaction transaction;

	public Loader(Object object, Row row, Transaction transaction) {
		this.object = object;
		this.row = row;
		this.transaction = transaction;

	}

	public void loadValueField(Field field, FieldInfo info) throws ObjectCasketException {
		try {
			Object value = this.row.read(this.transaction, info.getColumnName(), info.getColumnType());
			field.set(this.object, value);
		} catch (TableModuleException | IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
	}

	public Object load_M2O_Field(Field field, Many2OneFieldInfo info, SessionImpl session) throws ObjectCasketException {
		Object value = null;
		try {
			Object fk = this.row.read(this.transaction, info.getColumnName(), info.getColumnType());
			if (fk != null) {
				EntityFactory foreignFactory = session.getEntityFactory(info.getForeignClass());
				value = foreignFactory.getObjByPk(fk, this.transaction);
			}
			field.set(this.object, value);
		} catch (TableModuleException | IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
		return value;
	}

	public Object load_O2O_Field(Field field, One2OneFieldInfo info, SessionImpl session) throws ObjectCasketException {
		Object value = null;
		try {
			Object fk = this.row.read(this.transaction, info.getColumnName(), info.getColumnType());
			if (fk != null) {
				EntityFactory foreignFactory = session.getEntityFactory(info.getForeignClass());
				value = foreignFactory.getObjByPk(fk, this.transaction);
			}
			field.set(this.object, value);
		} catch (TableModuleException | IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public Set<Object> load_O2M_Field(Object pk, Field field, One2ManyFieldInfo info, SessionImpl session) throws ObjectCasketException {
		Set<Object> remoteObjects = new HashSet<>();
		try {
			EntityFactory foreignFactory = session.getEntityFactory(info.getForeignClass());
			remoteObjects = foreignFactory.getObjsByFk(pk, info.getRemoteFkColumnName(), this.transaction);
			Set<Object> targetSet = (Set<Object>) field.get(this.object);
			if (targetSet == null) {
				WriterException.Error.MissingInitializedSet.build(field.getName(), this.object.getClass().getSimpleName());
			}
			targetSet.clear();
			targetSet.addAll(remoteObjects);
		} catch (IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
		return remoteObjects;
	}

	@SuppressWarnings("unchecked")
	public Set<Object> load_M2M_Field(Object pk, Field field, Many2ManyFieldInfo info, SessionImpl session, Map<Field, Map<Object, Object>> joinTableMap) throws ObjectCasketException {
		Set<Object> remoteObjects = new HashSet<>();
		Map<Object, Object> joinTable = new HashMap<>();
		try {
			EntityFactory jointTableFactory = session.getEntityFactory(info.getJoinTableClass());
			Set<Object> joinTableObjects = jointTableFactory.getObjsByFk(pk, info.getOwnFKColumnName(), this.transaction);
			for (Object jtObj : joinTableObjects) {
				Entity jtEntity = jointTableFactory.entity(jtObj, this.transaction);
				Object remoteObject = jtEntity.getStoredRemoteObjectM2O(info.getRemoteFKfield(), info.getRemoteFKColumnName(), this.transaction);
				remoteObjects.add(remoteObject);
				joinTable.put(remoteObject, jtObj);
			}
			Set<Object> targetSet = (Set<Object>) field.get(this.object);
			if (targetSet == null) {
				WriterException.Error.MissingInitializedSet.build(field.getName(), this.object.getClass().getSimpleName());
			}
			targetSet.clear();
			targetSet.addAll(remoteObjects);
		} catch (IllegalArgumentException | IllegalAccessException exc) {
			ObjectCasketException.build(exc);
		}
		joinTableMap.put(field, joinTable);
		return remoteObjects;
	}

}
