package org.fuchss.tablemodule;

import org.fuchss.tablemodule.port.ModulePort;

/**
 * Factory for the {@link ModulePort}.
 */
public interface TableModuleFactory {
	/**
	 * The one and only factory.
	 */
	TableModuleFactory FACTORY = new TableModuleFactoryImpl();

	/**
	 * Get a {@link ModulePort}.
	 *
	 * @return the {@link ModulePort}
	 */
	ModulePort modulePort();

}
