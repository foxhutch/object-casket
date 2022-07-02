package org.fuchss.objectcasket.api;

import org.fuchss.objectcasket.api.objects.FAIL;
import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestWrongTable extends TestBase {

	@Test
	public void test() throws IOException, ObjectCasketException {
		Session session = this.storePort.sessionManager().session(this.config());
		session.declareClass(FAIL.class);
		Assertions.assertThrows(ObjectCasketException.class, session::open);
	}
}
