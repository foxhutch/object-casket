package org.fuchss.objectcasket.sqlconnector.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.DatabaseObserver;
import org.fuchss.objectcasket.sqlconnector.port.PreCompiledStatement;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlObject;
import org.fuchss.objectcasket.sqlconnector.port.StorageClass;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDBObserver extends PrepareTable {

	@Test
	void observe() throws CasketException, IOException {
		this.observe(DB.SQLITE);
		this.observe(DB.H2);
	}

	private void observe(DB db) throws CasketException, IOException {
		Exception exc = null;
		try {
			DBObs obs = new DBObs();
			TableAssignment tab = this.initTable("Table2", "KeyText", db);
			PreCompiledStatement preStat = this.db.mkNewRowStmt(tab);
			PreCompiledStatement preStatUpdate = null;// this.db.mkUpdateRowStmt(tab);

			Set<SqlArg> argSet = new HashSet<>();

			SqlArg arg = this.db.mkSqlArg(tab, TextCol, CMP.EQUAL);
			argSet.add(arg);

			PreCompiledStatement preStatDel = this.db.mkDeleteStmt(tab, argSet, OP.AND);

			this.db.attach(obs, tab);
			this.db.attach(obs, this.table);

			Map<String, SqlObject> values = new HashMap<>();
			values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "123"));
			values.put("KeyText", this.factory.mkSqlObject(StorageClass.TEXT, "nKey1"));

			Object voucher = this.db.beginTransaction(true);
			this.db.newRow(preStat, values, voucher);

			SqlObject pkObj = null;
			values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "456"));
			values.put("KeyText", pkObj = this.factory.mkSqlObject(StorageClass.TEXT, "nKey2"));

			this.db.newRow(preStat, values, voucher);

			values.clear();

			values.put(TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "789"));

			preStatUpdate = this.db.mkUpdateRowStmt(tab, values.keySet());
			this.db.updateRow(preStatUpdate, pkObj, values, voucher);

			Map<SqlArg, SqlObject> args = new HashMap<>();
			args.put(arg, this.factory.mkSqlObject(StorageClass.TEXT, "123"));

			this.db.delete(preStatDel, args, voucher);

			PreCompiledStatement preStat2 = this.db.mkNewRowStmt(this.table);
			values.clear();
			values.put(PrepareTable.TextCol, this.factory.mkSqlObject(StorageClass.TEXT, "123456789"));

			this.db.newRow(preStat2, values, voucher);

			this.db.endTransaction(voucher);

			this.db.detach(obs, this.table);

			voucher = this.db.beginTransaction(true);
			this.db.newRow(preStat2, values, voucher);
			this.db.endTransaction(voucher);

			this.delTable();
			preStatUpdate.close();
			preStat2.close();

		} catch (CasketException | IOException e) {
			exc = e;
		}
		Assertions.assertNull(exc);

	}

	private static class DBObs implements DatabaseObserver {

		@Override
		public void update(TableAssignment tabOrView, List<SqlObject> changed, List<SqlObject> deleted, List<SqlObject> added) {
			Assertions.assertNotNull(tabOrView);
			Assertions.assertNotNull(changed);
			Assertions.assertNotNull(deleted);
			Assertions.assertNotNull(added);
		}

	}

}
