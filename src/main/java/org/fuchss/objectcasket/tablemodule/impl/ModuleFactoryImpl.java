package org.fuchss.objectcasket.tablemodule.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError.CE2;
import org.fuchss.objectcasket.common.CasketError.CE4;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabaseFactory;
import org.fuchss.objectcasket.sqlconnector.port.SqlObjectFactory;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;

/**
 * The implementation of the {@link TableModuleFactory}.
 */
public class ModuleFactoryImpl implements TableModuleFactory {

	private final SqlObjectFactory objFac;
	private final SqlDatabaseFactory dbFac;

	private final Map<ModuleConfigurationImpl, Set<TableModuleImpl>> configModuleMap = new HashMap<>();
	private final Map<ModuleConfigurationImpl, SqlDatabase> configDBMap = new HashMap<>();
	private final Map<SqlDatabase, ModuleConfigurationImpl> dbConfigMap = new HashMap<>();

	/**
	 * The constructor.
	 *
	 * @param objFac
	 *            - the assigned {@link SqlObjectFactory}.
	 * @param dbFac
	 *            - the assigned {@link SqlDatabaseFactory}.
	 */
	public ModuleFactoryImpl(SqlObjectFactory objFac, SqlDatabaseFactory dbFac) {
		this.objFac = objFac;
		this.dbFac = dbFac;
	}

	@Override
	public synchronized void closeModule(TableModule tabMod) throws CasketException {
		TableModuleImpl tabModImpl = this.getTabMod(tabMod);
		if (tabModImpl.isClosed())
			throw CE2.ALREADY_CLOSED.defaultBuild("table module", tabModImpl);
		ModuleConfiguration config = tabModImpl.config();
		Set<TableModuleImpl> allTabMods = this.configModuleMap.get(config);
		boolean changed = allTabMods.remove(tabModImpl);
		try {
			if (allTabMods.isEmpty()) {
				this.dbFac.closeDatabase(tabModImpl.getDatabase());
				this.dbConfigMap.remove(this.configDBMap.get(config));
				this.configDBMap.remove(config);
			}
			tabModImpl.close();
		} catch (Exception exc) {
			if (changed)
				allTabMods.add(tabModImpl);
			throw CasketException.build(exc);
		}
	}

	@Override
	public synchronized void closeAllModules(ModuleConfiguration config) throws CasketException {
		ModuleConfigurationImpl modConf = this.getConfig(config);
		Set<TableModuleImpl> allTabMods = this.configModuleMap.get(modConf);
		if ((allTabMods == null) || allTabMods.isEmpty())
			return;
		SqlDatabase db = this.configDBMap.get(config);

		this.dbFac.closeDatabase(db);

		for (TableModuleImpl tabMod : allTabMods)
			tabMod.close();
		allTabMods.clear();
		this.configDBMap.remove(config);
		this.dbConfigMap.remove(db);
	}

	@Override
	public synchronized ModuleConfiguration createConfiguration() {
		DBConfiguration dbConf = this.dbFac.createConfiguration();
		return new ModuleConfigurationImpl(dbConf);
	}

	@Override
	public synchronized TableModule newTableModule(ModuleConfiguration config) throws CasketException {
		ModuleConfigurationImpl modConf = this.getConfig(config);
		Set<TableModuleImpl> allTabMods = this.configModuleMap.computeIfAbsent(modConf, k -> new HashSet<>());
		SqlDatabase db = this.dbFac.openDatabase(modConf.getConfig());
		this.configDBMap.put(modConf, db);
		this.dbConfigMap.put(db, modConf);
		TableModuleImpl tabMod = new TableModuleImpl(modConf, db, this.objFac);
		allTabMods.add(tabMod);
		return tabMod;

	}

	private ModuleConfigurationImpl getConfig(ModuleConfiguration config) throws CasketException {
		if (config instanceof ModuleConfigurationImpl configImpl)
			return configImpl;
		throw CE4.UNKNOWN_MANAGED_OBJECT.defaultBuild("Configuration", config, this.getClass(), this);
	}

	private TableModuleImpl getTabMod(TableModule tabMod) throws CasketException {
		if (tabMod instanceof TableModuleImpl tabModImpl)
			return tabModImpl;
		throw CE4.UNKNOWN_MANAGED_OBJECT.defaultBuild("Table module", tabMod, this.getClass(), this);
	}

}
