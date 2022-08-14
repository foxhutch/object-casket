package org.fuchss.objectcasket.objectpacker.common;

import org.fuchss.objectcasket.objectpacker.port.SessionObserver;

import java.util.HashSet;
import java.util.Set;

public class Observer implements SessionObserver {

	Set<Object> deletedObjects = new HashSet<>();
	Set<Object> changedObjects = new HashSet<>();

	@Override
	public synchronized void externDeleted(Object deleted) {
		this.deletedObjects.add(deleted);
	}

	@Override
	public synchronized void externChanged(Object changed) {
		this.changedObjects.add(changed);
	}

	public synchronized Set<Object> getDeleted() {
		try {
			return new HashSet<>(this.deletedObjects);
		} finally {
			this.deletedObjects.clear();
		}
	}

	public synchronized Set<Object> getChanged() {
		try {
			return new HashSet<>(this.changedObjects);
		} finally {
			this.changedObjects.clear();
		}
	}

	public synchronized boolean isEmpty() {
		return this.deletedObjects.isEmpty() && this.changedObjects.isEmpty();
	}

}