package org.fuchss.sqlconnector.port;

import java.util.Arrays;

/**
 * Exception type for SQLConnector.
 */
public class ConnectorException extends Exception {

	private static final long serialVersionUID = 1L;

	protected ConnectorException() {
		super();
	}

	protected ConnectorException(String message) {
		super(message);
	}

	protected ConnectorException(String message, Throwable cause) {
		super(message, cause);
	}

	protected ConnectorException(Throwable cause) {
		super(cause);
	}

	public static void build(Exception exc) throws ConnectorException {
		if (exc != null && exc instanceof ConnectorException) {
			throw (ConnectorException) exc;
		}
		throw new ConnectorException(exc);
	}

	protected static <T extends ConnectorException> void build(T e) throws T {
		StackTraceElement[] original = e.getStackTrace();
		e.setStackTrace(Arrays.copyOfRange(original, 1, original.length));
		throw e;
	}

}
