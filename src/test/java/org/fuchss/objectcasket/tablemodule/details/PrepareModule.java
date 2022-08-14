package org.fuchss.objectcasket.tablemodule.details;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.SqlPort;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.tablemodule.ModulePort;
import org.fuchss.objectcasket.tablemodule.port.*;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

class PrepareModule {

	protected TableModuleFactory tabFac = ModulePort.PORT.tableModuleFactory();
	protected ModuleConfiguration config = null;
	protected SqlObjectFactory objFac = SqlPort.SQL_PORT.sqlObjectFactory();
	protected File dbFile = null;
	protected TableModule tabMod;
	protected Table[] table;

	protected String textColumns[] = { "text1", "text2", "text3" };
	protected String fkColumns[] = { "FKtext" };
	protected String pkColumn = "PkCol";
	protected Class<? extends Serializable> pkType = Integer.class;
	protected Class<? extends Serializable> fkType = Integer.class;
	protected Map<String, Class<? extends Serializable>> columns = new HashMap<>();

	protected Row[][] initTables(DB dialect, Class<?>... clazzs) throws CasketException, IOException {

		this.initModule(dialect, true, clazzs);
		Object voucher = this.tabMod.beginTransaction();

		Row[][] row = new Row[this.table.length][10];
		for (int tIdx = 0; tIdx < row.length; tIdx++) {
			for (int rIdx = 0; rIdx < row.length; rIdx++) {

				Map<String, Serializable> values = new HashMap<>();
				int i = 0;
				for (String col : this.textColumns)
					values.put(col, "abc" + ((100 * (tIdx + 1)) + (10 * rIdx) + i++));

				row[tIdx][rIdx] = this.table[tIdx].createRow(values, voucher);
			}
		}
		this.tabMod.endTransaction(voucher);
		return row;
	}

	protected void initModule(DB dialect, boolean autoIncrement, Class<?>... clazzs) throws CasketException, IOException {
		this.createConfig(dialect);
		this.tabMod = this.tabFac.newTableModule(this.config);

		this.columns.clear();
		this.columns.put(this.pkColumn, this.pkType);

		for (String col : this.textColumns)
			this.columns.put(col, String.class);

		for (String col : this.fkColumns)
			this.columns.put(col, this.fkType);

		this.table = new Table[clazzs.length];
		int i = 0;
		for (Class<?> clazz : clazzs)
			this.table[i++] = this.tabMod.createTable(clazz.getSimpleName(), this.pkColumn, this.columns, autoIncrement);

	}

	protected void reopenModule(Class<?>... clazzs) throws CasketException, IOException {
		this.tabFac.closeModule(this.tabMod);
		this.tabMod = null;
		this.table = null;

		this.tabMod = this.tabFac.newTableModule(this.config);

		Map<String, Class<? extends Serializable>> columns = new HashMap<>();
		columns.put(this.pkColumn, this.pkType);

		for (String col : this.textColumns)
			columns.put(col, String.class);

		for (String col : this.fkColumns)
			columns.put(col, this.fkType);

		this.table = new Table[clazzs.length];
		int i = 0;

		for (Class<?> clazz : clazzs)
			this.table[i++] = this.tabMod.mkView(clazz.getSimpleName(), this.pkColumn, columns, true);

	}

	protected void delTable() throws CasketException, IOException {

		this.tabFac.closeModule(this.tabMod);
		this.tabMod = null;
		this.table = null;
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

}
