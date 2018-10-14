package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;
import java.util.Objects;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.impl.field.FieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class M2O_ValueWriter extends Many2OneWriter {
	public M2O_ValueWriter(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		super(field, transaction, session, entity, ownFactory);
	}

	public void persist() throws ObjectCasketException {

		Object remoteObject = this.entity.getRemoteObjectM2O(this.field);
		Object oldRemoteObject = this.entity.getStoredRemoteObjectM2O(this.field, this.info.getColumnName(), this.transaction);

		if (Objects.equals(remoteObject, oldRemoteObject)) {
			return;
		}
		if (this.info.getForeignFieldKind() == FieldInfo.Kind.MANY2MANY) {
			WriterException.Error.KISSmanyTomany.build(this.field.getName(), this.ownFactory.getType().getSimpleName());
		}

		this.setRemoteObject(remoteObject);
		this.removeMeInOldRemoteObject(oldRemoteObject);

	}

}
