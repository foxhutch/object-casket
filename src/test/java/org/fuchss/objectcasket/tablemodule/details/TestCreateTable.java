package org.fuchss.objectcasket.tablemodule.details;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.tablemodule.ModulePort;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCreateTable {

	TableModuleFactory tabFac = ModulePort.PORT.tableModuleFactory();
	ModuleConfiguration config = null;

	SqlObjectFactory objFac = SqlPort.SQL_PORT.sqlObjectFactory();

	File dbFile = null;

	@Test
	void createTable() throws CasketException, IOException {
		this.createTable(DB.SQLITE);
		this.createTable(DB.H2);
	}

	private void createTable(DB dialect) throws CasketException, IOException {

		this.createConfig(dialect);
		TableModule tabMod = this.tabFac.newTableModule(this.config);

		Map<String, Class<? extends Serializable>> columns = new HashMap<>();
		columns.put("PkCol", Integer.class);
		columns.put("text", String.class);
		columns.put("text2", String.class);

		Table table = tabMod.createTable(TableA.class.getSimpleName(), "PkCol", columns, true);
		Assertions.assertNotNull(table);

		this.tabFac.closeModule(tabMod);

		tabMod = this.tabFac.newTableModule(this.config);
		columns.remove("text");

		Table table2 = tabMod.mkView(TableA.class.getSimpleName(), "PkCol", columns, true);

		Assertions.assertNotNull(table2);
		this.tabFac.closeModule(tabMod);

		this.delConfig();
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

	private void delConfig() throws CasketException, IOException {
		this.config = null;
		Files.deleteIfExists(this.dbFile.toPath());
	}

	private static class TableA {

	}

}
