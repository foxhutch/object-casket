package org.fuchss.objectcasket.port;

import java.util.Arrays;

/**
 * Exception type for Object Casket.
 */
public class ObjectCasketException extends Exception {
	private static final long serialVersionUID = 1L;

	protected ObjectCasketException() {
		super();
	}

	protected ObjectCasketException(String message) {
		super(message);
	}

	protected ObjectCasketException(String message, Throwable cause) {
		super(message, cause);
	}

	protected ObjectCasketException(Throwable cause) {
		super(cause);
	}

	public static void build(Exception exc) throws ObjectCasketException {
		if (exc != null && exc instanceof ObjectCasketException) {
			throw (ObjectCasketException) exc;
		}
		throw new ObjectCasketException(exc);
	}

	protected static <T extends ObjectCasketException> void build(T e) throws T {
		StackTraceElement[] original = e.getStackTrace();
		e.setStackTrace(Arrays.copyOfRange(original, 1, original.length));
		throw e;
	}

}
