package org.fuchss.objectcasket.sqlconnector.port;

import org.fuchss.objectcasket.sqlconnector.port.SqlArg.CMP;
import org.fuchss.objectcasket.sqlconnector.port.SqlArg.OP;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature.Flag;

/**
 * By implementing the SqlDialect interface, it is possible to customize the
 * Object Casket System to work with a specific SQL database implementation.
 * Typically, SQL dialects differ in the naming of data types and annotations
 * for column definitions.
 * <p>
 * Currently, two dialects are predefined {@link DialectH2} and
 * {@link DialectSqlite}.
 */
public interface SqlDialect {

	/**
	 * Methods implementing this operation should return string representations of
	 * the given comparison operator.
	 *
	 * @param cmp - the {@link CMP comparator}.
	 * @return the dialect specific representation of the comparator.
	 */
	String cmpString(CMP cmp);

	/**
	 * Methods implementing this operation should return string representations of
	 * the given boolean operator.
	 *
	 * @param op - the {@link OP operator}.
	 * @return the dialect specific name of an operator.
	 */
	String operatorString(OP op);

	/**
	 * Methods implementing this operation should return string representations of
	 * the given {@link Flag annotation}.
	 *
	 * @param flag - the {@link Flag flag}.
	 * @return the dialect specific name of the flag.
	 */
	String flagString(Flag flag);

	/**
	 * Methods implementing this operations should realize the mapping between
	 * Object Casket StorageClasses and dialect specific SQL types representing this
	 * classes.
	 *
	 * @param stClass - the Object Casket {@link StorageClass}.
	 * @return the dialect specific name of the SQL type.
	 *
	 * <p>
	 * E.g. StorageClass.TEXT {@literal ->} "VARCHAR(1000000000)"
	 */
	String storageClassString(StorageClass stClass);

	/**
	 * Methods implementing this operation should realize the mapping between
	 * dialect specific SQL types and their corresponding Object Casket
	 * StorageClasses. Mention: The strings representing the SQL types are driver
	 * and dialect specific and can differ from the strings used in the reverse
	 * mapping ({@link SqlDialect#storageClassString(StorageClass)}.
	 *
	 * @param typeName - the dialect specific name of the SQL type.
	 * @return the abstract {@link StorageClass SQL type}.
	 *
	 * <p>
	 * E.g. "CHARACTER VARYING" {@literal ->} StorageClass.TEXT
	 */
	StorageClass baseTypeStorageClass(String typeName);

}
