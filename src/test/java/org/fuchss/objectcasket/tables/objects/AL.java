package org.fuchss.objectcasket.tables.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//
// ---- 1              1 -----
//| AL | x------------> |  AR  |
// ----                  -----
//

@Entity()
@Table(name = "AL")
public final class AL {

	@Id
	@GeneratedValue
	public Integer pk;

	@OneToOne
	public AR one2one;
}
