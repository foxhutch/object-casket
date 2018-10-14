package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.impl.field.Many2OneFieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class Many2OneWriter implements Writer {
	protected Field field;
	protected Transaction transaction;
	protected Entity entity;
	protected EntityFactory ownFactory;
	protected EntityFactory foreignFactory;
	protected Many2OneFieldInfo info;

	protected Many2OneWriter(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		this.field = field;
		this.transaction = transaction;
		this.entity = entity;
		this.ownFactory = ownFactory;
		this.info = this.entity.getM2OFieldInfo(this.field);
		this.foreignFactory = session.getEntityFactory(this.info.getForeignClass());

	}

	protected void setRemoteObject(Object remoteObj) throws ObjectCasketException {
		if (remoteObj == null) {
			this.entity.clear_m2o_Field(this.field, this.info.getColumnName(), this.transaction);
			return;
		}
		Entity remoteEntity = this.foreignFactory.entity(remoteObj, this.transaction);
		this.entity.set_m2o_Field(remoteEntity, this.field, this.info.getColumnName(), this.transaction);

		if (this.info.getForeignField() == null) {
			return;
		}
		remoteEntity.getStoredRemoteObjectsO2M(this.info.getForeignField()).add(this.entity.getMe());
		remoteEntity.getRemoteObjectsO2M(this.info.getForeignField()).add(this.entity.getMe());
	}

	protected void removeMeInOldRemoteObject(Object oldRemoteObject) throws ObjectCasketException {
		if (oldRemoteObject == null || this.info.getForeignField() == null) {
			return;
		}
		Entity oldEntity = this.foreignFactory.entity(oldRemoteObject, this.transaction);
		oldEntity.getStoredRemoteObjectsO2M(this.info.getForeignField()).remove(this.entity.getMe());
		oldEntity.getRemoteObjectsO2M(this.info.getForeignField()).remove(this.entity.getMe());
	}

}
