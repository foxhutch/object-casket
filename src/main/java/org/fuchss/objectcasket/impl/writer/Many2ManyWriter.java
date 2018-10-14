package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.impl.field.Many2ManyFieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class Many2ManyWriter implements Writer {
	protected Field field;
	protected Transaction transaction;
	protected SessionImpl session;
	protected Entity entity;
	protected Many2ManyFieldInfo info;

	protected Many2ManyWriter(Field field, Transaction transaction, SessionImpl session, Entity entity) {
		this.field = field;
		this.transaction = transaction;
		this.session = session;
		this.entity = entity;
		this.info = this.entity.getM2MFieldInfo(this.field);
	}

	protected Object createAndSetJoinTableM2M(Object remoteObject) throws ObjectCasketException {
		Entity joinTableEntity = this.session.getEntityFactory(this.info.getJoinTableClass()).mkObjectAndEntity(this.transaction);
		Object joinTableObj = joinTableEntity.getMe();
		Entity remoteEnity = this.session.getEntityFactory(this.info.getRemoteClass()).entity(remoteObject, this.transaction);

		joinTableEntity.set_m2o_Field(this.entity, this.info.getOwnFKfield(), this.info.getOwnFKColumnName(), this.transaction);
		joinTableEntity.set_m2o_Field(remoteEnity, this.info.getRemoteFKfield(), this.info.getRemoteFKColumnName(), this.transaction);

		this.setMeInForeignObjectM2M(remoteObject, joinTableObj);
		return joinTableObj;
	}

	protected void setMeInForeignObjectM2M(Object foreignObject, Object joinTableObj) throws ObjectCasketException {
		Field foreignField = this.info.getRemoteField();
		if (foreignField == null) {
			return;
		}
		EntityFactory ef = this.session.getEntityFactory(this.info.getRemoteClass());
		Entity foreignEntity = ef.entity(foreignObject, this.transaction);
		foreignEntity.getRemoteObjectsM2M(foreignField).add(this.entity.getMe());
		foreignEntity.getStoredRemoteObjectsM2M(foreignField).add(this.entity.getMe());
		foreignEntity.getJoinTableM2M(foreignField).put(this.entity.getMe(), joinTableObj);
	}

	protected void clearAndRemoveJoinTableM2M(Object remoteObject) throws ObjectCasketException {
		this.removeMeInForeignObjectM2M(remoteObject);

		Object joinTableObj = this.entity.getJoinTableM2M(this.field).get(remoteObject);
		EntityFactory ef = this.session.getEntityFactory(this.info.getJoinTableClass());
		Entity joinTableEntity = ef.entity(joinTableObj, this.transaction);

		joinTableEntity.clear_m2o_Field(this.info.getOwnFKfield(), this.info.getOwnFKColumnName(), this.transaction);
		joinTableEntity.clear_m2o_Field(this.info.getRemoteFKfield(), this.info.getRemoteFKColumnName(), this.transaction);

		this.session.someObjectToDelete(joinTableObj);
	}

	protected void removeMeInForeignObjectM2M(Object foreignObject) throws ObjectCasketException {
		Field foreignField = this.info.getRemoteField();
		if (foreignField == null) {
			return;
		}

		EntityFactory ef = this.session.getEntityFactory(this.info.getRemoteClass());
		Entity foreignEntity = ef.entity(foreignObject, this.transaction);

		foreignEntity.getRemoteObjectsM2M(foreignField).add(this.entity.getMe());
		foreignEntity.getStoredRemoteObjectsM2M(foreignField).remove(this.entity.getMe());
		foreignEntity.getJoinTableM2M(foreignField).remove(this.entity.getMe());
	}

}
