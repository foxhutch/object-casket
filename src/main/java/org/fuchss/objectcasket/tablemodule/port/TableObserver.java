package org.fuchss.objectcasket.tablemodule.port;

import java.util.Set;

/**
 * To keep track of modifications done by others, it is possible to attach an
 * observer to any {@link Table} inside the {@link TableModule table module}.
 * These observers will be informed whenever things changed in the assigned
 * database table.
 *
 * @see Table#register(TableObserver)
 * @see Table#deregister(TableObserver)
 *
 */
public interface TableObserver {

	/**
	 * If an object should be informed about performed table changes, it must
	 * implement this operation.
	 *
	 * @param changed
	 *            - all changed rows.
	 * @param deleted
	 *            - all deleted rows.
	 * @param added
	 *            - all added rows.
	 */
	void update(Set<Row> changed, Set<Row> deleted, Set<Row> added);

}
