package org.fuchss.objectcasket.tablemodule.impl;

import java.sql.Driver;
import java.util.EnumMap;
import java.util.Map;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;

class ModuleConfigurationImpl implements ModuleConfiguration {

	private DBConfiguration config;

	private static Map<Flag, DBConfiguration.Flag> flagMap = new EnumMap<>(ModuleConfiguration.Flag.class);
	static {
		flagMap.put(ModuleConfiguration.Flag.CREATE, DBConfiguration.Flag.CREATE);
		flagMap.put(ModuleConfiguration.Flag.MODIFY, DBConfiguration.Flag.MODIFY);
	}

	public ModuleConfigurationImpl(DBConfiguration config) {
		this.config = config;
	}

	public DBConfiguration getConfig() {
		return this.config;
	}

	@Override
	public boolean setDriver(Class<? extends Driver> driver, String driverPrefix, SqlDialect dialect) throws CasketException {
		return this.config.setDriver(driver, driverPrefix, dialect);

	}

	@Override
	public boolean setUri(String uri) throws CasketException {

		return this.config.setUri(uri);

	}

	@Override
	public boolean setUser(String name) throws CasketException {

		return this.config.setUser(name);

	}

	@Override
	public boolean setPassword(String name) throws CasketException {

		return this.config.setPassword(name);

	}

	@Override
	public boolean setFlag(Flag... flags) throws CasketException {
		DBConfiguration.Flag[] dbFlags = new DBConfiguration.Flag[flags.length];
		for (int idx = 0; idx < dbFlags.length; idx++)
			dbFlags[idx] = flagMap.get(flags[idx]);

		return this.config.setFlag(dbFlags);

	}

	@Override
	public boolean removeFlag(Flag... flags) throws CasketException {
		DBConfiguration.Flag[] dbFlags = new DBConfiguration.Flag[flags.length];
		for (int idx = 0; idx < dbFlags.length; idx++)
			dbFlags[idx] = flagMap.get(flags[idx]);

		return this.config.removeFlag(dbFlags);

	}

	@Override
	public boolean containsAll(Flag... flags) throws CasketException {
		DBConfiguration.Flag[] dbFlags = new DBConfiguration.Flag[flags.length];
		for (int idx = 0; idx < dbFlags.length; idx++)
			dbFlags[idx] = flagMap.get(flags[idx]);

		return this.config.containsAll(dbFlags);

	}

	@Override
	public boolean inUse() {
		return this.config.isInUse();
	}
}
