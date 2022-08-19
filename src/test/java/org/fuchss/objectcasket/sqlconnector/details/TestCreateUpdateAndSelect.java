package org.fuchss.objectcasket.sqlconnector.details;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError.CE1;
import org.fuchss.objectcasket.common.CasketError.CE2;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCreateUpdateAndSelect {

	@Test
	void mkNewRow() throws IOException, CasketException {
		this.mkNewRow(DB.SQLITE);
		this.mkNewRow(DB.H2);
	}

	private void mkNewRow(DB dialect) throws IOException, CasketException {

		File dbFile = Utility.createFile(this);
		try {
			DBConfiguration config = Utility.createDBConfig(dbFile, dialect);

			SqlDatabase db = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);

			db.createTable(Utility.TABLE_NAME, Utility.createColumns(Utility.PK_NAME, (byte) 0));
			SqlPort.SQL_PORT.sqlDatabaseFactory().closeDatabase(db);

			db = SqlPort.SQL_PORT.sqlDatabaseFactory().openDatabase(config);

			List<SqlObject> pks = this.createRow(db);

			this.updateRow(db, pks);

			this.createArg(db);

			Map<String, SqlColumnSignature> allColumns = Utility.createColumns(Utility.PK_NAME, (byte) 0);
			for (String col : allColumns.keySet()) {
				this.select(db, col);

			}

			for (String col : allColumns.keySet()) {
				this.selectAll(db, col);
			}

			String last = null;
			for (String col : allColumns.keySet()) {
				if (last == null) {
					last = col;
					continue;
				}
				this.selectTwo(db, last, col);
				last = col;
			}

			try {
				this.select(db, "int_25");
				Assertions.assertTrue(false);
			} catch (CasketException e) {
				Assertions.assertEquals(CE1.INVALID_COLUMN_SIGNATURES, e.error());
			}

			SqlPort.SQL_PORT.sqlDatabaseFactory().closeDatabase(db);

		} finally {
			Utility.deleteFile(dbFile);
		}
	}

	private List<SqlObject> createRow(SqlDatabase db) throws CasketException {
		Map<String, SqlColumnSignature> columns = new HashMap<>();
		Map<String, SqlColumnSignature> allColumns = Utility.createColumns(Utility.PK_NAME, (byte) 0);

		Map<String, SqlObject> pk;
		List<SqlObject> pks = new ArrayList<>();
		for (String col : allColumns.keySet()) {
			columns.put(col, allColumns.get(col));
			columns.put(Utility.PK_NAME, allColumns.get(Utility.PK_NAME));
			Map<String, SqlObject> values = new HashMap<>();
			for (String c : columns.keySet()) {
				if (c.equals(Utility.PK_NAME))
					continue;
				values.put(c, columns.get(c).getValue());
			}

			TableAssignment tab = db.createView(Utility.TABLE_NAME, columns);
			PreCompiledStatement preStat = db.mkNewRowStmt(tab);
			Object voucher = db.beginTransaction(true);
			pk = db.newRow(preStat, values, voucher);
			db.endTransaction(voucher);
			Assertions.assertTrue(pk.containsKey(Utility.PK_NAME));
			pks.add(pk.get(Utility.PK_NAME));
			columns.clear();
		}
		return pks;
	}

	private void updateRow(SqlDatabase db, List<SqlObject> pks) throws CasketException {
		Map<String, SqlColumnSignature> columns = new HashMap<>();
		Map<String, SqlColumnSignature> allColumns = Utility.createColumns(Utility.PK_NAME, (byte) 0);
		int i = 0;
		String lastCol = null;
		for (String col : allColumns.keySet()) {
			if (lastCol == null) {
				lastCol = col;
				continue;
			}
			columns.put(lastCol, allColumns.get(lastCol));
			columns.put(col, allColumns.get(col));
			columns.put(Utility.PK_NAME, allColumns.get(Utility.PK_NAME));
			Map<String, SqlObject> values = new HashMap<>();
			for (String c : columns.keySet()) {
				if (c.equals(Utility.PK_NAME))
					continue;
				values.put(c, columns.get(c).getValue());
			}
			TableAssignment tab = db.createView(Utility.TABLE_NAME, columns);
			try (PreCompiledStatement preStat = db.mkUpdateRowStmt(tab, values.keySet())) {
				Object voucher = db.beginTransaction(true);
				db.updateRow(preStat, pks.get(i), values, voucher);
				db.endTransaction(voucher);
				i++;
				columns.clear();
			}
		}
	}

	private void createArg(SqlDatabase db) throws CasketException {
		Map<String, SqlColumnSignature> columns = new HashMap<>();
		Map<String, SqlColumnSignature> allColumns = Utility.createColumns(Utility.PK_NAME, (byte) 0);
		Map<String, SqlColumnSignature> allChecks = Utility.createColumns(Utility.PK_NAME, (byte) 1);

		String lastCol = null;
		for (String col : allColumns.keySet()) {
			if (lastCol == null) {
				lastCol = col;
				continue;
			}
			columns.put(lastCol, allColumns.get(lastCol));
			columns.put(col, allColumns.get(col));
			columns.put(Utility.PK_NAME, allColumns.get(Utility.PK_NAME));

			TableAssignment tab = db.createView(Utility.TABLE_NAME, columns);
			columns.clear();
			columns.put(lastCol, allColumns.get(lastCol));
			columns.put(col, allColumns.get(col));

			Set<SqlArg> argSet = new HashSet<>();
			for (String columnName : columns.keySet()) {
				for (CMP cmp : CMP.values()) {
					SqlArg arg = null;
					Assertions.assertNotNull(arg = db.mkSqlArg(tab, columnName, cmp));
					argSet.add(arg);
					Assertions.assertEquals(Utility.TABLE_NAME, arg.tableName());
					Assertions.assertEquals(arg.columnName(), columnName);
					Assertions.assertEquals(arg.cmp(), cmp);
					Assertions.assertEquals(arg.storageClass(), columns.get(columnName).getType());
				}
			}
			try (PreCompiledStatement query = db.mkSelectStmt(tab, argSet, OP.AND)) {
				Map<SqlArg, SqlObject> args = new HashMap<>();
				for (SqlArg a : argSet) {
					args.put(a, allChecks.get(a.columnName()).getValue());
				}
				Assertions.assertNotNull(db.select(query, args, null));
				columns.clear();
			}
		}
	}

	private void select(SqlDatabase db, String columnName) throws CasketException {

		Map<String, SqlColumnSignature> columns = new HashMap<>();

		Map<String, SqlColumnSignature> allColumns = Utility.createColumns(Utility.PK_NAME, (byte) 0);

		columns.put(columnName, allColumns.get(columnName));
		columns.put(Utility.PK_NAME, allColumns.get(Utility.PK_NAME));

		TableAssignment tab = db.createView(Utility.TABLE_NAME, columns);

		columns.clear();
		columns.put(columnName, allColumns.get(columnName));
		Set<SqlArg> argSet = new HashSet<>();
		SqlArg arg = null;
		Assertions.assertNotNull(arg = db.mkSqlArg(tab, columnName, CMP.EQUAL));
		argSet.add(arg);

		PreCompiledStatement andQuery = db.mkSelectStmt(tab, argSet, SqlArg.OP.AND);
		PreCompiledStatement orQuery = db.mkSelectStmt(tab, argSet, SqlArg.OP.AND);
		PreCompiledStatement allQuery = db.mkSelectStmt(tab, new HashSet<>(), SqlArg.OP.AND);
		Map<SqlArg, SqlObject> args = new HashMap<>();
		for (SqlArg a : argSet) {
			args.put(a, allColumns.get(a.columnName()).getValue());
		}
		List<Map<String, SqlObject>> result = db.select(andQuery, args, null);
		Assertions.assertNotNull(result);
		result = db.select(orQuery, args, null);
		Assertions.assertNotNull(result);
		result = db.select(allQuery, new HashMap<SqlArg, SqlObject>(), null);
		Assertions.assertNotNull(result);
		andQuery.close();
		orQuery.close();
		allQuery.close();

	}

	private void selectAll(SqlDatabase db, String columnName) throws CasketException {

		Map<String, SqlColumnSignature> columns = new HashMap<>();

		Map<String, SqlColumnSignature> allColumns = Utility.createColumns(Utility.PK_NAME, (byte) 0);

		for (String col : allColumns.keySet())
			columns.put(col, allColumns.get(col));
		columns.put(Utility.PK_NAME, allColumns.get(Utility.PK_NAME));

		TableAssignment tab = db.createView(Utility.TABLE_NAME, columns);

		columns.clear();
		columns.put(columnName, allColumns.get(columnName));
		Set<SqlArg> argSet = new HashSet<>();
		SqlArg arg = null;
		Assertions.assertNotNull(arg = db.mkSqlArg(tab, columnName, CMP.EQUAL));
		argSet.add(arg);

		PreCompiledStatement andQuery = db.mkSelectStmt(tab, argSet, SqlArg.OP.AND);
		PreCompiledStatement orQuery = db.mkSelectStmt(tab, argSet, SqlArg.OP.AND);
		PreCompiledStatement allQuery = db.mkSelectStmt(tab, new HashSet<>(), SqlArg.OP.AND);

		Map<SqlArg, SqlObject> args = new HashMap<>();
		for (SqlArg a : argSet) {
			args.put(a, allColumns.get(a.columnName()).getValue());
		}
		List<Map<String, SqlObject>> result = db.select(andQuery, args, null);
		Assertions.assertNotNull(result);
		result = db.select(orQuery, args, null);
		Assertions.assertNotNull(result);
		result = db.select(allQuery, new HashMap<SqlArg, SqlObject>(), null);
		Assertions.assertNotNull(result);
		andQuery.close();
		orQuery.close();
		allQuery.close();

	}

	private void selectTwo(SqlDatabase db, String... cols) throws CasketException {

		Map<String, SqlColumnSignature> columns = new HashMap<>();

		Map<String, SqlColumnSignature> allColumns = Utility.createColumns(Utility.PK_NAME, (byte) 0);

		for (String col : cols)
			columns.put(col, allColumns.get(col));
		columns.put(Utility.PK_NAME, allColumns.get(Utility.PK_NAME));

		TableAssignment tab = db.createView(Utility.TABLE_NAME, columns);

		columns.clear();
		for (String col : cols)
			columns.put(col, allColumns.get(col));

		Set<SqlArg> argSet = new HashSet<>();
		for (String col : cols) {
			SqlArg arg = null;
			Assertions.assertNotNull(arg = db.mkSqlArg(tab, col, CMP.EQUAL));
			argSet.add(arg);
		}

		PreCompiledStatement andQuery = db.mkSelectStmt(tab, argSet, SqlArg.OP.AND);
		PreCompiledStatement orQuery = db.mkSelectStmt(tab, argSet, SqlArg.OP.AND);
		PreCompiledStatement testQuery = null;
		try (PreCompiledStatement allQuery = db.mkSelectStmt(tab, SqlArg.EMPTY_SET, SqlArg.OP.AND)) {

			testQuery = allQuery;
			Map<SqlArg, SqlObject> args = new HashMap<>();
			for (SqlArg a : argSet) {
				args.put(a, allColumns.get(a.columnName()).getValue());
			}
			List<Map<String, SqlObject>> result = db.select(andQuery, args, null);
			Assertions.assertNotNull(result);
			result = db.select(orQuery, args, null);
			Assertions.assertNotNull(result);
			result = db.select(allQuery, SqlArg.EMPTY_MAP, null);
			Assertions.assertNotNull(result);

			andQuery.close();
			orQuery.close();
		}
		CasketException exc = null;
		try {
			testQuery.close();
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertEquals(CE2.ALREADY_CLOSED, exc.error());

	}

}
