package org.fuchss.objectcasket.sqlconnector.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

class TestRollback extends PrepareTable {

	@Test
	void rollback() throws CasketException, IOException {
		this.rollback(DB.SQLITE);
		this.rollback(DB.H2);
	}

	@Test
	void wrongTransaction() throws CasketException, IOException {
		this.wrongTransaction(DB.SQLITE);
		this.wrongTransaction(DB.H2);
	}

	private void rollback(DB db) throws CasketException, IOException {
		this.initTable(db);

		List<Map<String, SqlObject>> result = null;
		Set<SqlArg> argSet = new HashSet<>();

		SqlArg arg = this.db.mkSqlArg(this.table, TextCol, CMP.EQUAL);
		argSet.add(arg);
		PreCompiledStatement select = this.db.mkSelectStmt(this.table, argSet, OP.AND);
		PreCompiledStatement delete = this.db.mkDeleteStmt(this.table, argSet, OP.AND);

		Map<SqlArg, SqlObject> args = new HashMap<>();
		args.put(arg, this.factory.mkSqlObject(StorageClass.TEXT, "123"));

		Object voucher = this.db.beginTransaction(true);
		result = this.db.select(select, args, voucher);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.size());
		CasketException exc = null;
		try {
			this.db.delete(delete, args, new Object());
			Assertions.fail("Delete shall not work here.");
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertEquals(CasketError.WRONG_TRANSACTION, exc.error());

		this.db.delete(delete, args, voucher);
		result = this.db.select(select, args, voucher);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(0, result.size());
		this.db.rollback(voucher);

		voucher = this.db.beginTransaction(true);
		result = this.db.select(select, args, null);
		this.db.endTransaction(voucher);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.size());

		this.delTable();

	}

	void wrongTransaction(DB db) throws CasketException, IOException {
		this.initTable(db);

		List<Map<String, SqlObject>> result = null;
		Set<SqlArg> argSet = new HashSet<>();

		SqlArg arg = this.db.mkSqlArg(this.table, TextCol, CMP.EQUAL);
		argSet.add(arg);
		PreCompiledStatement select = this.db.mkSelectStmt(this.table, argSet, OP.AND);
		PreCompiledStatement delete = this.db.mkDeleteStmt(this.table, argSet, OP.AND);

		Map<SqlArg, SqlObject> args = new HashMap<>();
		args.put(arg, this.factory.mkSqlObject(StorageClass.TEXT, "123"));

		Object voucher = this.db.beginTransaction(false);
		result = this.db.select(select, args, null);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.size());
		this.db.delete(delete, args, voucher);
		result = this.db.select(select, args, voucher);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(0, result.size());

		CasketException exc = null;
		try {
			this.db.endTransaction(new Object());
		} catch (CasketException e) {
			exc = e;
			this.db.rollback(voucher);
		}
		Assertions.assertEquals(CasketError.WRONG_TRANSACTION, exc.error());

		exc = null;
		voucher = this.db.beginTransaction(false);
		try {
			result = this.db.select(select, args, new Object());
		} catch (CasketException e) {
			exc = e;
		}
		Assertions.assertEquals(CasketError.WRONG_TRANSACTION, exc.error());

		result = this.db.select(select, args, voucher);
		this.db.endTransaction(voucher);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.size());

		this.delTable();

	}

}
