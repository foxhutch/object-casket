package org.fuchss.objectcasket.port;

import java.util.Map;
import java.util.Set;

/**
 * The session of the Object Casket system.
 *
 */
public interface Session {
	/**
	 * Register class to session.
	 *
	 * @param clazz
	 *            the class to register
	 * @throws ObjectCasketException
	 *             if class is invalid
	 */
	void declareClass(Class<?>... clazz) throws ObjectCasketException;

	/**
	 * Open session to work with Objects.
	 *
	 * @throws ObjectCasketException
	 *             if not possible
	 * @see SessionManager#terminate(Session)
	 */
	void open() throws ObjectCasketException;

	/**
	 * Get all Objects by class.
	 *
	 * @param <T>
	 *            the class type
	 * @param clazz
	 *            the class
	 * @return a set of objects
	 * @throws ObjectCasketException
	 *             if not possible
	 */
	<T> Set<T> getAllObjects(Class<T> clazz) throws ObjectCasketException;

	/**
	 * Get all objects by prototype.
	 *
	 * @param <T>
	 *            the class type
	 * @param prototype
	 *            the prototype with values for comparison
	 * @param cmps
	 *            the comparison definitions (attribute name -&gt; compare
	 *            operation)
	 * @return a set of objects
	 * @throws ObjectCasketException
	 *             if not possible
	 */
	<T> Set<T> getObjectsByPrototype(T prototype, Map<String, ObjectCasketCMP> cmps) throws ObjectCasketException;

	/**
	 * Get join table entity.
	 *
	 * @param me
	 *            the current object
	 * @param remote
	 *            the remote object
	 * @param joinTableName
	 *            the join table's name
	 * @return the join table's entity
	 * @throws ObjectCasketException
	 *             if not possible
	 */
	// Object joinTableEntity(Object me, Object remote, String joinTableName) throws
	// ObjectCasketException;
	<T> T joinTableEntity(Object me, Object remote, Class<T> joinTableClass) throws ObjectCasketException;

	/**
	 * Persist / Save an object.
	 *
	 * @param obj
	 *            the object
	 * @throws ObjectCasketException
	 *             if not possible
	 */
	void persist(Object obj) throws ObjectCasketException;

	/**
	 * Delete object from database
	 *
	 * @param obj
	 *            the object
	 * @throws ObjectCasketException
	 *             if not possible
	 */
	void delete(Object obj) throws ObjectCasketException;

}
