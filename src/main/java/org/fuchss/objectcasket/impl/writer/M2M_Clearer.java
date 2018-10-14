package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;
import java.util.Set;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class M2M_Clearer extends Many2ManyWriter {
	public M2M_Clearer(Field field, Transaction transaction, SessionImpl session, Entity entity) {
		super(field, transaction, session, entity);
	}

	public void clear() throws ObjectCasketException {
		Set<Object> knownRemoteObjects = this.entity.getStoredRemoteObjectsM2M(this.field);

		if (knownRemoteObjects.isEmpty()) {
			return;
		}

		for (Object removedObject : knownRemoteObjects) {
			this.clearAndRemoveJoinTableM2M(removedObject);
		}

		this.entity.getJoinTableM2M(this.field).clear();
		knownRemoteObjects.clear();

	}

}
