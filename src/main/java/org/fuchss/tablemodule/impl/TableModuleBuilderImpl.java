package org.fuchss.tablemodule.impl;

import java.util.HashMap;
import java.util.Map;

import org.fuchss.sqlconnector.port.SqlDatabase;
import org.fuchss.sqlconnector.port.SqlObjectFactory;
import org.fuchss.tablemodule.port.TableModule;
import org.fuchss.tablemodule.port.TableModuleBuilder;

public class TableModuleBuilderImpl implements TableModuleBuilder {

	Map<SqlDatabase, TableModuleImpl> databaseToTableModuleMap = new HashMap<>();

	@Override
	public TableModule tableModule(SqlDatabase database, SqlObjectFactory sqlObjectFactoryt) {
		return new TableModuleImpl(database, sqlObjectFactoryt);

		/*
		 * TableModuleImpl tm; if ((tm = this.databaseToTableModuleMap.get(database)) ==
		 * null) { this.databaseToTableModuleMap.put(database, tm = new
		 * TableModuleImpl(database, sqlObjectFactoryt)); } return tm;
		 */
	}

}
