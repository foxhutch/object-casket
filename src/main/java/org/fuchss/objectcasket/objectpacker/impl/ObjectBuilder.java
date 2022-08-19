package org.fuchss.objectcasket.objectpacker.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError.CE1;
import org.fuchss.objectcasket.common.CasketError.CE3;
import org.fuchss.objectcasket.common.CasketError.CE4;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.port.Session.Exp;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableObserver;

@SuppressWarnings("java:S3011")
class ObjectBuilder<T> extends ObjectBuilderCore<T> implements TableObserver {

	private static final Class<? extends Serializable> REF_COUNTER_TYPE = Integer.TYPE;
	private static final Class<? extends Serializable> SUPPLIER_COUNTER_TYPE = Integer.TYPE;

	private Table objectTable;

	Map<T, Row> objectRowMap = new HashMap<>();
	private final Map<Row, T> rowObjectMap = new HashMap<>();
	private final Map<Serializable, T> pkToObjectMap = new HashMap<>();

	synchronized T getObjectByPk(Serializable pk) {
		return this.pkToObjectMap.get(pk);

	}

	ObjectBuilder(SessionImpl session, TableModule tabMod, ClassInfo<T> info) throws CasketException {
		super(session, tabMod, info);
		this.createTableOrView();
	}

	private void createTableOrView() throws CasketException {
		Map<String, Class<? extends Serializable>> signature = new HashMap<>();
		signature.put(REF_COUNTER, REF_COUNTER_TYPE);
		signature.put(SUPPLIER_COUNTER, SUPPLIER_COUNTER_TYPE);
		for (Field field : this.valueFields) {
			signature.put(this.fieldColumnMap.get(field), this.fieldTypeMap.get(field));
		}
		for (Field field : this.many2OneFields) {
			signature.put(this.fieldColumnMap.get(field), this.fieldTypeMap.get(field));
		}

		if (this.tabMod.tableExists(this.tableName))
			this.objectTable = this.tabMod.mkView(this.tableName, this.classInfo.getColumnName(), signature, this.classInfo.isGenerated());
		else
			this.objectTable = this.tabMod.createTable(this.tableName, this.classInfo.getColumnName(), signature, this.classInfo.isGenerated());
		this.objectTable.register(this);
	}

	synchronized Set<T> getAllObjects(Object transaction) throws CasketException {
		try {
			List<Row> rows = this.objectTable.allRows(transaction);
			return this.loadObjectsByRow(rows, transaction);
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	synchronized Set<T> getObjects(Set<Exp> args, Object transaction) throws CasketException {
		try {
			List<Row> rows = this.objectTable.searchRows(this.mkFilter(args), transaction);
			return this.loadObjectsByRow(rows, transaction);
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	private Set<T> loadObjectsByRow(List<Row> rows, Object transaction) throws CasketException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Set<T> objects = new HashSet<>();
		List<Row> newRows = new ArrayList<>();
		for (Row row : rows) {
			T obj = this.rowObjectMap.get(row);
			if (obj == null) {
				newRows.add(row);
				obj = this.defaultConstructor.newInstance();
				this.fillValueFields(obj, row);
				this.objectRowMap.put(obj, row);
				this.rowObjectMap.put(row, obj);
				this.pkToObjectMap.put(this.classInfo.getPK(obj), obj);
			}
			objects.add(obj);
		}
		for (Row row : newRows) {
			final T obj = this.rowObjectMap.get(row);
			synchronized (obj) {
				this.fillMany2OneFields(obj, row);
			}
		}
		for (Row row : newRows) {
			final T obj = this.rowObjectMap.get(row);
			synchronized (obj) {
				this.fillMany2ManyFields(obj, transaction);
			}
		}

		return objects;
	}

	private Set<Table.Exp> mkFilter(Set<Exp> args) throws CasketException, IllegalArgumentException {
		Map<String, List<Exp>> attrExpSetMap = new HashMap<>();
		for (Exp exp : args) {
			if ((exp.fieldName() == null) || (exp.op() == null) || exp.fieldName().isBlank() || exp.op().isBlank())
				throw CE1.EMPTY_EXPRESSION.defaultBuild(exp);
			String attr = exp.fieldName().trim();
			List<Exp> attrSet = attrExpSetMap.computeIfAbsent(attr, k -> new ArrayList<>());
			attrSet.add(exp);
		}
		Set<Table.Exp> filter = new HashSet<>();
		for (Field field : this.valueFields) {
			List<Exp> expList = attrExpSetMap.get(field.getName());
			if (expList == null)
				continue;
			String column = this.fieldColumnMap.get(field);
			for (Exp exp : expList)
				filter.add(new Table.Exp(column, str2CMP(exp.op().trim()), exp.value()));
		}
		if (filter.size() != args.size()) {
			throw CE3.MISSING_OPERATOR.defaultBuild(args, this.valueFields, this.classInfo.myClass);
		}
		return filter;
	}

	@SuppressWarnings("java:S1121")
	synchronized void persist(T obj, Object transaction) throws CasketException {
		Row row = this.objectRowMap.get(obj);
		Map<String, Serializable> newValues = new HashMap<>();
		try {
			for (Field field : this.valueFields) {
				if (this.classInfo.notPkField(field))
					this.writeValueField(newValues, obj, field, row);
			}
			if (row == null) {
				newValues.put(SUPPLIER_COUNTER, 0);
				newValues.put(REF_COUNTER, 0);
				Serializable pk = this.classInfo.getPK(obj);
				if (pk != null)
					newValues.put(this.classInfo.getColumnName(), pk);
				this.objectRowMap.put(obj, row = this.objectTable.createRow(newValues, transaction));
				this.rowObjectMap.put(row, obj);
				this.fillEmptyValueFields(obj, row);
				this.pkToObjectMap.put(this.classInfo.getPK(obj), obj);
				newValues.clear();
			}
			this.writeFields(obj, row, newValues, transaction);
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	private void writeFields(T obj, Row row, Map<String, Serializable> newValues, Object transaction) throws CasketException {
		for (Field field : this.many2OneFields) {
			this.writeMany2OneField(newValues, obj, field, row);
		}
		for (Field field : this.many2ManyFields) {
			this.writeMany2ManyField(newValues, obj, field, row, transaction);
		}
		if (!newValues.isEmpty())
			this.objectTable.updateRow(row, newValues, transaction);

	}

	@SuppressWarnings("java:S2445")
	synchronized void resync(T obj, Object transaction) throws CasketException {
		Row row = this.getRowIfExists(obj);
		try {
			this.objectTable.reloadRow(row, transaction);
			synchronized (obj) {
				this.fillValueFields(obj, row);
				this.fillMany2OneFields(obj, row);
				this.fillMany2ManyFields(obj, transaction);
			}

		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	synchronized void resync(Object transaction) throws CasketException {
		try {
			for (Entry<T, Row> entry : this.objectRowMap.entrySet()) {
				T obj = entry.getKey();
				Row row = entry.getValue();
				this.objectTable.reloadRow(row, transaction);
				synchronized (obj) {
					this.fillValueFields(obj, row);
					this.fillMany2OneFields(obj, row);
					this.fillMany2ManyFields(obj, transaction);
				}
			}
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	synchronized void deleteByUpdate(T obj) throws CasketException {
		Row row = this.getRowIfExists(obj);
		if (row == null)
			return;
		try {
			this.objectRowMap.remove(obj);
			this.rowObjectMap.remove(row);
			this.pkToObjectMap.remove(this.classInfo.getPK(obj));
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	synchronized void delete(T obj, Object transaction) throws CasketException {
		Row row = this.getRowIfExists(obj);
		try {
			this.objectTable.deleteRow(row, transaction);
			this.objectRowMap.remove(obj);
			this.rowObjectMap.remove(row);
			this.pkToObjectMap.remove(this.classInfo.getPK(obj));
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	private void fillValueFields(T obj, Row row) throws IllegalArgumentException, IllegalAccessException, CasketException {
		for (Field field : this.valueFields) {
			field.set(obj, row.getValue(this.fieldColumnMap.get(field), this.fieldTypeMap.get(field)));
		}
	}

	private void fillEmptyValueFields(T obj, Row row) throws IllegalArgumentException, IllegalAccessException, CasketException {
		for (Field field : this.valueFields) {
			if (field.get(obj) == null)
				field.set(obj, row.getValue(this.fieldColumnMap.get(field), this.fieldTypeMap.get(field)));
		}
	}

	private void fillMany2OneFields(T obj, Row row) throws IllegalArgumentException, IllegalAccessException, CasketException {
		for (Field field : this.many2OneFields) {
			ClassInfo<?> classInfo = this.session.classInfoMap.getIfExists(field.getType());
			Serializable fk = row.getValue(this.fieldColumnMap.get(field), classInfo.getType());
			if (fk == null) {
				field.set(obj, null);
				continue;
			}
			ObjectBuilder<?> objFac = this.session.objectFactoryMap.getIfExists(field.getType());
			Object supplier = objFac.pkToObjectMap.get(fk);
			if (supplier == null) {
				Set<Exp> pkArgs = new HashSet<>();
				pkArgs.add(new Exp(classInfo.getFieldName(), "==", fk));
				supplier = this.session.getObjects(field.getType(), pkArgs).iterator().next();
			}
			assert (supplier != null);
			field.set(obj, supplier);
		}
	}

	@SuppressWarnings("unchecked")
	private <S> void fillMany2ManyFields(T obj, Object transaction) throws IllegalArgumentException, IllegalAccessException, CasketException {
		for (Field field : this.many2ManyFields) {
			M2MInfo<T, S> m2mInfo = (M2MInfo<T, S>) this.m2mFieldInfoMap.get(field);
			JoinTableBuilder<T, S> jtabBuilder = (JoinTableBuilder<T, S>) this.session.joinTabFactoryFactoryMap.getIfExists(m2mInfo);
			Set<S> supplierSet = (Set<S>) field.get(obj);
			supplierSet.clear();
			supplierSet.addAll(jtabBuilder.allSuppliers(obj, transaction));
		}
	}

	private void writeValueField(Map<String, Serializable> newValues, Object obj, Field field, Row row) throws CasketException {
		String column = this.fieldColumnMap.get(field);
		try {
			Serializable val = (Serializable) field.get(obj);
			if ((row == null) || !Objects.equals(val, row.getValue(column, this.fieldTypeMap.get(field))))
				newValues.put(column, val);
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	private void writeMany2OneField(Map<String, Serializable> newValues, Object obj, Field field, Row row) throws CasketException {
		assert (row != null);
		String column = this.fieldColumnMap.get(field);
		ClassInfo<?> classInfo = this.session.classInfoMap.getIfExists(field.getType());
		try {
			Object val = field.get(obj);
			Serializable fk = null;
			if (val != null) {
				if (!this.session.isManaged(val))
					this.session.persist(val);
				fk = classInfo.getPK(val);
			}
			Serializable oldFk = row.getValue(column, classInfo.getType());
			if (!Objects.equals(fk, oldFk)) {
				newValues.put(column, fk);
				this.session.removeClient(field.getType(), oldFk);
				if (val != null)
					this.session.addClient(val);
				int supCount = newValues.containsKey(SUPPLIER_COUNTER) ? (int) newValues.get(SUPPLIER_COUNTER) : row.getValue(SUPPLIER_COUNTER, Integer.TYPE);
				if (oldFk == null)
					newValues.put(SUPPLIER_COUNTER, supCount + 1);
				if (val == null) {
					newValues.put(SUPPLIER_COUNTER, supCount - 1);
				}
			}
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	@SuppressWarnings("unchecked")
	private <S> void writeMany2ManyField(Map<String, Serializable> newValues, T obj, Field field, Row row, Object transaction) throws CasketException {
		assert (row != null);

		M2MInfo<T, S> m2mInfo = (M2MInfo<T, S>) this.m2mFieldInfoMap.get(field);
		JoinTableBuilder<T, S> joinTabBuilder = (JoinTableBuilder<T, S>) this.session.joinTabFactoryFactoryMap.getIfExists(m2mInfo);
		try {
			Set<S> suppliers = (Set<S>) field.get(obj);
			for (S val : suppliers) {
				if (!this.session.isManaged(val))
					this.session.persist(val);
			}
			int delta = joinTabBuilder.write(obj, suppliers, transaction);
			if (delta != 0) {
				int supCount = newValues.containsKey(SUPPLIER_COUNTER) ? (int) newValues.get(SUPPLIER_COUNTER) : row.getValue(SUPPLIER_COUNTER, Integer.TYPE);
				newValues.put(SUPPLIER_COUNTER, supCount + delta);
			}

		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	/*
	 * used by session to manage m2o
	 */
	synchronized void addClient(T supplier, Object transaction) throws CasketException {
		Row row = this.getRowIfExists(supplier);

		int currentClients = row.getValue(REF_COUNTER, Integer.TYPE) + 1;
		Map<String, Serializable> newValue = new HashMap<>();
		newValue.put(REF_COUNTER, currentClients);
		this.objectTable.updateRow(row, newValue, transaction);

	}

	synchronized boolean hasClients(T supplier) throws CasketException {
		Row row = this.getRowIfExists(supplier);
		return row.getValue(REF_COUNTER, Integer.TYPE) > 0;

	}

	synchronized boolean isClient(T client) throws CasketException {
		Row row = this.getRowIfExists(client);

		return row.getValue(SUPPLIER_COUNTER, Integer.TYPE) > 0;

	}

	private Row getRowIfExists(T obj) throws CasketException {
		if (!this.objectRowMap.containsKey(obj))
			throw CE4.UNKNOWN_MANAGED_OBJECT.defaultBuild("Object", obj, this.getClass(), this);
		return this.objectRowMap.get(obj);
	}

	synchronized void removeClient(Serializable pk, Object transaction) throws CasketException {
		T supplier = this.pkToObjectMap.get(pk);

		if (supplier == null) {
			Set<Exp> pkArgs = new HashSet<>();
			pkArgs.add(new Exp(this.classInfo.getFieldName(), "==", pk));
			supplier = this.getObjects(pkArgs, transaction).iterator().next();
		}
		Row row = this.objectRowMap.get(supplier);
		int currentClients = row.getValue(REF_COUNTER, Integer.TYPE);
		currentClients = (currentClients > 1) ? (currentClients - 1) : 0;
		Map<String, Serializable> newValue = new HashMap<>();
		newValue.put(REF_COUNTER, currentClients);
		this.objectTable.updateRow(row, newValue, transaction);

	}

	synchronized void changedObjects(Set<T> changed) {
		for (T obj : changed) {
			if (this.objectRowMap.containsKey(obj))
				this.session.addToChanged(obj);
		}
	}

	@Override
	public synchronized void update(Set<Row> changed, Set<Row> deleted, Set<Row> added) {

		for (Row row : changed) {
			T object = this.rowObjectMap.get(row);
			if (object != null)
				this.session.addToChanged(object);
		}
		for (Row row : deleted) {
			T object = this.rowObjectMap.get(row);
			if (object != null)
				this.session.addToDeleted(object);
		}

		this.session.updateDone();

	}

	private static Table.TabCMP str2CMP(String sCmp) throws CasketException {
		return switch (sCmp) {
		case "<" -> Table.TabCMP.LESS;
		case ">" -> Table.TabCMP.GREATER;
		case "==", "=" -> Table.TabCMP.EQUAL;
		case "<=", "=<" -> Table.TabCMP.LESSEQ;
		case ">=", "=>" -> Table.TabCMP.GREATEREQ;
		case "!=", "<>" -> Table.TabCMP.UNEQUAL;
		default -> throw CE1.UNKNOWN_OPERATOR.defaultBuild(sCmp);
		};
	}

}
