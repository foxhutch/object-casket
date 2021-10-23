package org.fuchss.objectcasket.json.objects;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "JSON_INTS")
public final class JSON_Ints implements JSON_Object<JSON_Ints> {

	@Id
	@GeneratedValue
	Integer pk;

	@Column(columnDefinition = "JSON")
	public int[] blob;

	JSON_Ints() {
	}

	public JSON_Ints(int[] bytes) {
		this.blob = bytes;
	}

	@Override
	public boolean sameAs(JSON_Ints x, JSON_Ints y) {
		return (((x.pk == y.pk) || ((x.pk != null) && (y.pk != null) && (x.pk.intValue() == y.pk.intValue()))) //
				&& ((x.blob == y.blob) || ((x.blob != null) && (y.blob != null) && Arrays.equals(x.blob, y.blob))));
	}

}
