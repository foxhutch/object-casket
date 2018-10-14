package org.fuchss.objectcasket.impl.writer;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.port.ObjectCasketException;

public interface Writer {
	public static boolean setCompare(Set<Object> newSet, Set<Object> oldSet, Set<Object> addedObjects, Set<Object> removedObjects) {
		if (newSet == null) {
			newSet = new HashSet<>();
		}
		if (oldSet == null) {
			oldSet = new HashSet<>();
		}
		addedObjects.addAll(newSet);
		addedObjects.removeAll(oldSet);
		removedObjects.addAll(oldSet);
		removedObjects.removeAll(newSet);
		return (addedObjects.isEmpty() && removedObjects.isEmpty());
	}

	public static class WriterException extends ObjectCasketException {

		private static final long serialVersionUID = 1L;

		private WriterException(Error error, String... arg) {
			super(error.format(arg));
		}

		public static enum Error {

			KISSmanyTomany("Direct modification of a foreign key field %s in a join table entity %s is prohibided."), //
			MissingInitializedSet("The field %s of class %s is not initialized with a proper (not 'null') set. ");
			private String str;

			private Error(String str) {
				this.str = str;
			}

			private String format(String... arg) {
				Object[] oargs = arg;
				return String.format(this.str, oargs);
			}

			public void build(String... arg) throws ObjectCasketException {
				ObjectCasketException.build(new WriterException(this, arg));
			}

		}
	}

}
