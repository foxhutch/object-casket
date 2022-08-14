package org.fuchss.objectcasket.tablemodule.details;

import org.fuchss.objectcasket.common.CasketError;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.ModulePort;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class TestModuleFactory {

	TableModuleFactory tabFac = ModulePort.PORT.tableModuleFactory();

	@Test
	void createConfig() {

		ModuleConfiguration config = this.tabFac.createConfiguration();
		Assertions.assertNotNull(config);

	}

	@Test
	void modConfig() throws CasketException, IOException {
		this.modConfig(DB.SQLITE);
		this.modConfig(DB.H2);
	}

	@Test
	void createTableModule() throws CasketException, IOException {
		this.createTableModule(DB.SQLITE);
		this.createTableModule(DB.H2);
	}

	@Test
	void createTableModules() throws CasketException, IOException {
		this.createTableModules(DB.SQLITE);
		this.createTableModules(DB.H2);
	}

	private void modConfig(DB dialect) throws CasketException, IOException {

		ModuleConfiguration config = this.tabFac.createConfiguration();
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		File dbFile = Utility.createFile(this);
		config.setUri(dbFile.toURI().getPath());
		config.setUser("xyz");
		config.setPassword("geheim");
		config.setFlag(ModuleConfiguration.Flag.MODIFY, ModuleConfiguration.Flag.CREATE);
		Assertions.assertTrue(config.containsAll(ModuleConfiguration.Flag.MODIFY));
		Assertions.assertTrue(config.containsAll(ModuleConfiguration.Flag.CREATE));
		Assertions.assertTrue(config.containsAll(ModuleConfiguration.Flag.CREATE, ModuleConfiguration.Flag.MODIFY));
		config.removeFlag(ModuleConfiguration.Flag.MODIFY);
		Assertions.assertTrue(config.containsAll(ModuleConfiguration.Flag.CREATE));
		Assertions.assertFalse(config.containsAll(ModuleConfiguration.Flag.MODIFY));
		Assertions.assertFalse(config.containsAll(ModuleConfiguration.Flag.CREATE, ModuleConfiguration.Flag.MODIFY));
		config.removeFlag(ModuleConfiguration.Flag.MODIFY, ModuleConfiguration.Flag.CREATE);
		Assertions.assertFalse(config.containsAll(ModuleConfiguration.Flag.CREATE));
		Assertions.assertFalse(config.containsAll(ModuleConfiguration.Flag.MODIFY));
		Assertions.assertFalse(config.containsAll(ModuleConfiguration.Flag.CREATE, ModuleConfiguration.Flag.MODIFY));
	}

	private void createTableModule(DB dialect) throws CasketException, IOException {

		ModuleConfiguration config = this.tabFac.createConfiguration();
		File dbFile = Utility.createFile(this);
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(ModuleConfiguration.Flag.MODIFY, ModuleConfiguration.Flag.CREATE);

		TableModule tabMod = this.tabFac.newTableModule(config);
		Assertions.assertNotNull(tabMod);
		Assertions.assertFalse(tabMod.isClosed());

		this.tabFac.closeModule(tabMod);

		Assertions.assertTrue(tabMod.isClosed());
		Files.deleteIfExists(dbFile.toPath());
	}

	private void createTableModules(DB dialect) throws CasketException, IOException {

		ModuleConfiguration config = this.tabFac.createConfiguration();
		File dbFile = Utility.createFile(this);
		config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
		config.setUri(dbFile.toURI().getPath());
		config.setUser("");
		config.setPassword("");
		config.setFlag(ModuleConfiguration.Flag.MODIFY, ModuleConfiguration.Flag.CREATE);

		TableModule[] tabMods = new TableModule[] { this.tabFac.newTableModule(config), this.tabFac.newTableModule(config), this.tabFac.newTableModule(config) };

		for (TableModule mod : tabMods)
			Assertions.assertFalse(mod.isClosed());
		this.tabFac.closeAllModules(config);
		for (TableModule mod : tabMods)
			Assertions.assertTrue(mod.isClosed());

		try {
			this.tabFac.closeModule(tabMods[0]);
		} catch (CasketException exc) {
			Assertions.assertEquals(exc.getMessage(), CasketError.TABLE_MODULE_ALREADY_CLOSED.build().getMessage());
		}

		Files.deleteIfExists(dbFile.toPath());
	}

}
