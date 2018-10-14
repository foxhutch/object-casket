package org.fuchss.objectcasket;

import org.fuchss.objectcasket.port.ObjectCasketPort;

/**
 * Factory for the {@link ObjectCasketPort}.
 */
public interface ObjectCasketFactory {
	/**
	 * The one and only factory.
	 */
	ObjectCasketFactory FACTORY = new ObjectCasketFactoryImpl();

	/**
	 * Get a {@link ObjectCasketPort}.
	 *
	 * @return the {@link ObjectCasketPort}
	 */
	ObjectCasketPort ObjectCasketPort();
}
