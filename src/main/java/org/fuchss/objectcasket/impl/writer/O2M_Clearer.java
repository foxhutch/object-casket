package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;
import java.util.Set;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class O2M_Clearer extends One2ManyWriter {
	public O2M_Clearer(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		super(field, transaction, session, entity, ownFactory);
	}

	public void clear() throws ObjectCasketException {
		Set<Object> knownRemoteObjects = this.entity.getStoredRemoteObjectsO2M(this.field);

		if (knownRemoteObjects.isEmpty()) {
			return;
		}

		for (Object removedObject : knownRemoteObjects) {
			this.clearRemovedObject(removedObject);
		}
	}

}
