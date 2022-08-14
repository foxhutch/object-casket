package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlObj;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;

class TransactionImpl {

	// Table -> PK.Value -> SqlObj
	protected Map<String, Map<Object, SqlObj>> changed = new HashMap<>();
	protected Map<String, Map<Object, SqlObj>> deleted = new HashMap<>();
	protected Map<String, Map<Object, SqlObj>> created = new HashMap<>();

	protected Set<SqlObject> getDeletedPKs(TableAssignmentImpl tabAssignment) {
		return this.getFrom(tabAssignment, this.deleted);
	}

	protected Set<SqlObject> getChangedPKs(TableAssignmentImpl tabAssignment) {
		return this.getFrom(tabAssignment, this.changed);
	}

	protected Set<SqlObject> getCreatedPKs(TableAssignmentImpl tabAssignment) {
		return this.getFrom(tabAssignment, this.created);
	}

	private Set<SqlObject> getFrom(TableAssignmentImpl tabAssignment, Map<String, Map<Object, SqlObj>> map) {
		Set<SqlObject> set = new HashSet<>();
		Map<Object, SqlObj> pkObjects = map.get(tabAssignment.tableName());
		if (pkObjects == null)
			return set;
		set.addAll(pkObjects.values());
		return set;
	}

	protected void add2deleted(String tabName, SqlObj sqlObj) {
		Object key = sqlObj.getVal();
		Map<Object, SqlObj> createdKeys = this.created.get(tabName);
		Map<Object, SqlObj> changedKeys = this.changed.get(tabName);
		if (createdKeys != null)
			createdKeys.remove(key);
		if (changedKeys != null)
			changedKeys.remove(key);
		this.add2Map(this.deleted, tabName, sqlObj);
	}

	protected void add2changed(String tabName, SqlObj sqlObj) {
		Object key = sqlObj.getVal();
		Map<Object, SqlObj> createdKeys = this.created.get(tabName);
		if ((createdKeys != null) && createdKeys.containsKey(key))
			return;
		this.add2Map(this.changed, tabName, sqlObj);
	}

	protected void add2Created(String tabName, SqlObj sqlObj) {
		this.add2Map(this.created, tabName, sqlObj);
	}

	private void add2Map(Map<String, Map<Object, SqlObj>> map, String tabName, SqlObj sqlObj) {
		Map<Object, SqlObj> keySet = map.computeIfAbsent(tabName, k -> new HashMap<>());
		keySet.put(sqlObj.getVal(), sqlObj);
	}

}
