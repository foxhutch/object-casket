package org.fuchss.objectcasket.sqlconnector.api;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

class TestSelectRow extends PrepareTable {

	@Test
	void selectRow() throws CasketException, IOException {
		this.selectRow(DB.SQLITE);
		this.selectRow(DB.H2);
	}

	@Test
	void selectRow2() throws CasketException, IOException {
		this.selectRow2(DB.SQLITE);
		this.selectRow2(DB.H2);
	}

	private void selectRow(DB db) throws CasketException, IOException {
		this.initTable(db);

		Set<SqlArg> argSet = new HashSet<>();
		SqlArg arg1 = this.db.mkSqlArg(this.table, TextCol, CMP.EQUAL);
		SqlArg arg2 = this.db.mkSqlArg(this.table, TextCol, CMP.EQUAL);
		argSet.add(arg1);
		argSet.add(arg2);
		PreCompiledStatement select = this.db.mkSelectStmt(this.table, argSet, OP.OR);

		Map<SqlArg, SqlObject> args = new HashMap<>();
		args.put(arg1, this.factory.mkSqlObject(StorageClass.TEXT, "123"));
		args.put(arg1, this.factory.mkSqlObject(StorageClass.TEXT, "456"));

		List<Map<String, SqlObject>> result = null;
		CasketException exc = null;
		Object voucher = this.db.beginTransaction(true);
		try {
			result = this.db.select(select, args, voucher);
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertNull(result);
		Assertions.assertEquals(CasketError.INVALID_ARGUMENTS, exc.error());

		args.clear();
		args.put(arg1, this.factory.mkSqlObject(StorageClass.TEXT, "123"));
		args.put(arg2, this.factory.mkSqlObject(StorageClass.TEXT, "456"));

		result = this.db.select(select, args, voucher);

		this.db.endTransaction(voucher);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.size());
		if (!result.get(0).get(TextCol).get(String.class).equals("123"))
			Assertions.assertEquals("456", result.get(0).get(TextCol).get(String.class));
		select.close();
		this.delTable();

	}

	private void selectRow2(DB db) throws CasketException, IOException {
		this.initTable(db);

		Set<SqlArg> argSet = new HashSet<>();
		SqlArg arg = this.db.mkSqlArg(this.table, TextCol, CMP.UNEQUAL);
		argSet.add(arg);
		PreCompiledStatement select = this.db.mkSelectStmt(this.table, argSet, OP.AND);

		Map<SqlArg, SqlObject> args = new HashMap<>();
		args.put(arg, this.factory.mkSqlObject(StorageClass.TEXT, "123"));

		Object voucher = this.db.beginTransaction(false);
		List<Map<String, SqlObject>> result = this.db.select(select, args, null);
		this.db.endTransaction(voucher);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.size());
		for (Map<String, SqlObject> row : result)
			Assertions.assertNotEquals("123", row.get(TextCol).get(String.class));

		try {
			result = this.db.select(select, args, voucher);
		} catch (Exception exp) {
			result = null;
		}
		Assertions.assertNull(result);

		this.delTable();

	}

}
