package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class O2M_ValueWriter extends One2ManyWriter {
	public O2M_ValueWriter(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		super(field, transaction, session, entity, ownFactory);
	}

	public void persist() throws ObjectCasketException {
		Set<Object> remoteObjects = this.entity.getRemoteObjectsO2M(this.field);
		Set<Object> knownRemoteObjects = this.entity.getStoredRemoteObjectsO2M(this.field);

		Set<Object> addedObjects = new HashSet<>();
		Set<Object> removedObjects = new HashSet<>();

		if (Writer.setCompare(remoteObjects, knownRemoteObjects, addedObjects, removedObjects)) {
			return;
		}

		for (Object removedObject : removedObjects) {
			this.clearRemovedObject(removedObject);
		}

		for (Object addedObject : addedObjects) {
			this.disconnectO2M(addedObject);
		}

		for (Object addedObject : addedObjects) {
			this.connectO2M(addedObject);
		}

		for (Object removedObject : removedObjects) {
			knownRemoteObjects.remove(removedObject);
		}

		for (Object addedObject : addedObjects) {
			knownRemoteObjects.add(addedObject);
		}
	}

}
