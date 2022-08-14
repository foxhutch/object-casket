package org.fuchss.objectcasket.tablemodule.port;

import org.fuchss.objectcasket.common.CasketException;

/**
 * By this factory one can create {@link TableModule table modules},
 * representations of databases.
 *
 *
 */
public interface TableModuleFactory {

	/**
	 * This operation closes the {@link TableModule}. So no one can read and write
	 * any more. If the underling {@link ModuleConfiguration} has no more modules
	 * then the connection to the database is also closed.
	 *
	 * @param tabMod
	 *            - the table module.
	 * @throws CasketException
	 *             on error.
	 */
	void closeModule(TableModule tabMod) throws CasketException;

	/**
	 * This operation closes all {@link TableModule TableModules} which are build by
	 * the given {@link ModuleConfiguration}. So the underling database will be
	 * closed too.
	 *
	 * @param config
	 *            - the module configuration.
	 * @throws CasketException
	 *             on error.
	 */
	void closeAllModules(ModuleConfiguration config) throws CasketException;

	/**
	 * This operation creates a {@link ModuleConfiguration} so one can build new
	 * {@link TableModule TableModules}.
	 *
	 * @return a new {@link ModuleConfiguration}.
	 */
	ModuleConfiguration createConfiguration();

	/**
	 * This operation creates a new {@link TableModule} by a
	 * {@link ModuleConfiguration}. Mention, using the same
	 * {@link ModuleConfiguration ModulConfigurations} twice will get different
	 * {@link TableModule TableModules}. But all these modules work on the same
	 * underling database.
	 *
	 * @param config
	 *            - the module configuration.
	 * @return the new table module.
	 * @throws CasketException
	 *             on error.
	 */
	TableModule newTableModule(ModuleConfiguration config) throws CasketException;

}
