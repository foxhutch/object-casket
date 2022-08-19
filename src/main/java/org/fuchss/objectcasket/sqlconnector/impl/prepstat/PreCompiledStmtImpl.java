package org.fuchss.objectcasket.sqlconnector.impl.prepstat;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError.CE1;
import org.fuchss.objectcasket.common.CasketError.CE2;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;

class PreCompiledStmtImpl implements PreCompiledStatement {

	/**
	 * The prepared statement.
	 */
	protected PreparedStatement prepStat;

	/**
	 * The column names.
	 */
	protected List<String> columnNames;

	/**
	 * The column signatures.
	 */
	protected Map<String, SqlColumnSignatureImpl> columns = new HashMap<>();

	/**
	 * The table name.
	 */
	protected String tableName;

	/**
	 * The primary key column.
	 */
	protected String pkColumnName;

	/**
	 * If an exception occurs this attribute is set.
	 */
	protected CasketException exc = null;

	protected PreCompiledStmtImpl(PreparedStatement prepStat, String table, List<String> columnNames, Map<String, SqlColumnSignatureImpl> protoMap) {
		this.prepStat = prepStat;
		this.tableName = table;
		this.columnNames = columnNames;
		this.columnNames.forEach(col -> this.columns.put(col, new SqlColumnSignatureImpl(protoMap.get(col))));
	}

	@Override
	public String pkName() {
		return this.pkColumnName;
	}

	@Override
	public String tableName() {
		return this.tableName;
	}

	@Override
	public boolean pkIsAutoIncremented() {
		return this.columns.get(this.pkColumnName).isAutoIncrementedPrimaryKey();
	}

	@Override
	public void close() throws CasketException {
		try {
			if (this.prepStat.isClosed())
				throw CE2.ALREADY_CLOSED.defaultBuild("pre-compiled statement", this);
			this.prepStat.close();
		} catch (SQLException e) {
			throw CasketException.build(e);
		}
	}

	/**
	 * This operation returns the columns together with their SQL types.
	 *
	 * @return column names and types
	 */
	public Map<String, StorageClass> sqlColumnTypes() {
		Map<String, StorageClass> columnTypes = new HashMap<>();
		this.columns.forEach((col, proto) -> columnTypes.put(col, proto.getType()));
		return columnTypes;
	}

	void setValues(Map<String, SqlObject> values) throws CasketException {
		this.checkColumnsExist(values);
		this.columns.values().forEach(SqlColumnSignatureImpl::clear);
		this.exc = null;
		values.forEach((col, obj) -> this.updatePrototype(this.columns.get(col), obj));
		if (this.exc != null) { // exc maybe set during upDatePrototype
			this.columns.values().forEach(SqlColumnSignatureImpl::clear);
			throw this.exc;
		}
	}

	void updatePrototype(SqlColumnSignatureImpl proto, SqlObject sqlObj) {
		try {
			proto.setValue(sqlObj);
		} catch (CasketException e) {
			this.exc = e;
		}
	}

	String getPkName() throws CasketException {
		for (String columnName : this.columnNames) {
			if (this.columns.get(columnName).isPrimaryKey()) {
				return columnName;
			}
		}
		throw CE1.MISSING_PK.defaultBuild(this.columnNames);
	}

	private void checkColumnsExist(Map<String, SqlObject> values) throws CasketException {
		Set<String> missingColumnNames = this.missingColumnNames(values);
		if (missingColumnNames.isEmpty())
			return;
		throw CE2.MISSING_VALUES.defaultBuild(missingColumnNames, this);
	}

	private Set<String> missingColumnNames(Map<String, SqlObject> values) {
		Set<String> missingColumnNames = new HashSet<>(values.keySet());
		missingColumnNames.removeIf(col -> this.columnNames.contains(col));
		return missingColumnNames;
	}

}
