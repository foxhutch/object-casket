package org.fuchss.objectcasket.blobs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fuchss.objectcasket.blobs.objects.BLOB_Ints;
import org.fuchss.objectcasket.blobs.objects.BLOB_IntsList;
import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Session;
import org.junit.Assert;
import org.junit.Test;

public class TestBlob extends TestBase {

	@Test
	public void testPkInt() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				BLOB_Ints.class);
		session.open();

		Set<BLOB_Ints> sql1 = new HashSet<>();

		int[] x = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		BLOB_Ints myBlob = new BLOB_Ints(x);
		session.persist(myBlob);
		sql1.add(myBlob);

		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				BLOB_Ints.class);
		session.open();

		Set<BLOB_Ints> sql_1 = session.getAllObjects(BLOB_Ints.class);

		this.storePort.sessionManager().terminate(session);

		Assert.assertTrue(myBlob.check(sql1, sql_1));

	}

	@Test
	public void testOverride() throws Exception {

		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				BLOB_IntsList.class);
		session.open();

		BLOB_IntsList myBlob = new BLOB_IntsList(new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)));
		session.persist(myBlob);
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				BLOB_IntsList.class);
		session.open();

		myBlob = session.getAllObjects(BLOB_IntsList.class).iterator().next();
		// Now write blob
		myBlob.blob.set(0, 42);
		session.persist(myBlob);
		this.storePort.sessionManager().terminate(session);

		session = this.storePort.sessionManager().session(this.config());
		session.declareClass(//
				BLOB_IntsList.class);
		session.open();

		Set<BLOB_IntsList> sql_1 = session.getAllObjects(BLOB_IntsList.class);

		this.storePort.sessionManager().terminate(session);
		Assert.assertTrue(myBlob.check(Set.of(myBlob), sql_1));
	}
}
