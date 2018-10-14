package org.fuchss.objectcasket.impl;

import java.sql.Driver;
import java.util.ArrayList;

import org.fuchss.objectcasket.port.Configuration;
import org.fuchss.objectcasket.port.ObjectCasketException;

public class ConfigurationAdapter implements Configuration {
	org.fuchss.sqlconnector.port.Configuration theConfiguration;

	ConfigurationAdapter(org.fuchss.sqlconnector.port.Configuration config) {
		this.theConfiguration = config;
	}

	@Override
	public boolean setDriver(Class<? extends Driver> driver, String driverName) throws ObjectCasketException {
		try {
			return this.theConfiguration.setDriver(driver, driverName);
		} catch (Exception exc) {
			ObjectCasketException.build(exc);
		}
		return false;
	}

	@Override
	public boolean setUri(String uri) throws ObjectCasketException {
		try {
			return this.theConfiguration.setUri(uri);
		} catch (Exception exc) {
			ObjectCasketException.build(exc);
		}
		return false;
	}

	@Override
	public boolean setUser(String name) throws ObjectCasketException {
		try {
			return this.theConfiguration.setUser(name);
		} catch (Exception exc) {
			ObjectCasketException.build(exc);
		}
		return false;

	}

	@Override
	public boolean setPasswd(String name) throws ObjectCasketException {
		try {
			return this.theConfiguration.setPasswd(name);
		} catch (Exception exc) {
			ObjectCasketException.build(exc);
		}
		return false;

	}

	@Override
	public boolean setFlag(Flag... flags) throws ObjectCasketException {
		try {
			org.fuchss.sqlconnector.port.Configuration.Flag[] tf = new org.fuchss.sqlconnector.port.Configuration.Flag[2];
			ArrayList<org.fuchss.sqlconnector.port.Configuration.Flag> targetFlags = new ArrayList<>();
			for (int i = 0; i < flags.length; i++) {
				targetFlags.add(org.fuchss.sqlconnector.port.Configuration.Flag.values()[flags[i].ordinal()]);
			}
			return this.theConfiguration.setFlag(targetFlags.toArray(tf));
		} catch (Exception exc) {
			ObjectCasketException.build(exc);
		}
		return false;
	}

	org.fuchss.sqlconnector.port.Configuration getConfigruation() {
		return this.theConfiguration;
	}

}
