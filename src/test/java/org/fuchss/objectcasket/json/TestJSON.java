package org.fuchss.objectcasket.json;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.json.objects.JSON_Ints;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestJSON extends TestBase {

	@Test
	public void testPkInt() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				JSON_Ints.class);
		session.open();

		Set<JSON_Ints> sql1 = new HashSet<>();

		int[] x = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		JSON_Ints myBlob = new JSON_Ints(x);
		session.persist(myBlob);
		sql1.add(myBlob);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				JSON_Ints.class);
		session.open();

		Set<JSON_Ints> sql_1 = session.getAllObjects(JSON_Ints.class);

		this.storePort.sessionManager().terminate(session);

		Assert.assertTrue(myBlob.check(sql1, sql_1));

	}

	@Test
	public void testOverride() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				JSON_Ints.class);
		session.open();

		JSON_Ints myBlob = new JSON_Ints(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 });
		session.persist(myBlob);
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				JSON_Ints.class);
		session.open();

		myBlob = session.getAllObjects(JSON_Ints.class).iterator().next();
		// Now write blob
		myBlob.blob[0] = 42;
		session.persist(myBlob);
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				JSON_Ints.class);
		session.open();

		Set<JSON_Ints> sql_1 = session.getAllObjects(JSON_Ints.class);

		this.storePort.sessionManager().terminate(session);
		Assert.assertTrue(myBlob.check(Set.of(myBlob), sql_1));
	}
}
