package org.fuchss.objectcasket.blobs;

import java.util.HashSet;
import java.util.Set;

import org.fuchss.objectcasket.blobs.objects.BLOB_Ints;
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
}
