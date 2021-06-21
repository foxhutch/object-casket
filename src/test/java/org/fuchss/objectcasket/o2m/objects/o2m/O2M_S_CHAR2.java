package org.fuchss.objectcasket.o2m.objects.o2m;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "O2M_S_CHAR2")
public final class O2M_S_CHAR2 implements O2M__S_Object<O2M_S_CHAR2> {

	@Id
	char tCharacter;

	O2M_S_CHAR2() {
	}

	public O2M_S_CHAR2(char x) {
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