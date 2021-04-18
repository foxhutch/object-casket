package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_BOOL1")
final class PK_BOOL1 implements PK__Object<PK_BOOL1> {

	@Id
	Boolean cBoolean;

	PK_BOOL1() {
	}

	PK_BOOL1(boolean x) {
		this.cBoolean = x;
	}

	@Override
	public boolean sameAs(PK_BOOL1 x, PK_BOOL1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cBoolean.booleanValue() == y.cBoolean.booleanValue()));
	}

	@Override
	public String toString() {
		return "PK_BOOL1 cBoolean " + (this.cBoolean == null ? "NULL" : ("" + this.cBoolean));
	}

}

@Entity()
@Table(name = "PK_BOOL2")
final class PK_BOOL2 implements PK__Object<PK_BOOL2> {

	@Id
	boolean tBoolean;

	PK_BOOL2() {
	}

	PK_BOOL2(boolean x) {
		this.tBoolean = x;
	}

	@Override
	public boolean sameAs(PK_BOOL2 x, PK_BOOL2 y) {
		return x.tBoolean == y.tBoolean;
	}

	@Override
	public String toString() {
		return "PK_BOOL2 tBoolean " + this.tBoolean;
	}

}