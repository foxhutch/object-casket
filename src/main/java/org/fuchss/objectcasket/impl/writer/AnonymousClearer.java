package org.fuchss.objectcasket.impl.writer;

import java.util.HashMap;
import java.util.Map;

import org.fuchss.objectcasket.impl.Entity;
import org.fuchss.objectcasket.impl.EntityFactory;
import org.fuchss.objectcasket.impl.SessionImpl;
import org.fuchss.objectcasket.impl.field.FieldInfo;
import org.fuchss.objectcasket.impl.field.Many2ManyFieldInfo;
import org.fuchss.objectcasket.impl.field.One2ManyFieldInfo;
import org.fuchss.objectcasket.impl.field.One2OneFieldInfo;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.tablemodule.port.Transaction;

public class AnonymousClearer {
	private Map<FieldInfo.Kind, FieldFunction> clearAnonymousFieldMap = new HashMap<FieldInfo.Kind, FieldFunction>() {
		private static final long serialVersionUID = 1L;

		{ // stored FieldInfos are inverse informations
			this.put(FieldInfo.Kind.ONE2ONE, AnonymousClearer.this::clearO2O);
			this.put(FieldInfo.Kind.ONE2MANY, AnonymousClearer.this::clearM2O);
			this.put(FieldInfo.Kind.MANY2MANY, AnonymousClearer.this::clearM2M);

		}
	};

	private FieldInfo info;
	private Transaction transaction;
	private Entity entity;
	private SessionImpl session;

	public AnonymousClearer(FieldInfo info, Transaction transaction, SessionImpl session, Entity entity) {
		this.info = info;
		this.transaction = transaction;
		this.entity = entity;
		this.session = session;
	}

	public void clear() throws ObjectCasketException {
		this.clearAnonymousFieldMap.get(this.info.getKind()).apply(this.info, this.transaction);
	}

	@FunctionalInterface
	private interface FieldFunction {
		void apply(FieldInfo info, Transaction transaction) throws ObjectCasketException;
	}

	private void clearO2O(FieldInfo commomInfo, Transaction transaction) throws ObjectCasketException {
		One2OneFieldInfo info = (One2OneFieldInfo) commomInfo;
		String columnName = info.getRemoteFkColumnName();
		Object remoteObject = this.entity.getStoredRemoteObjectO2O(null, columnName, transaction);
		if (remoteObject != null) {
			EntityFactory remoteEf = this.session.getEntityFactory(info.getEntityClass());
			Entity remoteEntity = remoteEf.entity(remoteObject, transaction);
			remoteEntity.clear_o2o_Field(info.getField(), info.getColumnName(), transaction);
			this.entity.clear_o2o_Field(null, columnName, transaction);
		}
	}

	private void clearM2O(FieldInfo commonInfo, Transaction transaction) throws ObjectCasketException {
		One2ManyFieldInfo info = (One2ManyFieldInfo) commonInfo;
		String columnName = info.getRemoteFkColumnName();

		Object remoteObject = this.entity.getStoredRemoteObjectM2O(null, columnName, transaction);

		if (remoteObject != null) {
			EntityFactory remoteEf = this.session.getEntityFactory(info.getEntityClass());
			Entity remoteEntity = remoteEf.entity(remoteObject, transaction);
			remoteEntity.getRemoteObjectsO2M(info.getField()).remove(this.entity.getMe());
			this.entity.clear_m2o_Field(null, columnName, transaction);
		}
	}

	private void clearM2M(FieldInfo commomInfo, Transaction transaction) throws ObjectCasketException {
		Many2ManyFieldInfo info = (Many2ManyFieldInfo) commomInfo;
		String ownFkcolumnName = info.getOwnFKColumnName();
		String remoteFkColumnName = info.getRemoteFKColumnName();

		Object ownObject = this.entity.getStoredRemoteObjectM2O(info.getOwnFKfield(), ownFkcolumnName, transaction);
		Object remoteObject = this.entity.getStoredRemoteObjectM2O(info.getRemoteFKfield(), remoteFkColumnName, transaction);

		if (ownObject != null) {
			assert (remoteObject != null);
			EntityFactory ownEf = this.session.getEntityFactory(info.getEntityClass());
			EntityFactory remoteEf = this.session.getEntityFactory(info.getRemoteClass());
			Entity ownEntity = ownEf.entity(ownObject, transaction);
			Entity remoteEntity = remoteEf.entity(remoteObject, transaction);
			ownEntity.getRemoteObjectsM2M(info.getField()).remove(remoteObject);
			ownEntity.getJoinTableM2M(info.getField()).remove(remoteObject);
			if (info.getRemoteField() != null) {
				remoteEntity.getRemoteObjectsM2M(info.getRemoteField()).remove(ownObject);
				remoteEntity.getJoinTableM2M(info.getRemoteField()).remove(this.entity.getMe());
			}
			this.entity.clear_m2o_Field(info.getOwnFKfield(), ownFkcolumnName, transaction);
			this.entity.clear_m2o_Field(info.getRemoteFKfield(), remoteFkColumnName, transaction);
		} else {
			assert (remoteObject == null);
		}
	}

}
