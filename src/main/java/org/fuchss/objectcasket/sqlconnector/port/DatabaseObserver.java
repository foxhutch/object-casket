package org.fuchss.objectcasket.sqlconnector.port;

import java.util.List;
import java.util.Map;

/**
 * In order to track the changes made by others, it is possible to assign an
 * observer to each {@link TableAssignment} existing on a specific
 * {@link SqlDatabase}. in a given database. These observers are informed when
 * something changes in the affected database table.
 *
 * @see SqlDatabase#attach(DatabaseObserver, TableAssignment)
 * @see SqlDatabase#detach(DatabaseObserver, TableAssignment)
 * @see SqlDatabase#newRow(PreCompiledStatement, Map, Object)
 * @see SqlDatabase#delete(PreCompiledStatement, Map, Object)
 * @see SqlDatabase#updateRow(PreCompiledStatement, SqlObject, Map, Object)
 */
public interface DatabaseObserver {

	/**
	 * If an object should be informed about performed database updates, it must
	 * implement this operation.
	 *
	 * @param tabOrView - the observed {@link TableAssignment}.
	 * @param changed   - the primary keys of all changed rows.
	 * @param deleted   - the former primary keys of all deleted rows.
	 * @param added     - the new primary keys of all added rows.
	 */
	void update(TableAssignment tabOrView, List<SqlObject> changed, List<SqlObject> deleted, List<SqlObject> added);

}
