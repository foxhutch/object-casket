package org.fuchss.tablemodule;

import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.tablemodule.impl.TableModuleBuilderImpl;
import org.fuchss.tablemodule.port.ModulePort;
import org.fuchss.tablemodule.port.TableModule;
import org.fuchss.tablemodule.port.TableModuleBuilder;

class TableModuleFactoryImpl implements TableModuleFactory, ModulePort, TableModuleBuilder {

	private TableModuleBuilderImpl tableModuleBuilder;

	@Override
	public ModulePort modulePort() {
		return this;
	}

	@Override
	public TableModuleBuilder tableModuleBuilder() {
		if (this.tableModuleBuilder == null) {
			this.tableModuleBuilder = new TableModuleBuilderImpl();
		}
		return this;
	}

	@Override
	public TableModule tableModule(SqlDatabase database, SqlObjectFactory objectFactory) {
		return this.tableModuleBuilder.tableModule(database, objectFactory);
	}

}
