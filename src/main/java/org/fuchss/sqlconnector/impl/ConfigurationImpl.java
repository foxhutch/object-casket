package org.fuchss.sqlconnector.impl;

import java.io.File;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.fuchss.sqlconnector.port.Configuration;
import org.fuchss.sqlconnector.port.ConnectorException;

public class ConfigurationImpl implements Configuration {
	private Class<? extends Driver> driver;
	private String driverName;

	private String uri = null;
	private String user = null;
	private String passwd = null;
	private List<Configuration.Flag> flags = null;

	private boolean inUse = false;

	@Override
	public boolean setDriver(Class<? extends Driver> driver, String driverName) throws ConnectorException {
		Objects.requireNonNull(driver);
		Objects.requireNonNull(driverName);
		this.checkInUse();
		if (this.initialized()) {
			return driver.equals(this.driver) && driverName.equals(this.driverName);
		}
		this.driver = driver;
		driver.getSimpleName();
		this.driverName = driverName;
		return true;
	}

	@Override
	public synchronized boolean setUri(String path) throws ConnectorException {
		this.checkInUse();
		String canonicalPath = null;
		if ((path != null) && !this.initialized()) {
			this.uri = "" + (canonicalPath = new File(path).toURI().getPath());
		}
		return (canonicalPath != null) ? canonicalPath.equals(this.uri) : false;
	}

	@Override
	public synchronized boolean setUser(String name) throws ConnectorException {
		this.checkInUse();
		if ((name != null) && !this.initialized()) {
			this.user = "" + name;
		}
		return (name != null) ? name.equals(this.user) : false;
	}

	@Override
	public synchronized boolean setPasswd(String name) throws ConnectorException {
		this.checkInUse();
		if ((name != null) && !this.initialized()) {
			this.passwd = "" + name;
		}
		return (name != null) ? name.equals(this.passwd) : false;
	}

	@Override
	public synchronized boolean setFlag(Configuration.Flag... flags) throws ConnectorException {
		this.checkInUse();
		if ((flags != null) && !this.initialized()) {
			this.flags = new ArrayList<>(Arrays.asList(flags));
			return true;
		}
		return false;
	}

	void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	boolean initialized() {
		return (this.driver != null) && (this.driverName != null) && (this.uri != null) && (this.user != null) && (this.passwd != null) && (this.flags != null);
	}

	String getURL() {
		return this.driverName + this.uri;
	}

	String getURI() {
		return "" + this.uri;
	}

	String getUser() {
		return "" + this.user;
	}

	String getPasswd() {
		return "" + this.passwd;
	}

	boolean allows(Configuration.Flag flag) {
		return (this.flags != null) ? this.flags.contains(flag) : false;
	}

	private void checkInUse() throws ConnectorException {
		if (this.inUse) {
			ConfigurationException.Error.ConfigurationInUse.build();
		}
	}

	private static class ConfigurationException extends ConnectorException {

		private static final long serialVersionUID = 1L;

		private ConfigurationException(Error error, String... arg) {
			super(error.format(arg));
		}

		static enum Error {

			ConfigurationInUse("Modification impossible, configuration in use, create an other one.");

			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ConnectorException {
				ConnectorException.build(new ConfigurationException(this, arg));
			}

		}
	}

}
