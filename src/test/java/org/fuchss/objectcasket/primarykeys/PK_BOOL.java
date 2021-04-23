package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_BOOL1")
final class PK_BOOL1 implements PK__Object<PK_BOOL1> {

	@Id
	Boolean cBoolean;

	Boolean attr1;

	Boolean attr2;

	PK_BOOL1() {
	}

	PK_BOOL1(Boolean x) {
		this.cBoolean = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_BOOL1 x, PK_BOOL1 y) {
		return (((x.cBoolean == y.cBoolean) || ((x.cBoolean != null) && (y.cBoolean != null) && (x.cBoolean.booleanValue() == y.cBoolean.booleanValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.booleanValue() == y.attr1.booleanValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.booleanValue() == y.attr2.booleanValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_BOOL1 cBoolean = %s, attr1 = %s, attr2 = %s", "" + this.cBoolean, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_BOOL2")
final class PK_BOOL2 implements PK__Object<PK_BOOL2> {

	@Id
	boolean tBoolean;

	boolean attr1;

	PK_BOOL2() {
	}

	PK_BOOL2(boolean x) {
		this.tBoolean = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_BOOL2 x, PK_BOOL2 y) {
		return ((x.tBoolean == y.tBoolean) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_BOOL2 tBoolean = %s, attr1 = %s", "" + this.tBoolean, "" + this.attr1);
	}

}