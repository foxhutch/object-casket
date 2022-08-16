package org.fuchss.objectcasket.tablemodule.examples;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.ModulePort;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.Table.TabCMP;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestModulePort {

	@Test
	void usingTheModulePort() throws IOException, CasketException {
		this.usingTheModulePort(DB.SQLITE);
		this.usingTheModulePort(DB.H2);
	}

	private void usingTheModulePort(DB dialect) throws IOException, CasketException {

		File dbFile = Utility.createFile(this);
		TableModuleFactory tabModFactory = null;
		ModuleConfiguration config = null;

		try {
			// 1st: Get the table module factory!

			tabModFactory = ModulePort.PORT.tableModuleFactory();

			// 2nd: Create a configuration!
			config = tabModFactory.createConfiguration();
			config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
			config.setUri(dbFile.toURI().getPath());
			config.setUser("");
			config.setPassword("");
			config.setFlag(ModuleConfiguration.Flag.MODIFY, ModuleConfiguration.Flag.CREATE);

			// 3rd: Create a table module!
			TableModule tabMod = tabModFactory.newTableModule(config);

			// 4th: Work with the table module!

			// Create a table,
			Map<String, Class<? extends Serializable>> columns = new HashMap<>();
			columns.put("PkCol", Integer.class);
			columns.put("text", String.class);

			Table table = tabMod.createTable("Table", "PkCol", columns, true);

			// add some rows,
			Map<String, Serializable> values = new HashMap<>();

			Object voucher = tabMod.beginTransaction();
			values.put("text", "some text");
			table.createRow(values, voucher);

			values.clear();
			values.put("text", "some other text");
			table.createRow(values, voucher);

			values.clear();
			values.put("text", "some more text");
			Row row = table.createRow(values, voucher);

			tabMod.endTransaction(voucher);

			// modify some row.

			voucher = tabMod.beginTransaction();
			values.clear();
			values.put("text", "realy more text");
			table.updateRow(row, values, voucher);
			tabMod.endTransaction(voucher);

			// 5th: Work with another table module

			// Create a view,

			Table view = tabMod.mkView("Table", "PkCol", columns, true);

			// and select some rows.
			voucher = tabMod.beginTransaction();
			Set<Table.Exp> args = new HashSet<>();
			args.add(new Table.Exp("text", TabCMP.UNEQUAL, "some text"));
			args.add(new Table.Exp("text", TabCMP.UNEQUAL, "some other text"));
			List<Row> rows = view.searchRows(args, voucher);

			tabMod.endTransaction(voucher);

			// Now work with the result.
			Assertions.assertEquals(1, rows.size());
			Assertions.assertEquals("realy more text", rows.get(0).getValue("text", String.class));

			// 6th: Finally close all table modules!

			tabModFactory.closeAllModules(config);

			// That's all. Try also to delete a row, ...

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Files.deleteIfExists(dbFile.toPath());
		}

	}

}
