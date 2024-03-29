package org.fuchss.objectcasket.tablemodule.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.fuchss.objectcasket.common.CasketError.CE2;
import org.fuchss.objectcasket.common.CasketError.CE4;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectMaps;
import org.fuchss.objectcasket.tablemodule.port.Row;

class RowImpl implements Row {

	private static final Map<Class<?>, Object> NULL_MAP = new HashMap<>();
	static {
		RowImpl.NULL_MAP.put(Character.TYPE, (char) 0);
		RowImpl.NULL_MAP.put(Double.TYPE, 0.0);
		RowImpl.NULL_MAP.put(Float.TYPE, (float) 0.0);
		RowImpl.NULL_MAP.put(Long.TYPE, (long) 0);
		RowImpl.NULL_MAP.put(Integer.TYPE, 0);
		RowImpl.NULL_MAP.put(Short.TYPE, (short) 0);
		RowImpl.NULL_MAP.put(Byte.TYPE, (byte) 0);
		RowImpl.NULL_MAP.put(Boolean.TYPE, false);
	}

	private final TableImpl tab;
	protected Map<String, Object> valueMap = new HashMap<>();
	protected Map<String, Object> oldValueMap = new HashMap<>();

	private boolean isDirty;

	public <T extends Serializable> RowImpl(TableImpl table, Map<String, ? extends Serializable> javaValues, T pk) {
		this.tab = table;
		for (String col : this.tab.allColumns())
			this.valueMap.put(col, javaValues.get(col));
		this.valueMap.put(this.tab.pkName(), pk);
		this.isDirty = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T extends Serializable> T getValue(String column, Class<T> clazz) throws CasketException {
		Util.objectsNotNull(column, clazz);
		if (this.tab.getColumnType(column).isAssignableFrom(clazz)) {
			T val = (T) this.valueMap.get(column);
			if ((val == null) && clazz.isPrimitive())
				return (T) (NULL_MAP.get(clazz));
			return (val);
		}
		throw CE4.INCOMPATIBLE_TYPES.defaultBuild(clazz, this.tab.getColumnType(column), column, this.tab.getTableName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized <T extends Serializable> T getPk(Class<T> clazz) throws CasketException {
		Objects.requireNonNull(clazz);
		if (clazz.isAssignableFrom(this.tab.getColumnType(this.tab.pkName()))) {
			T val = (T) this.valueMap.get(this.tab.pkName());
			if ((val == null) && clazz.isPrimitive())
				return (T) (NULL_MAP.get(clazz));
			return val;
		}
		throw CE4.INCOMPATIBLE_TYPES.defaultBuild(clazz, this.tab.getColumnType(this.tab.pkName()), this.tab.pkName(), this.tab.getTableName());
	}

	protected synchronized void resetPK() { // roll back if key is auto incremented.
		this.valueMap.remove(this.tab.pkName());
	}

	protected synchronized void delete() {
		this.tab.allColumns().forEach(col -> this.switchValue(col, null));
	}

	protected synchronized void setValue(String column, Object val) throws CasketException {
		Objects.requireNonNull(column);
		Class<? extends Serializable> colType = this.checkColumn(column);
		if ((val == null) || SqlObjectMaps.respectBoxing(colType).isAssignableFrom(val.getClass()))
			this.switchValue(column, val);
		else
			throw CE4.INCOMPATIBLE_TYPES.defaultBuild(val.getClass(), colType, column, this.tab.getTableName());
	}

	protected synchronized void done() {
		this.isDirty = false;
		this.oldValueMap = new HashMap<>();
	}

	private void switchValue(String column, Object val) {
		Object oldVal = this.valueMap.put(column, val);
		if ((oldVal == val) || this.oldValueMap.containsKey(column))
			return;
		this.oldValueMap.put(column, oldVal);
		this.isDirty = true;
	}

	private Class<? extends Serializable> checkColumn(String column) throws CasketException {
		Objects.requireNonNull(column);
		if (this.tab.pkName().equals(column))
			throw CE2.DONT_CHANGE_PK.defaultBuild(column, this.tab.getTableName());
		Class<? extends Serializable> colType = this.tab.getColumnType(column);
		if (colType == null)
			throw CE2.WRONG_COLUMN_NAME.defaultBuild(column, this.tab.getTableName());
		return colType;
	}

	@Override
	public boolean isDirty() {
		return this.isDirty;
	}

	protected synchronized void hasChanged() {
		this.isDirty = true;
	}

}
