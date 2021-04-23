package org.fuchss.objectcasket.primarykeys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "PK_CHAR1")
final class PK_CHAR1 implements PK__Object<PK_CHAR1> {

	@Id
	Character cCharacter;

	Character attr1;

	Character attr2;

	PK_CHAR1() {
	}

	PK_CHAR1(Character x) {
		this.cCharacter = x;
		this.attr2 = x;
	}

	@Override
	public boolean sameAs(PK_CHAR1 x, PK_CHAR1 y) {
		return (((x.cCharacter == y.cCharacter) || ((x.cCharacter != null) && (y.cCharacter != null) && (x.cCharacter.charValue() == y.cCharacter.charValue()))) //
				&& ((x.attr1 == y.attr1) || ((x.attr1 != null) && (y.attr1 != null) && (x.attr1.charValue() == y.attr1.charValue()))) //
				&& ((x.attr2 == y.attr2) || ((x.attr2 != null) && (y.attr2 != null) && (x.attr2.charValue() == y.attr2.charValue()))));
	}

	@Override
	public String toString() {
		return String.format("PK_CHAR1 cCharacter = %s, attr1 = %s, attr2 = %s", "" + this.cCharacter, "" + this.attr1, "" + this.attr2);
	}

}

@Entity()
@Table(name = "PK_CHAR2")
final class PK_CHAR2 implements PK__Object<PK_CHAR2> {

	@Id
	char tCharacter;

	char attr1;

	PK_CHAR2() {
	}

	PK_CHAR2(char x) {
		this.tCharacter = x;
		this.attr1 = x;
	}

	@Override
	public boolean sameAs(PK_CHAR2 x, PK_CHAR2 y) {
		return ((x.tCharacter == y.tCharacter) && (x.attr1 == y.attr1));
	}

	@Override
	public String toString() {
		return String.format("PK_CHAR2 tCharacter = %s, attr1 = %s", "" + this.tCharacter, "" + this.attr1);
	}

}
