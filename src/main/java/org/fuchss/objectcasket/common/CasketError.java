package org.fuchss.objectcasket.common;

import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.sqlconnector.port.DBConfiguration;
import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.sqlconnector.port.SqlDatabase;
import org.fuchss.objectcasket.sqlconnector.port.TableAssignment;
import org.fuchss.objectcasket.tablemodule.port.ModuleConfiguration;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.TableModule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Object Casket error codes
 */
public enum CasketError {

	/**
	 * A closable object is already closed.
	 */
	ALREADY_CLOSED,

	/**
	 * The class is already declared. Don't declare twice.
	 */
	CLASS_ALREADY_DECLARED, //

	/**
	 * The column already exists.
	 */
	COLUMN_EXISTS,

	/**
	 * The configuration is already in use.
	 */
	CONFIGURATION_IN_USE,

	/**
	 * The creation or modification of a table or a database failed.
	 */
	CREATION_OR_MODIFICATION_FAILED,

	/**
	 * The process of building a domain is not yet finished.
	 */
	DOMAIN_BUILDING_IN_PROGRESS,

	/**
	 * It is not possible to alter a primary key.
	 */
	DONT_CHANGE_PK,

	/**
	 * There is no expression to evaluate.
	 */
	EMPTY_EXPRESSION,

	/**
	 * An external error occurred.
	 */
	EXTERNAL_ERROR,

	/**
	 * The Java class used is not suitable in this context.
	 */
	INCOMPATIBLE_TYPES,

	/**
	 * The SQL type is not suitable in this context.
	 */
	INCOMPATIBLE_STORAGE_CLASSES,

	/**
	 * Some mandatory parameters are missed in the configuration.
	 */
	INCOMPLETE_CONFIGURATION,

	/**
	 * The arguments do not match the expected signature or the set of expected
	 * arguments.
	 */
	INVALID_ARGUMENTS,

	/**
	 * The set of column signatures contains unexpected objects.
	 */
	INVALID_COLUMN_SIGNATURES,

	/**
	 * This name is unsuitable in this context.
	 */
	INVALID_NAME,

	/**
	 * The prototype is not suitable in this context.
	 */
	INVALID_PROTOTYPE,

	/**
	 * The number of columns used is not sufficient in this context.
	 */
	MISSING_COLUMN,

	/**
	 * The number of operators used is not sufficient in this context.
	 */
	MISSING_OPERATOR,

	/**
	 * The class doesn't have a default constructor.
	 */
	MISSING_DEFAULT_CONSTRUCTOR,

	/**
	 * There exists no default value for this column.
	 */
	MISSING_DEFAULT_VALUE,

	/**
	 * The class is not declared as an {@link Entity}.
	 */
	MISSING_ENTITY_ANNOTATION,

	/**
	 * The class has no attribute marked as primary key (marked with
	 * {@link Id @Id}).
	 */
	MISSING_PK,

	/**
	 * There is no matching table in the assigned database.
	 */
	MISSING_TABLE,

	/**
	 * No transaction exists.
	 */
	MISSING_TRANSACTION,

	/**
	 * {@link SqlColumnSignature.Flag#AUTOINCREMENT} is not suitable in this
	 * context.
	 */
	MISPLACED_AUTO_INCREMENT,

	/**
	 * There is no suitable SQL type for a Java type.
	 */
	NO_SUITABLE_STORAGE_CLASS,

	/**
	 * Only serializable values can be stored in a column directly.
	 */
	NON_SERIALIZABLE_FIELD,

	/**
	 * Entities must be final classes.
	 */
	NON_FINAL_CLASS,

	/**
	 * A table should contain at least two columns. One for the primary key and one
	 * for a value.
	 */
	NOT_ENOUGH_COLUMNS,

	/**
	 * This object cannot be deleted from the database. It either has clients or is
	 * a client.
	 */
	OBJECT_IN_USE,

	/**
	 * It is not possible to create or edit a domain if the configuration is already
	 * use within another session.
	 */
	OTHER_SESSION_EXISTS,

	/**
	 * It is impossible to work with a closed table.
	 */
	TABLE_CLOSED,

	/**
	 * It is impossible to modify the structure of a table if the table is part of a
	 * m2m or m2o association.
	 */
	TABLE_IN_USE,

	/**
	 * It is not possible to create a view or a table.
	 */
	TABLE_OR_VIEW_EXISTS,

	/**
	 * The table module is already closed.
	 */
	TABLE_MODULE_ALREADY_CLOSED,

	/**
	 * There is still a transaction running, this should be closed first.
	 */
	TRANSACTION_RUNNING,

	/**
	 * Several rows have been deleted. Please check consistency of the database.
	 */
	UNEXPECTED_DELETE,

	/**
	 * The {@link TableAssignment table assignment} used is unknown.
	 */
	UNKNOWN_ASSIGNMENT,

	/**
	 * The {@link Column column} used is unknown.
	 */
	UNKNOWN_COLUMN,

	/**
	 * Some {@link Column columns} used are unknown.
	 */
	UNKNOWN_COLUMNS,

	/**
	 * Some literals used cannot be mapped to an {@link Table.TabCMP
	 * operator}.
	 */
	UNKNOWN_OPERATOR,

	/**
	 * The configuration used ({@link DBConfiguration}, {@link ModuleConfiguration})
	 * is unknown.
	 */
	UNKNOWN_CONFIGURATION,

	/**
	 * The {@link SqlDatabase database} used is unknown.
	 */
	UNKNOWN_DATABASE,

	/**
	 * The object used is unknown.
	 */
	UNKNOWN_OBJECT,

	/**
	 * The {@link Session session} used is unknown.
	 */
	UNKNOWN_SESSION,

	/**
	 * The {@link TableModule table module} used is unknown.
	 */
	UNKNOWN_TABLE_MODULE,

	/**
	 * The voucher used is unknown.
	 */
	UNKNOWN_TRANSACTION,

	/**
	 * The URI used is not supported.
	 */
	UNKNOWN_URI,

	/**
	 * One of the classes is not suitable for a many-to-one association.
	 */
	WRONG_CLASS_IN_MANY_TO_ONE_DECLARATION,

	/**
	 * One of the classes is not suitable for a many-to-many association.
	 */
	WRONG_CLASS_IN_MANY_TO_MANY_DECLARATION,

	/**
	 * A {@link SqlColumnSignature column definition} used is not suitable for the
	 * selected table.
	 */
	WRONG_COLUMN_DEFINITION,

	/**
	 * The name is not suitable for an SQL column. A name should at least match the
	 * following regular expression: {@literal "[a-zA-Z_][\w#@$]*"}.
	 */
	WRONG_COLUMN_NAME,

	/**
	 * The container used is not suitable for a many-to-many association.
	 */
	WRONG_CONTAINER_IN_MANY_TO_MANY_DECLARATION, //

	/**
	 * The join table name used is not suitable for this many-to-many association.
	 */
	WRONG_JOIN_TABLE_IN_MANY_TO_ONE_DECLARATION, //

	/**
	 * Java type and or SQL type are not suitable for a primary key.
	 */
	WRONG_PK_TYPE, //

	/**
	 * The voucher for the running transaction is invalid.
	 */
	WRONG_TRANSACTION;

	/**
	 * Converts an error into an exception.
	 *
	 * @return the {@link CasketException}.
	 */
	public CasketException build() {
		return new CasketException(this, this.name());
	}

}