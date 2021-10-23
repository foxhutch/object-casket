package org.fuchss.objectcasket.othertests.justDoNotCrash;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.Test;

public class Test1SimpleClass extends TestBase {

	@Test
	public void simpleClassTest() throws IOException, ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(TestA.class);
		session.open();
		this.storePort.sessionManager().terminate(session);
	}

}

@Entity()
@Table(name = "tableA")
final class TestA {

	@Id
	@GeneratedValue
	@Column(name = "id")
	Integer id;

	@Column(name = "integer")
	private Integer x;

	@Column(name = "string")
	private String y;

	public TestA() {
	}

	public TestA(Integer x, String y) {
		this.x = x;
		this.y = y;
	}

	public Integer getX() {
		return this.x;
	}

	public String getY() {
		return this.y;
	}

	public Integer getId() {
		return this.id;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(String y) {
		this.y = y;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}