package org.fuchss.objectcasket.objectpacker.port;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.PackerPort;

/**
 * The session manager is the central object to work with the Object Casket
 * system. It can be obtained via the {@link PackerPort} calling
 * {@link PackerPort#sessionManager()} on the static attribute
 * {@link PackerPort#PORT}. By default it is possible to work with different
 * configurations simultaneously. If {@link Configuration.Flag#SESSIONS} is set
 * one can also work with multiple sessions based on the same
 * {@link Configuration configuration} concurrently. In this case Object Casket
 * synchronizes these sessions in the background.
 *
 * @see PackerPort
 *
 *
 */
public interface SessionManager {

	/**
	 * This operation creates a new and empty {@link Configuration}. After all
	 * informations to access the underlying database are added one can use this
	 * configuration to work with the configured database.
	 *
	 * @return a new {@link Configuration}
	 *
	 * @see SessionManager#editDomain(Configuration)
	 * @see SessionManager#mkDomain(Configuration)
	 * @see SessionManager#session(Configuration)
	 * @see SessionManager#terminateAll(Configuration)
	 */
	Configuration createConfiguration();

	/**
	 * This process creates a new and empty domain as well as an empty database
	 * according to the{@link Configuration}.
	 *
	 *
	 * @param config
	 *            - the configuration.
	 * @return the domain.
	 * @throws CasketException
	 *             if configuration is invalid or the database already exists.
	 */
	Domain mkDomain(Configuration config) throws CasketException;

	/**
	 * This operation opens an existing domain (database). So the domain can be
	 * modified.
	 *
	 *
	 * @param config
	 *            - the configuration.
	 * @return the domain.
	 * @throws CasketException
	 *             if configuration is invalid or the database is already in use.
	 */
	Domain editDomain(Configuration config) throws CasketException;

	/**
	 * This operation adds classes (entities) to an open or newly created domain by
	 * adding the necessary tables inside the database.
	 *
	 * <p>
	 * All Classes must be final and marked as {@link Entity @Entity}. At least one
	 * attribute for the primary key and a default constructor must exists. This
	 * attribute should be marked as {@link Id @Id}. For a primary key all primitive
	 * Java types and also their corresponding classes can be used. If the primary
	 * key corresponds to the Java class {@link Integer} it is possible to generate
	 * the key automatically. In this case the attribute must be annotated by
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
	 * Beside these kind of attributes, which are simply stored in columns one can
	 * also use two <code> javax.persistence </code> annotations to access other
	 * stored objects, objects from associated classes.
	 *
	 * <p>
	 * {@link ManyToOne @ManyToOne} to access a single object. This represents a non
	 * navigable UML-like many-to-one association. <code> A n-x--->1 B </code>
	 *
	 * <p>
	 * {@link ManyToMany @ManyToMany} to access multiple objects. This represents a
	 * non navigable UML-like many-to-many association. <code> A n-x--->m B </code>
	 * This annotation is only valid if the annotated attributes a {@link Set} with
	 * an existing container.<strong> Both classes should be added at the same time,
	 * or B before A.</strong>
	 *
	 * <p>
	 * Attribute annotated with {@link Transient @Transient} are ignored. To
	 * individualize the mapping, it is possible to use the {@link Column @Column}
	 * annotation to define an individual name. This is also possible for the entity
	 * itself by using {@link Table @Table} annotation.<strong> Both classes should
	 * be added at the same time, or B before A.</strong>
	 *
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
	 * 	Set{@literal<}Section{@literal>} sections = new HashSet{@literal<}{@literal>}();
	 *
	 * 	&#64;SuppressWarnings("unused")
	 * 	private Club() { }
	 *
	 * 	public Club(String name) {
	 * 		this.name = name;
	 * 	}
	 * }
	 * </pre>
	 *
	 *
	 * @param domain
	 *            - the domain.
	 * @param clazz
	 *            - the classes.
	 * @throws CasketException
	 *             if a class is invalid, a session is running or the configuration
	 *             has not enough rights.
	 * @throws InterruptedException
	 *             if interrupted.
	 *
	 * @see SessionManager#editDomain(Configuration)
	 * @see SessionManager#mkDomain(Configuration)
	 */

	void addEntity(Domain domain, Class<?>... clazz) throws CasketException, InterruptedException;

	/**
	 * By this method the assigned table inside the database is modified according
	 * to the new class definitions. Attributes are removed or added. So the new
	 * class definition were adopted. Be careful this operation cannot be undone.
	 * Maybe you are lucky if you get an exception maybe not.
	 *
	 * @param domain
	 *            - the domain.
	 * @param clazz
	 *            - the classes to modify.
	 * @throws CasketException
	 *             if class is invalid, did not exists, is in use, or the session
	 *             has not enough rights.
	 */
	default void alterEntity(Domain domain, Class<?>... clazz) throws CasketException {
		throw new UnsupportedOperationException("alterEntity not yet realized");
	}

	/**
	 * This finalize the building or modification process of the domain. The
	 * assigned database can now be use in a {@link Session}.
	 *
	 * @param domain
	 *            - the domain to finalize.
	 * @throws CasketException
	 *             if the domain is invalid or already finalized.
	 * @throws InterruptedException
	 *             if interrupted.
	 */
	void finalizeDomain(Domain domain) throws CasketException, InterruptedException;

	/**
	 * Get {@link Session} by {@link Configuration}. Mention that a second call with
	 * the same {@link Configuration Configurations} will get another {@link Session
	 * session} if the flag {@link Configuration.Flag#SESSIONS} is set, otherwise
	 * one will get the same session (object identity).
	 *
	 *
	 * @param config
	 *            - the configuration.
	 * @return the session.
	 * @throws CasketException
	 *             if configuration is invalid.
	 */
	Session session(Configuration config) throws CasketException;

	/**
	 * Terminate a session.
	 *
	 * @param session
	 *            - the session.
	 * @throws CasketException
	 *             on error.
	 * @throws InterruptedException
	 *             if interrupted.
	 */
	void terminate(Session session) throws CasketException, InterruptedException;

	/**
	 * Terminate all sessions based on the specific configuration.
	 *
	 * @param config
	 *            - the configuration.
	 * @throws CasketException
	 *             on error.
	 * @throws InterruptedException
	 *             if interrupted.
	 */
	void terminateAll(Configuration config) throws CasketException, InterruptedException;

	/**
	 * Terminate all sessions independently.
	 *
	 * @throws CasketException
	 *             on error.
	 * @throws InterruptedException
	 *             if interrupted.
	 */
	void terminateAll() throws CasketException, InterruptedException;
}
