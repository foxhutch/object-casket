package org.fuchss.objectcasket.objectpacker;

import org.fuchss.objectcasket.objectpacker.port.SessionManager;

/**
 * The main access point to work with the Object Casket system. Via the static
 * {@link PackerPort#PORT} one can access the {@link SessionManager session
 * manager} the central object to work with the Object Casket system.
 *
 *
 */
public interface PackerPort {

	/**
	 * Via this port one can access the {@link SessionManager session manager} the
	 * central object to work with the Object Casket system.
	 */
	PackerPort PORT = new ObjectPackerImpl();

	/**
	 * This operation returns the {@link SessionManager session manager} the central
	 * object to work with the Object Casket system.
	 *
	 * @return the {@link SessionManager session manager}.
	 */
	SessionManager sessionManager();

}
