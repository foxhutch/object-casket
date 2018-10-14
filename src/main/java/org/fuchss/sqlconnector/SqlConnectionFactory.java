package org.fuchss.sqlconnector;

import org.fuchss.sqlconnector.port.SqlPort;

/**
 * Factory for the {@link SqlPort}.
 *
 */
public interface SqlConnectionFactory {
	/**
	 * The one and only factory.
	 */
	SqlConnectionFactory FACTORY = new SqlConnectionFactoryImpl();

	/**
	 * Get a {@link SqlPort}.
	 *
	 * @return the {@link SqlPort}
	 */
	SqlPort sqlPort();
}
