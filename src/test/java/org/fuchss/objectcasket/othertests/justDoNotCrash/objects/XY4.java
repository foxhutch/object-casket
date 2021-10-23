package org.fuchss.objectcasket.othertests.justDoNotCrash.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "newaccount")
public final class XY4 {

	private static String toStringString = "newaccount(id = %s, column1 = %s, column2 = %s, column3 = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	@Column(name = "column1")
	public Integer column;

	@Column(columnDefinition = "Varchar")
	private String column2;

	@ManyToMany()
	@JoinTable(name = "transaction", joinColumns = @JoinColumn(name = "id_newaccount"), inverseJoinColumns = @JoinColumn(name = "id_accounting"))
	private Set<XY3> column3 = new HashSet<>();

	@Override
	public String toString() {
		return String.format(XY4.toStringString, this.id, this.column, this.column2, this.column3);
	}

}
