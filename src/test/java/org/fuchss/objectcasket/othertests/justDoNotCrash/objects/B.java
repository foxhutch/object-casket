package org.fuchss.objectcasket.othertests.justDoNotCrash.objects;

import javax.persistence.Column;

public class B {
	public Integer columnB;

	private static String toStringString = "(columnB = %s, columnA_B = %s)";

	@Column(name = "columnA_B")
	private Integer columnA;

	public void setA(Integer x) {
		this.columnA = x;
	}

	public Integer getA() {
		return this.columnA;
	}

	@Override
	public String toString() {
		return String.format(B.toStringString, this.columnB, this.columnA);
	}

}