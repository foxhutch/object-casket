package org.fuchss.objectcasket.tablemodule;

import org.fuchss.objectcasket.tablemodule.port.TableModuleFactory;

/**
 * The access point to interact with abstract representations of databases.
 * Through the static {@link ModulePort#PORT} object one obtains the
 * {@link TableModuleFactory} to create, modify, and access the tables inside a
 * database.
 */

public interface ModulePort {

	/**
	 * Via this port one can access the table module component.
	 */
	ModulePort PORT = new TabModImpl();

	/**
	 * This operation returns the {@link TableModuleFactory} the main object to
	 * create an abstraction of a database.
	 *
	 * @return The factory
	 */
	TableModuleFactory tableModuleFactory();

}
