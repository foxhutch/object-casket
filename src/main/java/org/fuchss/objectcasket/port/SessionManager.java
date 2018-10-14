package org.fuchss.objectcasket.port;

/**
 * The manager for {@link Session Sessions}.
 */
public interface SessionManager {
	/**
	 * Get {@link Session} by {@link Configuration}. Mention that same
	 * {@link Configuration Configurations} will get the same (object identity)
	 * {@link Session}.
	 *
	 * @param config
	 *            the configuration
	 * @return the session
	 * @throws ObjectCasketException
	 *             if configuration is invalid
	 */
	Session session(Configuration config) throws ObjectCasketException;

	/**
	 * Terminate a session.
	 *
	 * @param session
	 *            the session
	 * @throws ObjectCasketException
	 *             if not possible
	 */
	void terminate(Session session) throws ObjectCasketException;
}
