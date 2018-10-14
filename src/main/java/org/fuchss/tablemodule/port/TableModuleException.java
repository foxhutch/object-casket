package org.fuchss.tablemodule.port;

import java.util.Arrays;

public class TableModuleException extends Exception {

	private static final long serialVersionUID = 1L;

	protected TableModuleException() {
		super();
	}

	protected TableModuleException(String message) {
		super(message);
	}

	protected TableModuleException(String message, Throwable cause) {
		super(message, cause);
	}

	protected TableModuleException(Throwable cause) {
		super(cause);
	}

	public static TableModuleException build(Exception exc) throws TableModuleException {
		if (exc != null && exc instanceof TableModuleException) {
			throw (TableModuleException) exc;
		}
		throw new TableModuleException(exc);
	}

	protected static <T extends TableModuleException> void build(T e) throws T {
		StackTraceElement[] original = e.getStackTrace();
		e.setStackTrace(Arrays.copyOfRange(original, 1, original.length));
		throw e;
	}

}