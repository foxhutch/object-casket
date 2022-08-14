package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;

class TableAssignmentImpl implements TableAssignment {

	private String tableName;
	private String pkName;
	private Map<String, SqlColumnSignatureImpl> colSigMap;

	TableAssignmentImpl(String tableName, Map<String, SqlColumnSignatureImpl> colSigMap) {
		this.tableName = tableName;
		this.colSigMap = colSigMap;
		this.pkName = this.getPkName();

	}

	@Override
	public Set<String> columnNames() {
		return new HashSet<>(this.colSigMap.keySet());
	}

	@Override
	public StorageClass storageClass(String columnName) {
		SqlColumnSignatureImpl cellProt = this.colSigMap.get(columnName);
		return (cellProt == null) ? null : cellProt.getType();
	}

	@Override
	public String tableName() {
		return this.tableName;
	}

	@Override
	public String pkName() {
		return this.pkName;
	}

	Map<String, SqlColumnSignatureImpl> getColSigMap() {
		return this.colSigMap;
	}

	private String getPkName() {
		for (Entry<String, SqlColumnSignatureImpl> entry : this.colSigMap.entrySet()) {
			if (entry.getValue().isPrimaryKey())
				return entry.getKey();
		}
		return null;
	}

}
