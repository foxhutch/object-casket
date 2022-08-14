package org.fuchss.objectcasket.tablemodule.api;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.ModulePort;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;
import org.fuchss.objectcasket.tablemodule.port.TableObserver;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestTableObserver {

	TableModuleFactory tabFac = ModulePort.PORT.tableModuleFactory();
	ModuleConfiguration config = null;
	TableModule tabMod = null;
	TableModule tabMod2 = null;
	Table table = null;
	Table view = null;
	File dbFile = null;

	String[] columnNames = { "text", "text1", "text2" };
	String[] content = { "TEXT0_", "TEXT1_", "TEXT2_" };

	@Test
	void observeOther() throws CasketException, IOException {
		this.observeOther(DB.SQLITE);
		this.observeOther(DB.H2);
	}

	@Test
	void observe() throws CasketException, IOException {
		this.observe(DB.SQLITE);
		this.observe(DB.H2);
	}

	private void observeOther(DB dialect) throws CasketException, IOException {
		this.createTable(dialect, "TableA");
		TableObs obs = new TableObs();

		this.view.register(obs);

		Map<String, Serializable> values = new HashMap<>();
		Row[] rows = new Row[3];

		Object voucher = this.tabMod.beginTransaction();

		values.clear();
		for (int i = 0; i < this.columnNames.length; i++)
			values.put(this.columnNames[i], this.content[i] + "0");
		rows[0] = this.table.createRow(values, voucher);

		values.clear();
		for (int i = 0; i < this.columnNames.length; i++)
			values.put(this.columnNames[i], this.content[i] + "1");
		rows[1] = this.table.createRow(values, voucher);

		values.clear();

		for (int i = 0; i < this.columnNames.length; i++)
			values.put(this.columnNames[i], this.content[i] + "2");
		rows[2] = this.table.createRow(values, voucher);

		obs.assertNull();
		this.tabMod.endTransaction(voucher);
		obs.assertNotNull();

		Assertions.assertEquals(3, obs.added.size());
		Assertions.assertEquals(0, obs.deleted.size());
		Assertions.assertEquals(0, obs.changed.size());

		for (Row row : obs.added) {
			Assertions.assertNotEquals(rows[row.getPk(Integer.class) - 1], row);
			for (int i = 0; i < this.columnNames.length; i++)
				Assertions.assertEquals(rows[row.getPk(Integer.class) - 1].getValue(this.columnNames[i], String.class), row.getValue(this.columnNames[i], String.class));
		}

		// update content

		obs.clear();
		voucher = this.tabMod.beginTransaction();
		values.clear();
		values.put(this.columnNames[0], this.content[0] + "3");
		values.put(this.columnNames[1], this.content[0] + "3");
		this.table.updateRow(rows[0], values, voucher);
		values.clear();
		values.put(this.columnNames[0], this.content[0] + "4");
		values.put(this.columnNames[1], this.content[0] + "4");
		this.table.updateRow(rows[1], values, voucher);

		obs.assertNull();
		this.tabMod.endTransaction(voucher);
		obs.assertNotNull();

		Assertions.assertEquals(0, obs.added.size());
		Assertions.assertEquals(0, obs.deleted.size());
		Assertions.assertEquals(2, obs.changed.size());

		for (Row row : obs.changed) {
			Assertions.assertNotEquals(rows[row.getPk(Integer.class) - 1], row);
			Assertions.assertNotEquals(rows[row.getPk(Integer.class) - 1].getValue(this.columnNames[0], String.class), row.getValue(this.columnNames[0], String.class));
			Assertions.assertNotEquals(rows[row.getPk(Integer.class) - 1].getValue(this.columnNames[1], String.class), row.getValue(this.columnNames[1], String.class));
			Assertions.assertEquals(rows[row.getPk(Integer.class) - 1].getValue(this.columnNames[2], String.class), row.getValue(this.columnNames[2], String.class));
			voucher = this.tabMod2.beginTransaction();
			this.view.reloadRow(row, voucher);
			this.tabMod2.endTransaction(voucher);
			for (int i = 0; i < this.columnNames.length; i++)
				Assertions.assertEquals(rows[row.getPk(Integer.class) - 1].getValue(this.columnNames[i], String.class), row.getValue(this.columnNames[i], String.class));
		}

		// delete content

		obs.clear();
		voucher = this.tabMod.beginTransaction();

		this.table.deleteRow(rows[0], voucher);
		this.table.deleteRow(rows[1], voucher);

		obs.assertNull();
		this.tabMod.endTransaction(voucher);
		obs.assertNotNull();

		Assertions.assertEquals(0, obs.added.size());
		Assertions.assertEquals(2, obs.deleted.size());
		Assertions.assertEquals(0, obs.changed.size());

		for (Row row : obs.deleted) {
			Assertions.assertNull(row.getPk(Integer.class));
			for (int i = 0; i < this.columnNames.length; i++)
				Assertions.assertNull(row.getValue(this.columnNames[i], String.class));
		}

		this.close();

	}

	private void observe(DB dialect) throws CasketException, IOException {
		this.createTable(dialect, "TableA");
		TableObs obs = new TableObs();

		this.table.register(obs);

		Map<String, Serializable> values = new HashMap<>();
		Row[] rows = new Row[3];

		Object voucher = this.tabMod.beginTransaction();

		values.clear();
		for (int i = 0; i < this.columnNames.length; i++)
			values.put(this.columnNames[i], this.content[i] + "0");
		rows[0] = this.table.createRow(values, voucher);

		values.clear();
		for (int i = 0; i < this.columnNames.length; i++)
			values.put(this.columnNames[i], this.content[i] + "1");
		rows[1] = this.table.createRow(values, voucher);

		values.clear();
		for (int i = 0; i < this.columnNames.length; i++)
			values.put(this.columnNames[i], this.content[i] + "2");
		rows[2] = this.table.createRow(values, voucher);

		obs.assertNull();
		this.tabMod.endTransaction(voucher);
		obs.assertNotNull();

		Assertions.assertEquals(3, obs.added.size());
		Assertions.assertEquals(0, obs.deleted.size());
		Assertions.assertEquals(0, obs.changed.size());

		for (Row row : rows)
			Assertions.assertTrue(obs.added.contains(row));

		// update content

		obs.clear();
		voucher = this.tabMod.beginTransaction();
		values.clear();
		values.put(this.columnNames[0], this.content[0] + "3");
		values.put(this.columnNames[1], this.content[1] + "3");
		this.table.updateRow(rows[0], values, voucher);
		values.clear();

		values.put(this.columnNames[0], this.content[0] + "4");
		values.put(this.columnNames[1], this.content[1] + "4");
		this.table.updateRow(rows[1], values, voucher);

		obs.assertNull();
		this.tabMod.endTransaction(voucher);
		obs.assertNotNull();

		Assertions.assertEquals(0, obs.added.size());
		Assertions.assertEquals(0, obs.deleted.size());
		Assertions.assertEquals(2, obs.changed.size());

		Assertions.assertTrue(obs.changed.contains(rows[0]));
		Assertions.assertTrue(obs.changed.contains(rows[1]));

		// delete content

		obs.clear();
		voucher = this.tabMod.beginTransaction();

		this.table.deleteRow(rows[0], voucher);
		this.table.deleteRow(rows[1], voucher);

		obs.assertNull();
		this.tabMod.endTransaction(voucher);
		obs.assertNotNull();

		Assertions.assertEquals(0, obs.added.size());
		Assertions.assertEquals(2, obs.deleted.size());
		Assertions.assertEquals(0, obs.changed.size());

		Assertions.assertTrue(obs.deleted.contains(rows[0]));
		Assertions.assertTrue(obs.deleted.contains(rows[1]));

		this.close();
	}

	private void createTable(DB dialect, String name) throws CasketException, IOException {
		this.config = this.tabFac.createConfiguration();
		this.dbFile = Utility.createFile(this);
		this.config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		this.config.setUri(this.dbFile.toURI().getPath());
		this.config.setUser("");
		this.config.setPassword("");
		this.config.setFlag(ModuleConfiguration.Flag.MODIFY, ModuleConfiguration.Flag.CREATE);

		this.tabMod = this.tabFac.newTableModule(this.config);
		this.tabMod2 = this.tabFac.newTableModule(this.config);

		Map<String, Class<? extends Serializable>> columns = new HashMap<>();
		columns.put("PkCol", Integer.class);
		columns.put("text", String.class);
		columns.put("text1", String.class);
		columns.put("text2", String.class);

		this.table = this.tabMod.createTable(name, "PkCol", columns, true);
		this.view = this.tabMod2.mkView(name, "PkCol", columns, true);

	}

	private void close() throws CasketException, IOException {
		this.tabFac.closeAllModules(this.config);
		Files.deleteIfExists(this.dbFile.toPath());
	}

	private static class TableObs implements TableObserver {

		Set<Row> changed = null;
		Set<Row> deleted = null;
		Set<Row> added = null;

		@Override
		public void update(Set<Row> changed, Set<Row> deleted, Set<Row> added) {
			this.changed = changed;
			this.deleted = deleted;
			this.added = added;
		}

		void clear() {
			this.changed = null;
			this.deleted = null;
			this.added = null;
		}

		void assertNull() {
			Assertions.assertNull(this.added);
			Assertions.assertNull(this.deleted);
			Assertions.assertNull(this.changed);
		}

		void assertNotNull() {
			Assertions.assertNotNull(this.added);
			Assertions.assertNotNull(this.deleted);
			Assertions.assertNotNull(this.changed);
		}

	}

}
