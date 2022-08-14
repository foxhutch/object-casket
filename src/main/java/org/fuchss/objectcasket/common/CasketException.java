package org.fuchss.objectcasket.common;

import java.io.Serial;
import java.util.Arrays;
import java.util.Objects;

/**
 * The common Exception class for the object-casket system.
 *
 * @see CasketError
 */
public class CasketException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * The underlying error code.
	 */
	private final CasketError error;

	private CasketException(Throwable cause) {
		super(cause);
		this.error = CasketError.EXTERNAL_ERROR;
	}

	CasketException(CasketError error, String msg) {
		super(msg);
		this.error = error;
		StackTraceElement[] stack = this.getStackTrace();
		this.setStackTrace(Arrays.copyOfRange(stack, 1, stack.length));
	}

	/**
	 * Converts an arbitrary Exception into an CasketException.
	 *
	 * @param exc - the exception to convert.
	 * @return the CasketException.
	 */
	public static CasketException build(Exception exc) {
		Objects.requireNonNull(exc);
		StackTraceElement[] original = exc.getStackTrace();
		exc.setStackTrace(Arrays.copyOfRange(original, 1, original.length));

		if (exc instanceof CasketException aCasketException)
			return aCasketException;
		return new CasketException(exc);
	}

	/**
	 * @return the underlying error code.
	 * @see CasketError
	 */
	public CasketError error() {
		return this.error;
	}

}
