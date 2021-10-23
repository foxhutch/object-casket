package org.fuchss.objectcasket.blobs.objects;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "BLOB_INTS_LIST")
public final class BLOB_IntsList implements BLOB_Object<BLOB_IntsList> {

	@Id
	@GeneratedValue
	Integer pk;

	@Column(columnDefinition = "BLOB")
	public List<Integer> blob;

	BLOB_IntsList() {
	}

	public BLOB_IntsList(List<Integer> ints) {
		this.blob = ints;
	}

	@Override
	public boolean sameAs(BLOB_IntsList x, BLOB_IntsList y) {
		return (((x.pk == y.pk) || ((x.pk != null) && (y.pk != null) && (x.pk.intValue() == y.pk.intValue()))) //
				&& ((x.blob == y.blob) || ((x.blob != null) && (y.blob != null) && Objects.equals(x.blob, y.blob))));
	}

}
