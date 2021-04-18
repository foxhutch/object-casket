package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_CHAR1")
final class PK_CHAR1 implements PK__Object<PK_CHAR1> {

	@Id
	Character cCharacter;

	PK_CHAR1() {
	}

	PK_CHAR1(char x) {
		this.cCharacter = x;
	}

	@Override
	public boolean sameAs(PK_CHAR1 x, PK_CHAR1 y) {
		return (x == y) || ((x != null) && (y != null) && (x.cCharacter.charValue() == y.cCharacter.charValue()));
	}

	@Override
	public String toString() {
		return "PK_CHAR1 cCharacter " + (this.cCharacter == null ? "NULL" : ("" + this.cCharacter));
	}

}

@Entity()
@Table(name = "PK_CHAR2")
final class PK_CHAR2 implements PK__Object<PK_CHAR2> {

	@Id
	char tCharacter;

	PK_CHAR2() {
	}

	PK_CHAR2(char x) {
		this.tCharacter = x;
	}

	@Override
	public boolean sameAs(PK_CHAR2 x, PK_CHAR2 y) {
		return x.tCharacter == y.tCharacter;
	}

	@Override
	public String toString() {
		return "PK_CHAR2 tCharacter " + this.tCharacter;
	}

}
