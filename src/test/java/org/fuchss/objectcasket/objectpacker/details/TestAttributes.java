package org.fuchss.objectcasket.objectpacker.details;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import org.fuchss.objectcasket.common.CasketException;
import org.fuchss.objectcasket.objectpacker.PackerPort;
import org.fuchss.objectcasket.objectpacker.port.Configuration;
import org.fuchss.objectcasket.objectpacker.port.Domain;
import org.fuchss.objectcasket.objectpacker.port.Session;
import org.fuchss.objectcasket.objectpacker.port.SessionManager;
import org.fuchss.objectcasket.testutils.Utility;
import org.fuchss.objectcasket.testutils.Utility.DB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestAttributes {

	File dbFile;

	@Test
	void storeAndReadTest() throws CasketException, IOException, InterruptedException {
		this.storeAndReadTest(DB.SQLITE);
		this.storeAndReadTest(DB.H2);
	}

	private void storeAndReadTest(DB dialect) throws IOException, CasketException, InterruptedException {
		this.dbFile = Utility.createFile(this);
		Exception exc = null;
		try {
			SessionManager manager = PackerPort.PORT.sessionManager();

			Configuration config = manager.createConfiguration();
			config.setDriver(Utility.dialectDriverMap.get(dialect), Utility.dialectUrlPrefixMap.get(dialect), Utility.dialectMap.get(dialect));
			config.setUri(this.dbFile.toURI().getPath());
			config.setUser("");
			config.setPassword("");
			config.setFlag(Configuration.Flag.ALTER, Configuration.Flag.CREATE, Configuration.Flag.SESSIONS, Configuration.Flag.WRITE);

			Domain dom = manager.mkDomain(config);

			manager.addEntity(dom, AttributeEntity.class);

			manager.finalizeDomain(dom);

			Session session = manager.session(config);
			session.declareClass(AttributeEntity.class);

			AttributeEntity entity = new AttributeEntity();
			AttributeEntity entity1 = new AttributeEntity((byte) 11);

			session.persist(entity);
			session.persist(entity1);

			manager.terminateAll();

			session = manager.session(config);
			session.declareClass(AttributeEntity.class);

			Set<AttributeEntity> attrs = session.getAllObjects(AttributeEntity.class);
			Assertions.assertEquals(2, attrs.size());
			for (AttributeEntity attr : attrs) {
				if (attr.id == entity.id)
					Assertions.assertTrue(entity.sameAs(attr));
				else
					Assertions.assertTrue(entity1.sameAs(attr));
			}

			manager.terminateAll();
		} catch (Exception e) {
			exc = e;
			e.printStackTrace();
		} finally {
			Files.deleteIfExists(this.dbFile.toPath());
		}
		Assertions.assertNull(exc);
	}

}

@Entity()
final class AttributeEntity {

	@Id
	@GeneratedValue
	Integer id;

	String stringX;

	Long longX;
	long longY = 0;
	Integer intX = null;
	int intY = 0;
	Short shortX = null;
	short shortY = 0;
	Byte byteX = null;
	byte byteY = 0;
	Boolean booleanX = null;
	boolean booleanY = false;
	Float floatX = null;
	float floatY = 0;
	Double doubleX = null;
	double doubleY = 0;
	Character charX = null;
	char charY = (char) 0;
	Date dateX = null;

	AttributeEntity() {
	}

	AttributeEntity(byte val) {

		this.stringX = String.format("%d", val);

		this.longX = (long) val;
		this.longY = val;
		this.intX = (int) val;
		this.intY = val;
		this.shortX = (short) val;
		this.shortY = val;
		this.byteX = val;
		this.byteY = val;
		this.booleanX = 0 == (val % 2);
		this.booleanY = 0 == (val % 2);
		this.floatX = (float) val;
		this.floatY = val;
		this.doubleX = (double) val;
		this.doubleY = val;
		this.charX = (char) val;
		this.charY = (char) val;
		this.dateX = new Date(val);

	}

	boolean sameAs(AttributeEntity other) {
		boolean res = true;

		res &= this.id == other.id;
		res &= this.stringX == null ? other.stringX == null : this.stringX.equals(other.stringX);

		res &= this.longX == null ? other.longX == null : this.longX.equals(other.longX);
		res &= this.longY == other.longY;
		res &= this.intX == null ? other.intX == null : this.intX.equals(other.intX);
		res &= this.intY == other.intY;
		res &= this.shortX == null ? other.shortX == null : this.shortX.equals(other.shortX);
		res &= this.shortY == other.shortY;
		res &= this.byteX == null ? other.byteX == null : this.byteX.equals(other.byteX);
		res &= this.byteY == other.byteY;
		res &= this.booleanX == null ? other.booleanX == null : this.booleanX.equals(other.booleanX);
		res &= this.booleanY == other.booleanY;
		res &= this.floatX == null ? other.floatX == null : this.floatX.equals(other.floatX);
		res &= this.floatY == other.floatY;
		res &= this.doubleX == null ? other.doubleX == null : this.doubleX.equals(other.doubleX);
		res &= this.doubleY == other.doubleY;
		res &= this.charX == null ? other.charX == null : this.charX.equals(other.charX);
		res &= this.charY == other.charY;
		res &= this.dateX == null ? other.dateX == null : this.dateX.equals(other.dateX);

		return res;

	}

}
