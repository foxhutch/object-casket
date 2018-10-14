package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class M2M_ValueWriter extends Many2ManyWriter {
	public M2M_ValueWriter(Field field, Transaction transaction, SessionImpl session, Entity entity) {
		super(field, transaction, session, entity);
	}

	public void persist() throws ObjectCasketException {
		Set<Object> remoteObjects = this.entity.getRemoteObjectsM2M(this.field);
		Set<Object> knownRemoteObjects = this.entity.getStoredRemoteObjectsM2M(this.field);

		Set<Object> addedObjects = new HashSet<>();
		Set<Object> removedObjects = new HashSet<>();

		if (Writer.setCompare(remoteObjects, knownRemoteObjects, addedObjects, removedObjects)) {
			return;
		}

		for (Object removedObject : removedObjects) {
			this.clearAndRemoveJoinTableM2M(removedObject);
		}

		Map<Object, Object> newJoinTableObjects = new HashMap<>();
		for (Object addedObject : addedObjects) {
			Object joinTableObject = this.createAndSetJoinTableM2M(addedObject);
			newJoinTableObjects.put(addedObject, joinTableObject);
		}

		Map<Object, Object> knownJoinTableObjects = this.entity.getJoinTableM2M(this.field);
		for (Object removedObject : removedObjects) {
			knownRemoteObjects.remove(removedObject);
			knownJoinTableObjects.remove(removedObject);
		}

		for (Object addedObject : addedObjects) {
			knownRemoteObjects.add(addedObject);
			knownJoinTableObjects.put(addedObject, newJoinTableObjects.get(addedObject));
		}
	}

}
