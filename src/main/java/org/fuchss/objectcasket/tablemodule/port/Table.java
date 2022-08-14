package org.fuchss.objectcasket.tablemodule.port;

import org.fuchss.objectcasket.common.CasketException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The representation of an existing database table.
 */
public interface Table {

	/**
	 * To observe changes on the database table done by other table modules or by an
	 * assigned view of the same table module it is possible to register a
	 * {@link TableObserver}.
	 *
	 * @param observer - the observer to add.
	 * @return true if the observer was added and the set of observers has changed.
	 * @see Table#deregister(TableObserver)
	 */
	boolean register(TableObserver observer);

	/**
	 * This operation removes an existing {@link TableObserver}.
	 *
	 * @param observer - the observer to remove.
	 * @return true if the observer was removed and the set of observers has
	 * changed.
	 * @see Table#register(TableObserver)
	 */
	boolean deregister(TableObserver observer);

	/**
	 * This operation creates a new {@link Row row} inside the table.
	 *
	 * @param values  - column names and their initial values. A mapping for the primary
	 *                key is required if the key is not generated automatically.
	 * @param voucher - an object representing the running transaction.
	 * @return the new row.
	 * @throws CasketException on error. If an exception occurs inside an open transaction a
	 *                         rollback will be done and the transaction will be closed.
	 * @see TableModule#beginTransaction()
	 * @see TableModule#rollback(Object)
	 */
	Row createRow(Map<String, ? extends Serializable> values, Object voucher) throws CasketException;

	/**
	 * This operation inserts new values into an existing {@link Row row}.
	 *
	 * @param row     - the {@link Row row} to modify.
	 * @param values  - column names and their new values.
	 * @param voucher - an object representing the running transaction.
	 * @throws CasketException on error. If an exception occurs inside an open transaction a
	 *                         rollback will be done and the transaction will be closed.
	 */
	void updateRow(Row row, Map<String, ? extends Serializable> values, Object voucher) throws CasketException;

	/**
	 * This operation deletes an existing {@link Row row} from the assigned table
	 * inside the database.
	 *
	 * @param row     - the {@link Row row} to delete.
	 * @param voucher - an object representing the running transaction.
	 * @throws CasketException on error. If an exception occurs inside an open transaction a
	 *                         rollback will be done and the transaction will be closed.
	 */
	void deleteRow(Row row, Object voucher) throws CasketException;

	/**
	 * This operation reloads a {@link Row row}. So the row contains the actual
	 * database content. By this operation one can actualize a {@link Row row}.
	 *
	 * @param row     - the {@link Row row} to reload.
	 * @param voucher - an object representing the running transaction.
	 * @throws CasketException on error. If an exception occurs inside an open transaction a
	 *                         rollback will be done and the transaction will be closed.
	 * @see TableModule#beginTransaction()
	 * @see TableModule#rollback(Object)
	 * @see Table#deregister(TableObserver)
	 */
	void reloadRow(Row row, Object voucher) throws CasketException;

	/**
	 * This operation retrieves the entire table (all {@link Row rows}).
	 *
	 * @param voucher - an object representing the running transaction.
	 * @return a list containing all {@link Row rows} of this table.
	 * @throws CasketException on error. If an exception occurs inside an open transaction a
	 *                         rollback will be done and the transaction will be closed.
	 * @see TableModule#beginTransaction()
	 * @see TableModule#rollback(Object)
	 */
	List<Row> allRows(Object voucher) throws CasketException;

	/**
	 * This operation selects all {@link Row rows} matching the conjunction of the
	 * set of {@link Exp expressions}.
	 *
	 * @param args    - the set of {@link Exp expressions}.
	 * @param voucher - an object representing the running transaction.
	 * @return a list containing all {@link Row rows} matching the expressions.
	 * @throws CasketException on error. If an exception occurs inside an open transaction a
	 *                         rollback will be done and the transaction will be closed.
	 * @see TableModule#beginTransaction()
	 * @see TableModule#rollback(Object)
	 */
	List<Row> searchRows(Set<Exp> args, Object voucher) throws CasketException;

	/**
	 * Expressions are first-order predicates together with the column name.
	 *
	 * @param columnName - the name of the column
	 * @param op         - the {@link TabCMP comparator}: {@link TabCMP#LESS},
	 *                   {@link TabCMP#GREATER}, {@link TabCMP#EQUAL},
	 *                   {@link TabCMP#LESSEQ}, {@link TabCMP#GREATEREQ},
	 *                   {@link TabCMP#UNEQUAL};
	 */
	record Exp(String columnName, TabCMP op, Serializable value) {
	}

	/**
	 * This enumeration defines all possible comparators to build {@link Exp
	 * expressions}.
	 *
	 * @see TabCMP#LESS
	 * @see TabCMP#GREATER
	 * @see TabCMP#EQUAL
	 * @see TabCMP#LESSEQ
	 * @see TabCMP#GREATEREQ
	 * @see TabCMP#UNEQUAL
	 */
	enum TabCMP {
		/**
		 * {@literal " ? < x"}
		 */
		LESS,
		/**
		 * {@literal " ? > x"}
		 */
		GREATER,
		/**
		 * {@literal "? = x"}
		 */
		EQUAL,
		/**
		 * {@literal "? <= x"}
		 */
		LESSEQ,
		/**
		 * {@literal "? >= x"}
		 */
		GREATEREQ,
		/**
		 * {@literal "not(? = x)"}
		 */
		UNEQUAL
	}

}
