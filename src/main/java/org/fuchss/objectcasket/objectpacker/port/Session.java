package org.fuchss.objectcasket.objectpacker.port;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.tablemodule.port.TableModule;

/**
 * A session of the Object Casket system. Depending to the
 * {@link Configuration.Flag flags} set in the {@link Configuration
 * configuration} it is possible to have multiple sessions on the same database.
 *
 * @see SessionManager
 * @see SessionObserver
 * @see Configuration
 * @see Configuration.Flag
 */
public interface Session {
	/**
	 * This operation maps existing database tables to user-defined classes. These
	 * classes must be final and marked as an{@link Entity}. It is possible to use a
	 * class with fewer attributes than columns in the database table. At least an
	 * attribute for the primary key and the default constructor must exist. The
	 * attribute for the primary key should be marked as {@link Id @Id}. All
	 * primitive Java types and also their corresponding classes can be used for a
	 * primary key. If the primary key corresponds to the Java class
	 * {@link Integer}, it is possible to generate the key automatically. In this
	 * case the attribute must be annotated as
	 * ({@link GeneratedValue @GeneratedValue } additionally.
	 *
	 * <p>
	 * Attributes with standard Java types are mapped to columns as follows:
	 *
	 * <p>
	 * <strong> boolean </strong>, <strong> byte </strong>, <strong> short
	 * </strong>, <strong> int </strong>, <strong> long </strong>, <strong> Boolean
	 * </strong>, <strong> Byte </strong>, <strong> Short </strong>, <strong>
	 * Integer </strong>, <strong> Long </strong>, and <strong> Date </strong> are
	 * mapped to <strong> SQL:INTEGER</strong>. So it is possible to use this Java
	 * types to access any row of storage class INTEGER. Casts are done implicitly.
	 *
	 * <p>
	 * <strong>double</strong>, <strong>float</strong>, <strong>Double</strong>,
	 * <strong>Float</strong> are mapped to <strong>SQL:REAL</strong>. So it is
	 * possible to use this Java types to access any row of storage class REAL.
	 * Casts are done implicitly.
	 *
	 * <p>
	 * <strong>char</strong>, <strong>Character</strong>, and
	 * <strong>String</strong> are mapped to <strong>SQL:TEXT</strong>. So it is
	 * possible to use this Java types to access any row of storage class REAL.
	 * Casts are done implicitly.
	 *
	 * <p>
	 * All other {@link Serializable serializable} Java classes are mapped to
	 * <strong>SQL:Blobs</strong>. If one assigns a blob to a serializable Java
	 * class this class must be {@link Class#isAssignableFrom(Class) assignable}
	 * from the original Java class of the object stored inside the blob.
	 *
	 * <p>
	 * Beside these kind of attributes, which are simply stored in columns, one can
	 * also use two <code> jakarta.persistence </code> annotations to access other
	 * stored objects, objects from associated classes.
	 *
	 * <p>
	 * {@link ManyToOne @ManyToOne} to access a single object. This represents a
	 * non-navigable UML-like many-to-one association. <code> A n-x--->1 B </code>
	 *
	 * <p>
	 * {@link ManyToMany @ManyToMany} to access multiple objects. This represents a
	 * non-navigable UML-like many-to-many association. <code> A n-x--->m B </code>
	 * This annotation is valid only if the annotated attribute is declared as a
	 * {@link Set} with an initially existing container. <strong> Both classes
	 * should be declared at the same time, or B before A.</strong>
	 *
	 * <p>
	 * Attributes annotated with {@link Transient @Transient} are ignored. To
	 * individualize the mapping, it is possible to use the annotation
	 * {@link Column @Column} to define an individual name. This is also possible
	 * for the entity itself by using the {@link Table @Table} annotation.
	 *
	 * <pre>
	 * &#64;Entity()
	 * public final class Club {
	 *
	 * 	&#64;Id
	 * 	&#64;GeneratedValue
	 * 	private Integer id;
	 *
	 * 	private String name;
	 *
	 * 	private String address;
	 *
	 * 	&#64;ManyToOne
	 * 	private Member president;
	 *
	 * 	&#64;ManyToOne
	 * 	private Member vicePresident;
	 *
	 * 	&#64;ManyToMany
	 * 	Set{@literal<}Member{@literal>} members = new HashSet{@literal<}{@literal>}();
	 *
	 * 	&#64;ManyToMany
	 * 	Set{@literal<}Department{@literal>} departments = new HashSet{@literal<}{@literal>}();
	 *
	 * 	&#64;SuppressWarnings("unused")
	 * 	private Club() { }
	 *
	 * 	public Club(String name) {
	 * 		this.name = name;
	 *    }
	 * }
	 * </pre>
	 *
	 * @param clazz
	 *            - the classes.
	 * @throws CasketException
	 *             if a class is not a proper entity or no assignable database table
	 *             exists.
	 */
	void declareClass(Class<?>... clazz) throws CasketException;

	/**
	 * This operation opens a new transaction.
	 *
	 * @throws CasketException
	 *             if a transaction is already running.
	 */

	void beginTransaction() throws CasketException;

	/**
	 * This operation closes the transaction and commits all outstanding actions.
	 *
	 * @throws CasketException
	 *             if no transaction is running.
	 */
	void endTransaction() throws CasketException;

	/**
	 * This operation retrieves all objects of a given class.
	 *
	 * @param <T>
	 *            - the class type.
	 * @param clazz
	 *            - the class.
	 * @return a set of objects.
	 * @throws CasketException
	 *             on error. When called within a transaction, the transaction will
	 *             be closed and a {@link TableModule#rollback(Object) rollback}
	 *             will be performed.
	 */
	<T> Set<T> getAllObjects(Class<T> clazz) throws CasketException;

	/**
	 * This operation retrieves all objects matching the set of {@link Session.Exp
	 * expressions}.
	 *
	 * @param <T>
	 *            - the class type.
	 * @param clazz
	 *            - the class.
	 * @param args
	 *            - a set of {@link Session.Exp expressions} (attribute name,
	 *            comparator, value).
	 * @return a set of objects.
	 * @throws CasketException
	 *             on error. When called within a transaction, the transaction will
	 *             be closed and a {@link TableModule#rollback(Object) rollback}
	 *             will be performed.
	 */
	<T> Set<T> getObjects(Class<T> clazz, Set<Exp> args) throws CasketException;

	/**
	 * These operations persist / save an object of a previous assigned class.
	 * ({@link Session#declareClass(Class...)}).
	 *
	 * @param <T>
	 *            - the type of the object.
	 * @param obj
	 *            - the object.
	 * @throws CasketException
	 *             on error. When called within a transaction, the transaction will
	 *             be closed and a {@link TableModule#rollback(Object) rollback}
	 *             will be performed.
	 */
	<T> void persist(T obj) throws CasketException;

	/**
	 * This operation reloads the content of a database and restores all managed
	 * objects.
	 *
	 * @throws CasketException
	 *             on error. When called within a transaction, the transaction will
	 *             be closed and a {@link TableModule#rollback(Object) rollback}
	 *             will be performed.
	 */
	void resync() throws CasketException;

	/**
	 * This operation reloads the content of the given object from the databases. If
	 * multiple sessions exists ({@link Configuration.Flag#SESSIONS }), this was
	 * done automatically if the database content was update within another session.
	 *
	 * @param <T>
	 *            - the type of the object.
	 * @param obj
	 *            - the object to reload.
	 * @throws CasketException
	 *             on error. When called within a transaction, the transaction will
	 *             be closed and a {@link TableModule#rollback(Object) rollback}
	 *             will be performed.
	 */
	<T> void resync(T obj) throws CasketException;

	/**
	 * This operation deletes an object from a database. This is only possible, if
	 * this object is not used within a many-to-one or many-to-many association.
	 *
	 * @param <T>
	 *            - the type of the object.
	 * @param obj
	 *            - the object.
	 * @throws CasketException
	 *             on error. When called within a transaction, the transaction will
	 *             be closed and a {@link TableModule#rollback(Object) rollback}
	 *             will be performed.
	 */
	<T> void delete(T obj) throws CasketException;

	/**
	 * To observe changes done by the auto sync mechanism it is possible to register
	 * a {@link SessionObserver}.
	 *
	 * @param obs
	 *            - the session observer to add.
	 * @return true iff the observer is newly assigned.
	 * @see Session#resync(Object)
	 */
	boolean register(SessionObserver obs);

	/**
	 * This operation removes an existing {@link SessionObserver}.
	 *
	 * @param obs
	 *            - the session observer to remove.
	 * @return true if the observer was removed and the set of observers has
	 *         changed.
	 * @see Session#register(SessionObserver obs)
	 */
	boolean deregister(SessionObserver obs);

	/**
	 * An expression use in {@link Session#getObjects(Class, Set)} is a triple.
	 * Where the first element is the name of the attribute (@link
	 * java.lang.reflect.Field#getName() ), the second element is a string
	 * representing the comparison operation
	 * {@literal ("=", "<" , ">", "<=", ">=", "!=")}, and the third element is the
	 * value to which the attribute is compared.
	 *
	 * @param fieldName
	 *            - the name of the attribute.
	 * @param op
	 *            - the comparison operation.
	 * @param value
	 *            - the value to which the attribute is compared.
	 * @see Session#getObjects(Class, Set)
	 */
	record Exp(String fieldName, String op, Serializable value) {
	}

}
