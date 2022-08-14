package org.fuchss.objectcasket.tablemodule.api;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.ModulePort;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestTableModule {

	TableModuleFactory tabFac = ModulePort.PORT.tableModuleFactory();
	ModuleConfiguration config = null;
	File dbFile = null;

	@Test
	void createTableModule() throws CasketException, IOException {
		this.createTableModule(DB.SQLITE);
		this.createTableModule(DB.H2);
	}

	private void createTableModule(DB dialect) throws CasketException, IOException {

		this.createConfig(dialect);
		TableModule tabMod = this.tabFac.newTableModule(this.config);

		Map<String, Class<? extends Serializable>> columns = new HashMap<>();
		columns.put("PkCol", Integer.class);
		columns.put("text", String.class);
		columns.put("text1", String.class);
		columns.put("text2", String.class);

		Table table = tabMod.createTable("TableA", "PkCol", columns, true);

		columns.remove("text2");

		Table view = tabMod.mkView("TableA", "PkCol", columns, true);

		Object voucher = tabMod.beginTransaction();
		Row row1 = table.createRow(new HashMap<>(), voucher);
		Row row2 = view.createRow(new HashMap<>(), voucher);

		Assertions.assertNotNull(row1.getPk(Integer.class));
		Assertions.assertNotNull(row2.getPk(Integer.class));
		Assertions.assertNotEquals(row1.getPk(Integer.class), row2.getPk(Integer.class));

		tabMod.endTransaction(voucher);

		TableModule tabMod2 = this.tabFac.newTableModule(this.config);

		Assertions.assertNotNull(tabMod2);
		Assertions.assertNotEquals(tabMod, tabMod2);

		columns.remove("text1");

		Table view2 = tabMod2.mkView("TableA", "PkCol", columns, true);

		voucher = tabMod2.beginTransaction();

		List<Row> rows = view2.allRows(voucher);

		Assertions.assertEquals(2, rows.size());
		view2.deleteRow(rows.get(0), voucher);
		view2.deleteRow(rows.get(1), voucher);

		tabMod2.endTransaction(voucher);

		Assertions.assertTrue(row1.isDirty());
		Assertions.assertTrue(row2.isDirty());

		Assertions.assertNull(row1.getPk(Integer.class));
		Assertions.assertNull(row2.getPk(Integer.class));

		this.tabFac.closeModule(tabMod);
		this.tabFac.closeModule(tabMod2);
		this.tabFac.closeAllModules(this.config);

		tabMod = this.tabFac.newTableModule(this.config);
		columns.put("Integer1", Integer.class);
		columns.remove("text1");
		tabMod.adjustTable("TableA", "PkCol", columns, true);

		this.tabFac.closeAllModules(this.config);
		tabMod = this.tabFac.newTableModule(this.config);

		columns.remove("Integer1");
		columns.put("text1", String.class);

		Table view3 = null;
		boolean error = false;
		try {
			view3 = tabMod.mkView("TableA", "PkCol", columns, true);
		} catch (Exception e) {
			error = true;
		}
		Assertions.assertTrue(error);
		Assertions.assertNull(view3);

		columns.put("Integer1", Integer.class);
		columns.remove("text1");

		view3 = tabMod.mkView("TableA", "PkCol", columns, true);
		Assertions.assertNotNull(view3);

		this.tabFac.closeAllModules(this.config);
		tabMod = this.tabFac.newTableModule(this.config);

		tabMod.dropTable("TableA");

		view3 = null;
		error = false;
		try {
			view3 = tabMod.mkView("TableA", "PkCol", columns, true);
		} catch (Exception e) {
			error = true;
		}

		Assertions.assertTrue(error);
		Assertions.assertNull(view3);

		this.tabFac.closeModule(tabMod);

		Utility.deleteFile(this.dbFile);
	}

	private void createConfig(DB dialect) throws CasketException, IOException {
		this.config = this.tabFac.createConfiguration();
		this.dbFile = Utility.createFile(this);
		this.config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		this.config.setUri(this.dbFile.toURI().getPath());
		this.config.setUser("");
		this.config.setPassword("");
		this.config.setFlag(ModuleConfiguration.Flag.MODIFY, ModuleConfiguration.Flag.CREATE);
	}

}
