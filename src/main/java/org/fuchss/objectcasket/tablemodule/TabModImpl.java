package org.fuchss.objectcasket.tablemodule;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.tablemodule.impl.ModuleFactoryImpl;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;

class TabModImpl implements ModulePort, TableModuleFactory {

	private final SqlPort sqlPort = SqlPort.SQL_PORT;

	private ModuleFactoryImpl modFac;

	@Override
	public synchronized TableModuleFactory tableModuleFactory() {
		if (this.modFac == null)
			this.modFac = new ModuleFactoryImpl(this.sqlPort.sqlObjectFactory(), this.sqlPort.sqlDatabaseFactory());
		return this;
	}

	@Override
	public synchronized void closeModule(TableModule tabMod) throws CasketException {
		this.modFac.closeModule(tabMod);
	}

	@Override
	public synchronized void closeAllModules(ModuleConfiguration config) throws CasketException {
		this.modFac.closeAllModules(config);
	}

	@Override
	public synchronized ModuleConfiguration createConfiguration() {
		return this.modFac.createConfiguration();
	}

	@Override
	public synchronized TableModule newTableModule(ModuleConfiguration config) throws CasketException {
		return this.modFac.newTableModule(config);
	}

}
