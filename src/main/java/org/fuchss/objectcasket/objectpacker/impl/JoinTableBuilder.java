package org.fuchss.objectcasket.objectpacker.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.fuchss.objectcasket.common.CasketError.CE3;
import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.tablemodule.port.Row;
import org.fuchss.objectcasket.tablemodule.port.Table;
import org.fuchss.objectcasket.tablemodule.port.TableModule;
import org.fuchss.objectcasket.tablemodule.port.TableObserver;

class JoinTableBuilder<C, S> implements TableObserver {

	private static final String PK_ID = "pk@id";
	private static final String CLIENT_ID = "client@id";
	private static final String SUPPLIER_ID = "supplier@id";
	private static final Class<? extends Serializable> PK_TYPE = Integer.class;

	private final SessionImpl session;
	private final TableModule tabMod;
	private Table joinTable;
	private final M2MInfo<C, S> info;

	private final Map<C, Map<S, Row>> joinTabEntryMap = new HashMap<>();
	private final Map<Row, C> rowToClientMap = new HashMap<>();
	private final Map<Row, S> rowToSupplierMap = new HashMap<>();
	private final Map<Serializable, Row> supplierKeyMap = new HashMap<>();

	JoinTableBuilder(SessionImpl sessionImpl, TableModule tableModule, M2MInfo<C, S> info) throws CasketException {
		this.info = info;
		this.session = sessionImpl;
		this.tabMod = tableModule;

		this.createTableOrView();
	}

	Class<S> getSupplierClass() {
		return this.info.getSupplierClass();
	}

	int write(C client, Set<S> suppliers, Object transaction) throws CasketException, IllegalArgumentException, IllegalAccessException {
		Set<S> currentSuppliers = this.joinTabEntryMap.computeIfAbsent(client, k -> new HashMap<>()).keySet();
		boolean hasSuppliers = !currentSuppliers.isEmpty();
		Set<S> newSuppliers = new HashSet<>(suppliers);
		newSuppliers.removeAll(currentSuppliers);
		Set<S> removedSuppliers = new HashSet<>(currentSuppliers);
		removedSuppliers.removeAll(suppliers);

		this.deleteRow(client, removedSuppliers, transaction);
		this.createRows(client, newSuppliers, transaction);

		for (S supplier : newSuppliers)
			this.session.addClient(supplier);
		for (S supplier : removedSuppliers)
			this.session.removeClient(this.info.getSupplierClass(), this.info.getSupplierClassInfo().getPK(supplier));

		if (hasSuppliers)
			return (suppliers.isEmpty() ? -1 : 0);
		return (!suppliers.isEmpty() ? 1 : 0);
	}

	Set<S> allSuppliers(C obj, Object transaction) throws CasketException {
		try {
			if (!this.joinTabEntryMap.containsKey(obj)) {
				Map<S, Row> newJoinTabEntries = new HashMap<>();
				this.readSuppliers(obj, newJoinTabEntries, transaction);
				this.joinTabEntryMap.put(obj, newJoinTabEntries);
			}
			return new HashSet<>(this.joinTabEntryMap.get(obj).keySet());
		} catch (Exception exc) {
			throw CasketException.build(exc);
		}
	}

	private void createTableOrView() throws CasketException {
		Map<String, Class<? extends Serializable>> signature = new HashMap<>();
		signature.put(SUPPLIER_ID, this.info.getSupplierClassInfo().getType());
		signature.put(CLIENT_ID, this.info.getClientClassInfo().getType());
		signature.put(PK_ID, PK_TYPE);

		if (this.tabMod.tableExists(this.info.getJoinTableName()))
			this.joinTable = this.tabMod.mkView(this.info.getJoinTableName(), PK_ID, signature, true);
		else
			this.joinTable = this.tabMod.createTable(this.info.getJoinTableName(), PK_ID, signature, true);
		this.joinTable.register(this);

	}

	private void createRows(C client, Set<S> newSuppliers, Object transaction) throws IllegalArgumentException, IllegalAccessException, CasketException {
		Map<S, Row> joinTabEntries = this.joinTabEntryMap.get(client);
		Serializable clientPk = this.info.getClientClassInfo().getPK(client);
		for (S supplier : newSuppliers) {
			Map<String, Serializable> newValues = new HashMap<>();
			newValues.put(CLIENT_ID, clientPk);
			newValues.put(SUPPLIER_ID, this.info.getSupplierClassInfo().getPK(supplier));
			Row row = this.joinTable.createRow(newValues, transaction);
			joinTabEntries.put(supplier, row);
			this.rowToClientMap.put(row, client);
			this.rowToSupplierMap.put(row, supplier);
		}
	}

	private void deleteRow(C client, Set<S> removedSuppliers, Object transaction) throws CasketException {
		Map<S, Row> joinTabEntries = this.joinTabEntryMap.get(client);

		for (S supplier : removedSuppliers) {
			Row joinTabRow = joinTabEntries.remove(supplier);
			this.joinTable.deleteRow(joinTabRow, transaction);
			this.rowToClientMap.remove(joinTabRow);
			this.rowToSupplierMap.remove(joinTabRow);
		}

	}

	private void readSuppliers(C obj, Map<S, Row> newJoinTabEntries, Object transaction) throws IllegalArgumentException, IllegalAccessException, CasketException {
		Set<Table.Exp> args = new HashSet<>();
		args.add(new Table.Exp(CLIENT_ID, Table.TabCMP.EQUAL, this.info.getClientClassInfo().getPK(obj)));
		List<Row> rows = this.joinTable.searchRows(args, transaction);
		for (Row row : rows) {
			Serializable fk = row.getValue(SUPPLIER_ID, this.info.getSupplierClassInfo().getType());
			Set<Session.Exp> arg = new HashSet<>();
			arg.add(new Session.Exp(this.info.getSupplierClassInfo().getFieldName(), "==", fk));
			S supplier = this.session.getObjects(this.info.getSupplierClass(), arg).iterator().next();
			this.rowToClientMap.put(row, obj);
			this.rowToSupplierMap.put(row, supplier);
			newJoinTabEntries.put(supplier, row);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void update(Set<Row> changed, Set<Row> deleted, Set<Row> added) {
		synchronized (this.session) {
			if (this.session.ignore)
				return;
			try {
				ObjectBuilder<C> clientBuilder = (ObjectBuilder<C>) this.session.objectFactoryMap.getIfExists(this.info.getClientClass());
				ObjectBuilder<S> supplierBuilder = (ObjectBuilder<S>) this.session.objectFactoryMap.getIfExists(this.info.getSupplierClass());

				Set<C> changedClients = new HashSet<>();
				Set<S> changedSuppliers = new HashSet<>();
				Set<Serializable> suppliersToLoad = new HashSet<>();
				for (Row row : deleted) {
					S supplier = this.rowToSupplierMap.remove(row);
					C client = this.rowToClientMap.remove(row);
					if ((client != null) && (supplier != null)) {
						this.joinTabEntryMap.get(client).remove(supplier);
						changedClients.add(client);
					}
				}
				for (Row row : added) {
					Serializable clientKey = row.getValue(CLIENT_ID, this.info.getClientClassInfo().getType());
					C client = clientBuilder.getObjectByPk(clientKey);
					if (client == null)
						continue;
					Serializable supplierKey = row.getValue(SUPPLIER_ID, this.info.getSupplierClassInfo().getType());
					S supplier = supplierBuilder.getObjectByPk(supplierKey);
					if (supplier == null)
						suppliersToLoad.add(supplierKey);
					else
						changedSuppliers.add(supplier);
					changedClients.add(client);
					this.setMaps(row, client, supplier, supplierKey);

				}
				this.informBuilderAndSession(suppliersToLoad, changedClients, changedSuppliers, clientBuilder, supplierBuilder);
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}

	private void setMaps(Row row, C client, S supplier, Serializable supplierKey) throws CasketException {
		if ((supplier == null) && (supplierKey == null))
			throw CE3.MISSING_SUPPLIER.defaultBuild(client, row, this.info.getJoinTableName());
		this.rowToClientMap.put(row, client);
		if (supplier == null)
			this.supplierKeyMap.put(supplierKey, row);
		else {
			this.rowToSupplierMap.put(row, supplier);
			this.joinTabEntryMap.get(client).put(supplier, row);
		}

	}

	private void informBuilderAndSession(Set<Serializable> suppliersToLoad, Set<C> changedClients, Set<S> changedSuppliers, ObjectBuilder<C> clientBuilder, ObjectBuilder<S> supplierBuilder) {
		if (changedClients.isEmpty() && changedSuppliers.isEmpty() && suppliersToLoad.isEmpty())
			return;
		if (!changedClients.isEmpty())
			clientBuilder.changedObjects(changedClients);
		if (!changedSuppliers.isEmpty())
			supplierBuilder.changedObjects(changedSuppliers);
		if (!suppliersToLoad.isEmpty())
			this.session.loadNewAssignedObjects(this, suppliersToLoad, this.info.getSupplierClassInfo().getFieldName());
		this.session.updateDone();
	}

	@SuppressWarnings("unchecked")
	protected void insertNewSuppliers(Map<Serializable, Object> supplierMap) {
		for (Entry<Serializable, Object> entry : supplierMap.entrySet()) {
			Row row = this.supplierKeyMap.remove(entry.getKey());
			C client = this.rowToClientMap.get(row);
			S supplier = (S) entry.getValue();
			this.rowToSupplierMap.put(row, supplier);
			this.joinTabEntryMap.computeIfAbsent(client, k -> new HashMap<>()).put(supplier, row);
		}
	}

}
