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
import javax.persistence.Transient;

@Entity
@Table(name = "accounting")
public final class XY3 {

	private static String toStringString = "accounting(id = %s, column1 = %s, column2 = %s, column31 = %s, column32 = %s)";

	@Id
	@GeneratedValue
	public Integer id;

	@Column(name = "column1", columnDefinition = "Integer")
	public Integer column;

	@Column(name = "column2", columnDefinition = "Text")
	private String column2;

	@ManyToMany()
	@JoinTable(name = "accounting", joinColumns = @JoinColumn(name = "id_accounting"), inverseJoinColumns = @JoinColumn(name = "id_accounting2"))
	private Set<XY3> column31 = new HashSet<>();

	@Transient
	@JoinTable(name = "accounting", joinColumns = @JoinColumn(name = "id_accounting2"), inverseJoinColumns = @JoinColumn(name = "id_accounting"))
	private Set<XY3> column32 = new HashSet<>();

	@Override
	public String toString() {
		return String.format(XY3.toStringString, this.id, this.column, this.column2, this.column31, this.column32);
	}

}