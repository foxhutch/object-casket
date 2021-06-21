package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_CHAR1")
public final class O2M_S_CHAR1 implements O2M__S_Object<O2M_S_CHAR1> {

	@Id
	Character cCharacter;

	O2M_S_CHAR1() {
	}

	public O2M_S_CHAR1(char x) {
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
