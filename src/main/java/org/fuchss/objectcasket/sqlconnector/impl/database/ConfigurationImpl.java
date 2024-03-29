package org.fuchss.objectcasket.sqlconnector.impl.database;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fuchss.objectcasket.common.CasketError.CE2;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.common.Util;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlDialect;

class ConfigurationImpl implements DBConfiguration {

	private Class<? extends Driver> driver;
	private String driverPrefix;

	private String uri = null;
	private String user = null;
	private String passwd = null;
	private SqlCmd sqlCmd = null;
	final List<DBConfiguration.Flag> flags = new ArrayList<>();

	private boolean inUse = false;

	@Override
	public synchronized boolean setDriver(Class<? extends Driver> driver, String driverPrefix, SqlDialect dialect) throws CasketException {
		Util.objectsNotNull(driver, driverPrefix, dialect);
		this.checkNotInUse("set driver");
		this.driver = driver;
		driver.getSimpleName();
		this.driverPrefix = driverPrefix;
		this.sqlCmd = new SqlCmd(dialect);

		return true;
	}

	@Override
	public synchronized boolean setUri(String path) throws CasketException {
		Util.objectsNotNull(path);
		this.checkNotInUse("set URI");
		String oldUri = this.uri;
		URI theUri = new File(path).toURI();
		this.uri = Paths.get(theUri).toString();
		return !this.uri.equals(oldUri);
	}

	@Override
	public synchronized boolean setUser(String name) throws CasketException {
		Util.objectsNotNull(name);
		this.checkNotInUse("set user name");
		String oldUser = this.user;
		this.user = name;
		return !this.user.equals(oldUser);
	}

	@Override
	public synchronized boolean setPassword(String name) throws CasketException {
		Util.objectsNotNull(name);
		this.checkNotInUse("set password");
		String oldPasswd = this.passwd;
		this.passwd = name;
		return !this.passwd.equals(oldPasswd);
	}

	@Override
	public synchronized boolean setFlag(DBConfiguration.Flag... flags) throws CasketException {
		this.checkNotInUse("set flags");
		List<DBConfiguration.Flag> newFlags = new ArrayList<>(Arrays.asList(flags));
		newFlags.removeAll(this.flags);
		return this.flags.addAll(newFlags);
	}

	@Override
	public synchronized boolean removeFlag(DBConfiguration.Flag... flags) throws CasketException {
		this.checkNotInUse("remove flags");
		return this.flags.removeAll(Arrays.asList(flags));
	}

	@Override
	public synchronized boolean containsAll(Flag... flags) throws CasketException {
		return this.flags.containsAll(Arrays.asList(flags));
	}

	@Override
	public synchronized boolean isInUse() {
		return this.inUse;
	}

	@Override
	public synchronized boolean isComplete() {
		return (this.driver != null) && (this.uri != null) && (this.user != null) && (this.passwd != null);
	}

	synchronized void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	String getURL() {
		return this.driverPrefix + this.uri;
	}

	String getURI() {
		return this.uri;
	}

	String getUser() {
		return this.user;
	}

	String getPasswd() {
		return this.passwd;
	}

	boolean allows(DBConfiguration.Flag flag) {
		return this.flags.contains(flag);
	}

	private void checkNotInUse(String action) throws CasketException {
		if (this.inUse)
			throw CE2.CONFIGURATION_IN_USE.defaultBuild(action, this);
	}

	public SqlCmd getSqlCmd() {
		return this.sqlCmd;
	}

}
