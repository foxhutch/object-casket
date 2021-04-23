package org.fuchss.objectcasket.blobs;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "BLOB_INTS")
final class BLOB_Ints implements BLOB_Object<BLOB_Ints> {

	@Id
	@GeneratedValue
	Integer pk;

	@Column(columnDefinition = "BLOB")
	int[] blob;

	BLOB_Ints() {
	}

	BLOB_Ints(int[] bytes) {
		this.blob = bytes;
	}

	@Override
	public boolean sameAs(BLOB_Ints x, BLOB_Ints y) {
		return (((x.pk == y.pk) || ((x.pk != null) && (y.pk != null) && (x.pk.intValue() == y.pk.intValue()))) //
				&& ((x.blob == y.blob) || ((x.blob != null) && (y.blob != null) && Arrays.equals(this.blob, x.blob))));
	}

}
