package org.fuchss.objectcasket.impl.writer;

import java.lang.reflect.Field;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.impl.field.FieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class M2O_Clearer extends Many2OneWriter {
	public M2O_Clearer(Field field, Transaction transaction, SessionImpl session, Entity entity, EntityFactory ownFactory) {
		super(field, transaction, session, entity, ownFactory);

	}

	public void clear() throws ObjectCasketException {

		Object oldRemoteObject = this.entity.getStoredRemoteObjectM2O(this.field, this.info.getColumnName(), this.transaction);
		if (oldRemoteObject == null) {
			return;
		}
		if (this.info.getForeignFieldKind() == FieldInfo.Kind.MANY2MANY) {
			WriterException.Error.KISSmanyTomany.build(this.field.getName(), this.ownFactory.getType().getSimpleName());
		}

		this.setRemoteObject(null);
		this.removeMeInOldRemoteObject(oldRemoteObject);
	}

}
