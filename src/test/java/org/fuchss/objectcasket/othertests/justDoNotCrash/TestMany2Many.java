
package org.fuchss.objectcasket.othertests.justDoNotCrash;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketCMP;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestMany2Many extends TestBase {

	@Test
	public void testMany2Many() throws IOException, ObjectCasketException {
		Session session = null;
		for (int i = 0; i < 3; i++) {
			session = this.storePort.sessionManager().session(this.config());
			session.declareClass(LeftSide.class, RightSide.class, Join.class);
			session.open();

			RightSide r1 = new RightSide();
			r1.valueR = 1;
			r1.valueSR = "r1";
			RightSide r2 = new RightSide();
			r2.valueR = 2;
			r2.valueSR = "r2";
			RightSide r3 = new RightSide();
			r3.valueR = 3;
			r3.valueSR = "r3";

			session.persist(r1);
			session.persist(r2);
			session.persist(r3);

			LeftSide l1 = new LeftSide();
			l1.valueL = 1;
			l1.valueSL = "l1";
			LeftSide l2 = new LeftSide();
			l2.valueL = 2;
			l2.valueSL = "l2";
			LeftSide l3 = new LeftSide();
			l3.valueL = 3;
			l3.valueSL = "l3";

			session.persist(l1);
			session.persist(l2);
			session.persist(l3);

			r1.left.add(l1);
			r1.left.add(l2);
			r1.left.add(l3);

			r2.left.add(l1);
			r2.left.add(l2);
			r2.left.add(l3);

			session.persist(r1);
			session.persist(r2);

			r1.left.remove(l2);

			session.persist(r1);

			this.storePort.sessionManager().terminate(session);

		}

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(LeftSide.class, RightSide.class, Join.class);
		session.open();

		LeftSide lx = new LeftSide();
		lx.valueSL = "l1";
		Map<String, ObjectCasketCMP> selectMap = new HashMap<>();
		selectMap.put("valueSL", ObjectCasketCMP.EQUAL);
		Set<LeftSide> leftSides = session.getObjectsByPrototype(lx, selectMap);
		Assert.assertTrue(leftSides.size() == 3);

		RightSide rx = new RightSide();
		rx.valueSR = "r2";
		selectMap = new HashMap<>();
		selectMap.put("valueSR", ObjectCasketCMP.EQUAL);
		Set<RightSide> rightSides = session.getObjectsByPrototype(rx, selectMap);
		Assert.assertTrue(rightSides.size() == 3);

		this.storePort.sessionManager().terminate(session);
	}

}

@Entity()
@Table(name = "RightSide")
final class RightSide {

	@Id
	@GeneratedValue
	Integer id;

	Integer valueR;

	public String valueSR;

	@ManyToMany()
	@JoinTable(name = "jointab")
	Set<LeftSide> left = new HashSet<>();

}

@Entity()
@Table(name = "LeftSide")
final class LeftSide {

	@Id
	@GeneratedValue
	Integer id;

	Integer valueL;

	public String valueSL;

	@ManyToMany()
	@JoinTable(name = "jointab")
	Set<RightSide> right = new HashSet<>();

}

@Entity()
@Table(name = "jointab")
final class Join {
	@Id
	@GeneratedValue
	Integer id;

}
