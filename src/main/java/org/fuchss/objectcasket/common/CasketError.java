package org.fuchss.objectcasket.common;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.fuchss.objectcasket.sqlconnector.port.SqlColumnSignature;
import org.fuchss.objectcasket.tablemodule.port.Table;

/**
 * Object Casket error codes
 */

public interface CasketError {

	/**
	 * Returns the name of the error colde.
	 *
	 * @return the name of this error code.
	 */
	String name();

	enum CE0 implements CasketError {

		/**
		 * An external error occurred.
		 */
		EXTERNAL_ERROR(""),

		/**
		 * No transaction exists.
		 */
		MISSING_TRANSACTION("No transaction is running!");

		private String message;

		private CE0(String message) {
			this.message = message;
		}

		/**
		 * Converts an error into an exception.
		 *
		 * @return the {@link CasketException}.
		 */
		public CasketException defaultBuild() {
			return new CasketException(this, this.message);
		}

	}

	enum CE1 implements CasketError {

		/**
		 * The class is already declared. Don't declare twice.
		 */
		CLASS_ALREADY_DECLARED("Class (%s) is already declared!"), //

		/**
		 * The process of building a domain is not yet finished.
		 */
		DOMAIN_BUILDING_IN_PROGRESS("There is a non finalized open domain for configuration (%s). This should be finalized first!"),

		/**
		 * There is no expression to evaluate.
		 */
		EMPTY_EXPRESSION("The expression (%s) has either no operand or no assigned field!"),

		/**
		 * The named file already exists an cannot be created.
		 */
		FILE_ALREADY_EXISTS("The file (%s) already exists!"),

		/**
		 * Some mandatory parameters are missed in the configuration.
		 */
		INCOMPLETE_CONFIGURATION("The configuration (%s) is missing either some driver information, the URI, an user name or a password!"),

		/**
		 * The set of column signatures contains unexpected objects.
		 */
		INVALID_COLUMN_SIGNATURES("The collection of column definitions contains undefined elements %s!"),

		/**
		 * This name is not suitable in this context. A name should at least match the
		 * following regular expression: {@literal "[a-zA-Z_][\w#@$]*"}.
		 */
		INVALID_NAME("Name (%s) is not new or prohibited, choose another one!"),

		/**
		 * {@link SqlColumnSignature.Flag#AUTOINCREMENT} is not suitable in this
		 * context.
		 */
		MISPLACED_AUTO_INCREMENT("(%s) is not a correct column signature for a auto incremented primary key column!"),

		/**
		 * The class doesn't have a default constructor.
		 */
		MISSING_DEFAULT_CONSTRUCTOR("Entity (%s) has no default constructor!"),

		/**
		 * There exists no default value for this column.
		 */
		MISSING_DEFAULT_VALUE("The column signature (%s) with flags %s requires a default value!"),

		/**
		 * The class is not declared as an {@link Entity}.
		 */
		MISSING_ENTITY_ANNOTATION("Class (%s) is not annotated as 'Entity'!"),

		/**
		 * A primary key is missed.
		 */
		MISSING_PK("No primary key in (%s)"),

		/**
		 * There is no matching table in the assigned database.
		 */
		MISSING_TABLE("There exist no table named (%s)!"),

		/**
		 * There is no suitable SQL type for a Java type.
		 */
		NO_SUITABLE_STORAGE_CLASS("There is no suitable SQL type for the Java type (%s)!"),

		/**
		 * Only serializable or primitive values can be stored in a column directly.
		 */
		NON_SERIALIZABLE_FIELD("Field (%s) is neither primitive nor serializable!"),

		/**
		 * Entities must be final classes.
		 */
		NON_FINAL_CLASS("Modifier 'final' is missing in the declaration of class (%s)!"),

		/**
		 * This object cannot be deleted from the database. It either has clients or is
		 * a client.
		 */
		OBJECT_IN_USE("Cannot delete object (%s), it has still some clients or is a client!"),

		/**
		 * It is not possible to create or edit a domain if the configuration is already
		 * use within another session.
		 */
		OTHER_SESSION_EXISTS("There are active sessions for configuration (%s). These should be terminated first!"),

		/**
		 * It is not possible to create a view or a table.
		 */
		TABLE_OR_VIEW_EXISTS("Impossible to create more than one view per table or to create a table twice- affected table is (%s)!"),

		/**
		 * There is still a transaction running, this should be closed first.
		 */
		TRANSACTION_RUNNING("Impossible to initiate a new transaction. Another transaction (%s) is already running!"),

		/**
		 * Some literals used cannot be mapped to an {@link Table.TabCMP operator}.
		 */
		UNKNOWN_OPERATOR("Unknown operator (%s)!"),

		/**
		 * The URI used is not supported.
		 */
		UNSUPPORTED_URI("The URI (%s) exists but is not supported!"),

		/**
		 * Java type and or SQL type are not suitable for a primary key.
		 */
		WRONG_PK_TYPE("(%s) is not correct column signature for a primary key column!");

		private String defaultFormatString;

		private CE1(String defaultFormatString) {
			this.defaultFormatString = defaultFormatString;
		}

		/**
		 * Converts an error into an exception.
		 *
		 * @param obj
		 *            - object for the format string to individualize the error message!
		 * @return the {@link CasketException}.
		 */
		public CasketException defaultBuild(Object obj) {
			Object objNotNull = obj == null ? "null" : obj;
			return new CasketException(this, String.format(this.defaultFormatString, objNotNull));
		}

	}

	enum CE2 implements CasketError {

		/**
		 * A closable object is already closed.
		 */
		ALREADY_CLOSED("The %s (%s) is already closed and can no longer be accessed!"),

		/**
		 * The configuration is already in use.
		 */
		CONFIGURATION_IN_USE("Cannot %s! configuration (%s) is already in use!"),

		/**
		 * The creation or modification of a table failed.
		 */
		CREATION_OR_MODIFICATION_FAILED("There are not enough rights (%s) to drop or create the table %s!"),

		/**
		 * It is not possible to alter a primary key.
		 */
		DONT_CHANGE_PK("Object Casket does not allow to change a primary key value - related column is (%s) in table (%s)!"),

		/**
		 * The key exist so the value cannot stored in this map.
		 */
		KEY_EXISTS("Key (%s) is already stored in map (%s)!"),

		/**
		 * The arguments are not matching the set of expected arguments required by the
		 * pre-compiled statement.
		 */
		MISSED_ARGUMENTS("Some arguments are missed or needed! Missed arguments %s needed arguments %s!"),

		/**
		 * There exists no default value for this column.
		 */
		MISSING_DEFAULT_VALUE("The column signature (%s) with flags %s requires a default value!"),

		/**
		 * The pre-compiled statement requires more values.
		 */
		MISSING_VALUES("Missing values for the columns %s of the pre-compiled statement (%s)!"),

		/**
		 * A table should contain at least two columns. One for the primary key and one
		 * for a value.
		 */
		NOT_ENOUGH_COLUMNS("The table (%s) has not enough columns! %s are missed!"),

		/**
		 * It is impossible to modify the structure of a table if the table is part of a
		 * m2m or m2o association.
		 */
		TABLE_IN_USE("Table (%s) is already assigned and cannot be %s! Close the database first and open it again!"),

		/**
		 * A {@link SqlColumnSignature column definition} used is not suitable for the
		 * selected table.
		 */
		WRONG_COLUMN_DEFINITION("The name (%s) is not contained in table (%s)"),

		/**
		 * The column is not part of the specified table.
		 */
		WRONG_COLUMN_NAME("Column (%s) is not a part of the table (%s)");

		private String defaultFormatString;

		private CE2(String defaultFormatString) {
			this.defaultFormatString = defaultFormatString;
		}

		/**
		 * Converts an error into an exception.
		 *
		 * @param obj0
		 *            - 1st object for the format string to individualize the error
		 *            message!
		 * @param obj1
		 *            - 2nd object for the format string to individualize the error
		 *            message!
		 * @return the {@link CasketException}.
		 */
		public CasketException defaultBuild(Object obj0, Object obj1) {
			Object[] objNotNull = new Object[2];
			objNotNull[0] = (obj0 == null ? "null" : obj0);
			objNotNull[1] = (obj1 == null ? "null" : obj1);
			return new CasketException(this, String.format(this.defaultFormatString, objNotNull));
		}

	}

	enum CE3 implements CasketError {

		/**
		 * The column already exists.
		 */
		COLUMN_EXISTS("Column name (%s) for field (%s) in entity (%s) already exists. Use another one!"),

		/**
		 * The SQL storage class and Java class are incompatible.
		 */
		INCOMPATIBLE_SQL_TYPE("SQL storage class (%s) and object (%s) of type (%s) are incompatible!"),

		/**
		 * The prototype is not suitable in this context.
		 */
		INVALID_PROTOTYPE("There is no proper candidate in the set of associated Java types %s of the SQL type (%s) that is assignable from (%s)!"),

		/**
		 * The number of columns used is not sufficient in this context.
		 */
		MISSING_COLUMN("One or more columns %s are not part of the used table assignment (%s), which contains only %s!"),

		/**
		 * The number of operators used is not sufficient in this context.
		 */
		MISSING_OPERATOR("There are arguments in (%s) not matching the value fields (%s) of (%s)!"),

		/**
		 * The number of operators used is not sufficient in this context.
		 */

		MISSING_SUPPLIER("There exist no supplier for client (%s) in row (%s) of join table (%s)!"),

		/**
		 * A delete operation failed. No rows or more than one row was deleted.
		 */
		UNEXPECTED_DELETE("In table (%s) the delete on row (%s) with primary key (%s) failed!"),

		/**
		 * The {@link Column column} used is unknown.
		 */
		UNKNOWN_COLUMN("The column signature definition (%s) is not allowed for column (%s) in table (%s)!"),

		/**
		 * One of the classes is not suitable for a many-to-one or many-to-many
		 * association.
		 */
		WRONG_CLASS_IN_MANY_TO_X_DECLARATION("Impossible to use table (or join table) (%s) for the m2o or m2m column (%s) in entity (%s)!"),

		/**
		 * The container is not suitable for a many-to-many association.
		 */
		WRONG_CONTAINER_IN_MANY_TO_MANY_DECLARATION("The used container (%s) of field (%s) in entity (%s) is not a set!"); //

		private String defaultFormatString;

		private CE3(String defaultFormatString) {
			this.defaultFormatString = defaultFormatString;
		}

		/**
		 * Converts an error into an exception.
		 *
		 * @param obj0
		 *            - 1st object for the format string to individualize the error
		 *            message!
		 * @param obj1
		 *            - 2nd object for the format string to individualize the error
		 *            message!
		 * @param obj2
		 *            - 3rd object for the format string to individualize the error
		 *            message!
		 * @return the {@link CasketException}.
		 */
		public CasketException defaultBuild(Object obj0, Object obj1, Object obj2) {
			Object[] objNotNull = new Object[3];
			objNotNull[0] = (obj0 == null ? "null" : obj0);
			objNotNull[1] = (obj1 == null ? "null" : obj1);
			objNotNull[2] = (obj2 == null ? "null" : obj2);
			return new CasketException(this, String.format(this.defaultFormatString, objNotNull));
		}

	}

	enum CE4 implements CasketError {

		/**
		 * The Java class used is not suitable in this context.
		 */
		INCOMPATIBLE_TYPES("Java class (%s) and type (%s) of column (%s) in table (%s) are incompatible!"),

		/**
		 * The SQL type is not suitable in this context.
		 */
		INCOMPATIBLE_STORAGE_CLASSES("The SQL type (%s) of value (%s) does not match the required SQL type (%s) for the column signature object (%s)!"),

		/**
		 * The arguments do not match the expected signature or the set of expected
		 * arguments.
		 */
		INVALID_ARGUMENTS("Argument (%s) is not matching the correct signature (%s) for column (%s) in the table assignment (%s)!"),

		/**
		 * The used object is unknown.
		 */
		UNKNOWN_MANAGED_OBJECT("%s (%s) is not managed by %s (%s)!");

		private String defaultFormatString;

		private CE4(String defaultFormatString) {
			this.defaultFormatString = defaultFormatString;
		}

		/**
		 * Converts an error into an exception.
		 *
		 * @param obj0
		 *            - 1st object for the format string to individualize the error
		 *            message!
		 * @param obj1
		 *            - 2nd object for the format string to individualize the error
		 *            message!
		 * @param obj2
		 *            - 3rd object for the format string to individualize the error
		 *            message!
		 * @param obj3
		 *            - 4th object for the format string to individualize the error
		 *            message!
		 * @return the {@link CasketException}.
		 */
		public CasketException defaultBuild(Object obj0, Object obj1, Object obj2, Object obj3) {
			Object[] objNotNull = new Object[4];
			objNotNull[0] = (obj0 == null ? "null" : obj0);
			objNotNull[1] = (obj1 == null ? "null" : obj1);
			objNotNull[2] = (obj2 == null ? "null" : obj2);
			objNotNull[3] = (obj3 == null ? "null" : obj3);
			return new CasketException(this, String.format(this.defaultFormatString, objNotNull));
		}

	}

}