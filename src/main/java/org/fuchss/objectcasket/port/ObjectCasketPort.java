package org.fuchss.objectcasket.port;

/**
 * The port to the Object Casket system.
 *
 */
public interface ObjectCasketPort {
	/**
	 * Get a {@link ConfigurationBuilder}.
	 *
	 * @return a {@link ConfigurationBuilder}
	 */
	ConfigurationBuilder configurationBuilder();

	/**
	 * Get a {@link SessionManager}.
	 *
	 * @return a {@link SessionManager}
	 */
	SessionManager sessionManager();
}
