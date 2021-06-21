package org.fuchss.objectcasket.api;

import java.io.IOException;

import org.fuchss.objectcasket.api.objects.FAIL;
import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.Test;

public class TestWrongTable extends TestBase {

	@Test(expected = ObjectCasketException.class)
	public void test() throws IOException, ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(FAIL.class);
		session.open();
		this.storePort.sessionManager().terminate(session);

	}
}
