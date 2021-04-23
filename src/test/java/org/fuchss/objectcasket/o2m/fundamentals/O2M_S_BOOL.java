package org.fuchss.objectcasket.o2m.fundamentals;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__S_Object;

@Entity()
@Table(name = "O2M_S_BOOL1")
final class O2M_S_BOOL1 implements O2M__S_Object<O2M_S_BOOL1> {

	@Id
	Boolean cBoolean;

	O2M_S_BOOL1() {
	}

	O2M_S_BOOL1(boolean x) {
		this.cBoolean = x;
	}

	@Override
	public boolean sameAs(O2M_S_BOOL1 x, O2M_S_BOOL1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cBoolean.booleanValue() == y.cBoolean.booleanValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_BOOL1 cBoolean " + (this.cBoolean == null ? "NULL" : ("" + this.cBoolean));
	}

}

@Entity()
@Table(name = "O2M_S_BOOL2")
final class O2M_S_BOOL2 implements O2M__S_Object<O2M_S_BOOL2> {

	@Id
	boolean tBoolean;

	O2M_S_BOOL2() {
	}

	O2M_S_BOOL2(boolean x) {
		this.tBoolean = x;
	}

	@Override
	public boolean sameAs(O2M_S_BOOL2 x, O2M_S_BOOL2 y) {
		return x.tBoolean == y.tBoolean;
	}

	@Override
	public String toString() {
		return "O2M_S_BOOL2 tBoolean " + this.tBoolean;
	}
}
