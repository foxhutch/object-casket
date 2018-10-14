package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.impl.field.One2ManyFieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class One2ManyWriter implements Writer {
	protected Field field;
	protected Transaction transaction;
	protected Entity entity;
	protected One2ManyFieldInfo info;
	protected Field remoteFkField;
	protected EntityFactory fkFactory;
	protected EntityFactory ownFactory;

	protected One2ManyWriter(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		this.field = field;
		this.transaction = transaction;
		this.entity = entity;
		this.info = this.entity.getO2MFieldInfo(this.field);
		this.remoteFkField = this.info.getRemoteFkField();
		this.fkFactory = session.getEntityFactory(this.info.getForeignClass());
		this.ownFactory = ownFactory;
	}

	protected void clearRemovedObject(Object removedObject) throws ObjectCasketException {
		Entity fkEntity = this.fkFactory.entity(removedObject, this.transaction);
		fkEntity.clear_m2o_Field(this.remoteFkField, this.info.getRemoteFkColumnName(), this.transaction);
	}

	protected void disconnectO2M(Object addedObject) throws ObjectCasketException {
		Entity addedEntity = this.fkFactory.entity(addedObject, this.transaction);
		Object oldObject = addedEntity.getStoredRemoteObjectM2O(this.remoteFkField, this.info.getRemoteFkColumnName(), this.transaction);
		if (oldObject == null) {
			return;
		}
		Entity oldEntity = this.ownFactory.entity(oldObject, this.transaction);
		oldEntity.getStoredRemoteObjectsO2M(this.field).remove(addedObject);
		oldEntity.getRemoteObjectsO2M(this.field).remove(addedObject);
	}

	protected void connectO2M(Object addedObject) throws ObjectCasketException {
		Entity addedEntity = this.fkFactory.entity(addedObject, this.transaction);
		addedEntity.set_m2o_Field(this.entity, this.remoteFkField, this.info.getRemoteFkColumnName(), this.transaction);
	}

}
