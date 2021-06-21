package org.fuchss.objectcasket.sqltypes.objects.character;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.sqltypes.objects.common.PK__Object;

@Entity()
@Table(name = "PK_CHAR2")
public final class PK_CHAR2 implements PK__Object<PK_CHAR2> {

	@Id
	public char tCharacter;

	public char attr1;

	PK_CHAR2() {
	}

	public PK_CHAR2(char x) {
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