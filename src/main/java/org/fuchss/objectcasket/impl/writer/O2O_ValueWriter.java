package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;
import java.util.Objects;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class O2O_ValueWriter extends One2OneWriter {
	public O2O_ValueWriter(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		super(field, transaction, session, entity, ownFactory);
	}

	public void persist() throws ObjectCasketException {
		Object remoteObject = this.entity.getRemoteObjectO2O(this.field);
		Object oldRemoteObject = this.entity.getStoredRemoteObjectO2O(this.field, this.info.getColumnName(), this.transaction);
		if (Objects.equals(remoteObject, oldRemoteObject)) {
			return;
		}

		Entity oldEntity = (oldRemoteObject == null) ? null : this.foreigFactory.entity(oldRemoteObject, this.transaction);
		Entity remoteEntity = (remoteObject == null) ? null : this.foreigFactory.entity(remoteObject, this.transaction);

		this.clearOldRemoteObject(oldEntity);
		this.clearRemoteRemoteObject(remoteEntity);
		this.setMeInRemoteObject(remoteEntity);

		this.entity.set_o2o_Field(remoteEntity, this.field, this.info.getColumnName(), this.transaction);
	}

}
