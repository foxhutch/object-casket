package org.fuchss.objectcasket.port;

/**
 * All compare operations in Object Casket.
 */
public enum ObjectCasketCMP {
	LESS("<"), GREATER(">"), EQUAL("="), LESSEQ("<="), GREATEREQ(">=");

	private final String symbol;

	private ObjectCasketCMP(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return this.symbol;
	}
}
