package org.fuchss.objectcasket.objectpacker.impl;

import java.sql.Driver;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError.CE2;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.port.Configuration;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;

class ConfigurationImpl implements Configuration {

	private static final Map<Flag, ModuleConfiguration.Flag> flagMap = new EnumMap<>(Configuration.Flag.class);
	static {
		flagMap.put(Configuration.Flag.CREATE, ModuleConfiguration.Flag.CREATE);
		flagMap.put(Configuration.Flag.ALTER, ModuleConfiguration.Flag.CREATE);
		flagMap.put(Configuration.Flag.WRITE, ModuleConfiguration.Flag.MODIFY);
	}

	private final ModuleConfiguration config;
	private final Set<Flag> flags = new HashSet<>();

	ConfigurationImpl(ModuleConfiguration config) {
		this.config = config;
	}

	ModuleConfiguration getConfig() {
		return this.config;
	}

	@Override
	public synchronized boolean setDriver(Class<? extends Driver> driver, String driverPrefix, SqlDialect dialect) throws CasketException {
		return this.config.setDriver(driver, driverPrefix, dialect);
	}

	@Override
	public synchronized boolean setUri(String uri) throws CasketException {
		return this.config.setUri(uri);
	}

	@Override
	public synchronized boolean setUser(String name) throws CasketException {
		return this.config.setUser(name);
	}

	@Override
	public synchronized boolean setPassword(String passwd) throws CasketException {
		return this.config.setPassword(passwd);
	}

	@Override
	public synchronized boolean setFlag(Configuration.Flag... flags) throws CasketException {
		this.checkNotInUse(String.format("set flags %s", Arrays.toString(flags)));
		Set<Flag> newFlags = new HashSet<>(Arrays.asList(flags));
		newFlags.removeAll(this.flags);
		Set<ModuleConfiguration.Flag> newModFlags = new HashSet<>();
		for (Flag flag : newFlags) {
			ModuleConfiguration.Flag tabModFlag = flagMap.get(flag);
			if (tabModFlag != null)
				newModFlags.add(tabModFlag);
		}
		if (!newModFlags.isEmpty()) {

			ModuleConfiguration.Flag[] modFlags = new ModuleConfiguration.Flag[newModFlags.size()];
			newModFlags.toArray(modFlags);
			this.config.setFlag(modFlags);
		}
		return this.flags.addAll(newFlags);
	}

	@Override
	public synchronized boolean removeFlag(Configuration.Flag... flags) throws CasketException {
		this.checkNotInUse(String.format("remove flags %s", Arrays.toString(flags)));
		Set<Flag> remainingFlags = new HashSet<>(this.flags);
		boolean changed = remainingFlags.removeAll(Arrays.asList(flags));
		if (!changed)
			return false;
		this.flags.clear();
		Flag[] remainingFlagsAsArray = new Flag[remainingFlags.size()];
		remainingFlags.toArray(remainingFlagsAsArray);
		return this.setFlag(remainingFlagsAsArray);
	}

	@Override
	public synchronized boolean containsAll(Configuration.Flag... flags) throws CasketException {
		Set<Flag> flagsToCheck = new HashSet<>(Arrays.asList(flags));
		if (flagsToCheck.isEmpty())
			return false;
		flagsToCheck.removeAll(this.flags);
		return flagsToCheck.isEmpty();
	}

	private void checkNotInUse(String action) throws CasketException {
		if (this.config.inUse())
			throw CE2.CONFIGURATION_IN_USE.defaultBuild(action, this.config);
	}

}
