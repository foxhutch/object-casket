package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class O2O_Clearer extends One2OneWriter {
	public O2O_Clearer(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		super(field, transaction, session, entity, ownFactory);
	}

	public void clear() throws ObjectCasketException {

		Object oldRemoteObject = this.entity.getStoredRemoteObjectO2O(this.field, this.info.getColumnName(), this.transaction);
		if (oldRemoteObject == null) {
			return;
		}

		Entity oldEntity = this.foreigFactory.entity(oldRemoteObject, this.transaction);
		this.clearOldRemoteObject(oldEntity);

		this.entity.clear_o2o_Field(this.field, this.info.getColumnName(), this.transaction);
	}

}
