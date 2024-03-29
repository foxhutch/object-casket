package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError.CE3;
import org.fuchss.objectcasket.common.CasketError.CE4;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlArgImpl;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlColumnSignatureImpl;
import org.fuchss.objectcasket.sqlconnector.impl.objects.SqlObj;
import org.fuchss.objectcasket.sqlconnector.impl.prepstat.PreCompiledDelete;
import org.fuchss.objectcasket.sqlconnector.impl.prepstat.PreCompiledSelect;
import org.fuchss.objectcasket.sqlconnector.impl.prepstat.PreCompiledUpdate;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;

class SqlDatabaseImpl extends SimpleDatabase {

	protected SqlDatabaseImpl(Connection connection, boolean canCreate, SqlObjectFactoryImpl objectFactory, SqlCmd sqlCmd) {
		super(connection, canCreate, objectFactory, sqlCmd);
	}

	@Override
	public SqlArg mkSqlArg(TableAssignment tabAssignment, String columnName, CMP cmp) throws CasketException {
		Util.objectsNotNull(tabAssignment, columnName, cmp);
		TableAssignmentImpl assignImpl = this.getTableAssignment(tabAssignment);
		SqlColumnSignatureImpl proto = assignImpl.getColSigMap().get(columnName);
		if (proto == null)
			throw CE3.UNKNOWN_COLUMN.defaultBuild(proto, columnName, assignImpl.tableName());
		return new SqlArgImpl(tabAssignment.tableName(), columnName, proto, cmp);
	}

	@Override
	public List<Map<String, SqlObject>> select(PreCompiledStatement preStat, Map<SqlArg, SqlObject> relatedObjs, Object obj) throws CasketException {
		Util.objectsNotNull(preStat, relatedObjs);
		PreCompiledSelect preStatImpl = (PreCompiledSelect) preStat;
		List<Map<String, SqlObject>> result = new ArrayList<>();

		if (obj != null)
			this.checkVoucherAndAcquire(obj);

		try (ResultSet resultSet = preStatImpl.setValuesAndExecute(relatedObjs)) {
			while (resultSet.next())
				result.add(this.readRow(preStatImpl.sqlColumnTypes(), resultSet));
			return result;
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			if (obj != null)
				this.proceedTransaction();
		}
	}

	private Map<String, SqlObject> readRow(Map<String, StorageClass> sqlColumnTypes, ResultSet resultSet) throws SQLException, CasketException {
		Map<String, SqlObject> row = new HashMap<>();
		for (Entry<String, StorageClass> entry : sqlColumnTypes.entrySet()) {
			Object obj = resultSet.getObject(entry.getKey());
			SqlObject sqlObj = this.objectFactory.mkSqlObjectFromSQL(entry.getValue(), obj);
			row.put(entry.getKey(), sqlObj);
		}

		return row;

	}

	@Override
	@SuppressWarnings("java:S2095")
	public PreCompiledStatement mkSelectStmt(TableAssignment tabAssignment, Set<SqlArg> args, SqlArg.OP op) throws CasketException {
		Util.objectsNotNull(tabAssignment, args, op);

		TableAssignmentImpl assignImpl = this.getTableAssignment(tabAssignment);
		this.checkArgs(assignImpl, args);
		String tableName = assignImpl.tableName();
		try {
			List<SqlArg> argList = new ArrayList<>(args);
			PreparedStatement prepStmt = this.connection.prepareStatement(this.sqlCmd.select(tableName, assignImpl.getColSigMap().keySet(), argList, op));
			return new PreCompiledSelect(prepStmt, tableName, argList, assignImpl.getColSigMap());
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	@Override
	public List<SqlObject> delete(PreCompiledStatement preStat, Map<SqlArg, SqlObject> relatedObjs, Object obj) throws CasketException {
		Util.objectsNotNull(preStat, relatedObjs);
		PreCompiledDelete preStatImpl = (PreCompiledDelete) preStat;
		List<SqlObject> result = new ArrayList<>();

		this.checkVoucherAndAcquire(obj);

		try (ResultSet resultSet = preStatImpl.setValuesAndExecute(relatedObjs)) {
			String tableName = preStatImpl.tableName();
			String pkName = preStatImpl.pkName();
			while (resultSet.next()) {
				SqlObj pkObj = this.readPK(pkName, preStatImpl.sqlColumnTypes(), resultSet);
				this.transaction.add2deleted(tableName, pkObj);
				result.add(pkObj);
			}
			return result;
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		} finally {
			this.proceedTransaction();
		}
	}

	private SqlObj readPK(String pkName, Map<String, StorageClass> sqlColumnTypes, ResultSet resultSet) throws SQLException, CasketException {
		StorageClass type = sqlColumnTypes.get(pkName);
		Object obj = resultSet.getObject(pkName);
		return this.objectFactory.mkSqlObjectFromSQL(type, obj);
	}

	@Override
	@SuppressWarnings("java:S2095")
	public PreCompiledStatement mkDeleteStmt(TableAssignment tabAssignment, Set<SqlArg> args, SqlArg.OP op) throws CasketException {
		Util.objectsNotNull(tabAssignment, args, op);

		TableAssignmentImpl assignImpl = this.getTableAssignment(tabAssignment);
		this.checkArgs(assignImpl, args);
		String tableName = assignImpl.tableName();
		try {
			List<SqlArg> argList = new ArrayList<>(args);
			Set<String> pkName = new HashSet<>();
			pkName.add(tabAssignment.pkName());
			PreparedStatement prepStmt = this.connection.prepareStatement(this.sqlCmd.delete(tableName, argList, op)); // closed inside the
			PreparedStatement prepSelectStmt = this.connection.prepareStatement(this.sqlCmd.select(tableName, pkName, argList, op)); // PreCompiledDelete object
			return new PreCompiledDelete(prepStmt, prepSelectStmt, tableName, argList, assignImpl.getColSigMap());
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	@Override
	public void updateRow(PreCompiledStatement preStat, SqlObject pk, Map<String, SqlObject> values, Object obj) throws CasketException {
		Util.objectsNotNull(preStat, pk, values);
		PreCompiledUpdate preStatImpl = (PreCompiledUpdate) preStat;
		this.checkVoucherAndAcquire(obj);
		Map<String, SqlObject> tmpArgs = Util.copyAndIgnore(values, preStatImpl.pkName());
		try {
			preStatImpl.setValuesAndExecute(tmpArgs, pk);
			this.transaction.add2changed(preStatImpl.tableName(), (SqlObj) pk);
		} finally {
			this.proceedTransaction();
		}
	}

	@Override
	public PreCompiledStatement mkUpdateRowStmt(TableAssignment tabAssignment, Set<String> columns) throws CasketException {
		Util.objectsNotNull(tabAssignment);
		TableAssignmentImpl tabAssignImpl = this.getTableAssignment(tabAssignment);
		String tableName = tabAssignImpl.tableName();
		String pkName = tabAssignImpl.pkName();

		List<String> columnNames = new ArrayList<>(columns);
		columnNames.remove(pkName);

		if (columnNames.isEmpty() || !tabAssignImpl.getColSigMap().keySet().containsAll(columnNames))
			throw CE3.MISSING_COLUMN.defaultBuild(columnNames, tabAssignment, tabAssignImpl.getColSigMap().keySet());
		try {
			PreparedStatement prepStmt = this.connection.prepareStatement(this.sqlCmd.update(tableName, columnNames, pkName));
			return new PreCompiledUpdate(prepStmt, tableName, pkName, columnNames, tabAssignImpl.getColSigMap());
		} catch (SQLException exc) {
			throw CasketException.build(exc);
		}
	}

	private void checkArgs(TableAssignmentImpl assignImpl, Set<SqlArg> args) throws CasketException {
		for (SqlArg arg : args) {
			SqlColumnSignatureImpl proto = ((SqlArgImpl) arg).proto();
			if (!assignImpl.getColSigMap().get(arg.columnName()).equals(proto))
				throw CE4.INVALID_ARGUMENTS.defaultBuild(assignImpl.getColSigMap().get(arg.columnName()), proto, arg.columnName(), assignImpl);
		}
	}

	@Override
	protected SqlDatabase getSqlDatabase() {
		return this;
	}

	private TableAssignmentImpl getTableAssignment(TableAssignment tabAssignment) throws CasketException {
		TableAssignmentImpl assignImpl = this.assignedTables.get(tabAssignment);
		if (assignImpl == null)
			throw CE4.UNKNOWN_MANAGED_OBJECT.defaultBuild("Table assignment", tabAssignment, this.getClass(), this);
		return assignImpl;

	}

}
