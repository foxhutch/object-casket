package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.impl.field.One2OneFieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class One2OneWriter implements Writer {
	protected Field field;
	protected Transaction transaction;
	protected Entity entity;
	protected EntityFactory ownFactory;
	protected EntityFactory foreigFactory;
	protected One2OneFieldInfo info;

	protected One2OneWriter(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		this.field = field;
		this.transaction = transaction;
		this.entity = entity;
		this.ownFactory = ownFactory;
		this.info = this.entity.getO2OFieldInfo(this.field);
		this.foreigFactory = session.getEntityFactory(this.info.getForeignClass());
	}

	protected void clearOldRemoteObject(Entity oldEntity) throws ObjectCasketException {
		if (oldEntity == null) {
			return;
		}
		oldEntity.clear_o2o_Field(this.info.getRemoteFkField(), this.info.getRemoteFkColumnName(), this.transaction);
	}

	protected void clearRemoteRemoteObject(Entity remoteEntity) throws ObjectCasketException {
		if (remoteEntity == null) {
			return;
		}
		Object rrObject = remoteEntity.getStoredRemoteObjectO2O(this.info.getRemoteFkField(), this.info.getRemoteFkColumnName(), this.transaction);
		if (rrObject == null) {
			return;
		}
		Entity rrEntity = this.ownFactory.entity(rrObject, this.transaction);
		rrEntity.clear_o2o_Field(this.field, this.info.getColumnName(), this.transaction);
	}

	protected void setMeInRemoteObject(Entity remoteEntity) throws ObjectCasketException {
		if (remoteEntity == null) {
			return;
		}
		remoteEntity.set_o2o_Field(this.entity, this.info.getRemoteFkField(), this.info.getRemoteFkColumnName(), this.transaction);
	}

}
