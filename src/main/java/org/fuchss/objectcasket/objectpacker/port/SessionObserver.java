package org.fuchss.objectcasket.objectpacker.port;

/**
 * To avoid magic surprises, it is possible to assign a SessionObserver to any
 * {@link Session}. These observers will be informed if managed objects changed
 * in background by some concurrent session.
 *
 * @see Session#resync(Object)
 * @see Session#register(SessionObserver)
 * @see Session#deregister(SessionObserver)
 */

public interface SessionObserver {

	/**
	 * @param deleted -a managed object which was deleted in another session.
	 * @see Session#delete(Object)
	 */
	void externDeleted(Object deleted);

	/**
	 * @param changed - a managed object which was modified in another session.
	 * @see Session#persist(Object)
	 */
	void externChanged(Object changed);

}
