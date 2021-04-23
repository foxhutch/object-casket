package org.fuchss.objectcasket.o2m.fundamentals;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.o2m.common.O2M__S_Object;

@Entity()
@Table(name = "O2M_S_CHAR1")
final class O2M_S_CHAR1 implements O2M__S_Object<O2M_S_CHAR1> {

	@Id
	Character cCharacter;

	O2M_S_CHAR1() {
	}

	O2M_S_CHAR1(char x) {
		this.cCharacter = x;
	}

	@Override
	public boolean sameAs(O2M_S_CHAR1 x, O2M_S_CHAR1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cCharacter.charValue() == y.cCharacter.charValue()));
	}

	@Override
	public String toString() {
		return "O2M_S_CHAR1 cCharacter " + (this.cCharacter == null ? "NULL" : ("" + this.cCharacter));
	}

}

@Entity()
@Table(name = "O2M_S_CHAR2")
final class O2M_S_CHAR2 implements O2M__S_Object<O2M_S_CHAR2> {

	@Id
	char tCharacter;

	O2M_S_CHAR2() {
	}

	O2M_S_CHAR2(char x) {
		this.tCharacter = x;
	}

	@Override
	public boolean sameAs(O2M_S_CHAR2 x, O2M_S_CHAR2 y) {
		return x.tCharacter == y.tCharacter;
	}

	@Override
	public String toString() {
		return "PK_CHAR2 tCharacter " + this.tCharacter;
	}

}
