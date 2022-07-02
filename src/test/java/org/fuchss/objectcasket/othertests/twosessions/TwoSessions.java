package org.fuchss.objectcasket.othertests.twosessions;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.fuchss.objectcasket.common.TestBase;
import org.fuchss.objectcasket.port.Configuration;
import org.fuchss.objectcasket.port.ObjectCasketException;
import org.fuchss.objectcasket.port.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TwoSessions extends TestBase {

	@Test
	public void testIt() throws Exception {
		Configuration config = this.config();
		config.setFlag(Configuration.Flag.SESSIONS);

		Session[] session = new Session[3];
		UserDTO[] user = new UserDTO[3];
		LanguageDTO[] lang = new LanguageDTO[3];

		session[0] = this.storePort.sessionManager().session(config);
		session[0].declareClass(UserDTO.class, LanguageDTO.class);
		session[0].open();

		user[0] = new UserDTO();
		user[0].discordId = "XY";
		lang[0] = new LanguageDTO();
		lang[0].languageName = "lang";
		lang[0].userDTO = user[0];
		session[0].persist(user[0]);
		session[0].persist(lang[0]);

		session[1] = this.storePort.sessionManager().session(config);
		session[1].declareClass(UserDTO.class, LanguageDTO.class);
		session[1].open();

		user[1] = compareAndGet(user[0], session[1]);
		lang[1] = compareAndGet(lang[0], session[1]);

		lang[1].languageName = "newOne";
		session[1].persist(lang[1]);
		Assertions.assertFalse(lang[1].sameAs(lang[0]));

		session[2] = this.storePort.sessionManager().session(config);
		session[2].declareClass(UserDTO.class, LanguageDTO.class);
		session[2].open();

		user[2] = compareAndGet(user[1], session[2]);
		lang[2] = compareAndGet(lang[1], session[2]);

		this.storePort.sessionManager().terminate(session[0]);
		this.storePort.sessionManager().terminate(session[1]);
		this.storePort.sessionManager().terminate(session[2]);
		System.out.println("Done");
	}

	private static UserDTO compareAndGet(UserDTO user, Session session) throws ObjectCasketException {
		Set<UserDTO> users = session.getAllObjects(UserDTO.class);
		Assertions.assertTrue(users.size() == 1);
		UserDTO newUser = users.iterator().next();
		Assertions.assertTrue(newUser.sameAs(user));
		return newUser;
	}

	private static LanguageDTO compareAndGet(LanguageDTO lang, Session session) throws ObjectCasketException {
		Set<LanguageDTO> langs = session.getAllObjects(LanguageDTO.class);
		Assertions.assertTrue(langs.size() == 1);
		LanguageDTO newLang = langs.iterator().next();
		Assertions.assertTrue(newLang.sameAs(lang));
		return newLang;
	}

	@Entity
	@Table(name = "User")
	private static final class UserDTO {

		public UserDTO() {
		}

		@Id
		@GeneratedValue
		private Integer id = null;

		@Column(name = "discord_id")
		private String discordId;

		public boolean sameAs(UserDTO user) {
			return (this == user) ? false : ((this.id == user.id) && (this.discordId == null ? user.discordId == null : this.discordId.equals(user.discordId)));
		}

	}

	@Entity
	@Table(name = "Language")
	private static final class LanguageDTO {

		public LanguageDTO() {

		}

		@Id
		@GeneratedValue
		private Integer id = null;

		@ManyToOne
		public UserDTO userDTO;

		public String languageName;

		public boolean sameAs(LanguageDTO lang) {
			return (this == lang) ? false
					: ((this.id == lang.id) && (this.languageName == null ? lang.languageName == null : this.languageName.equals(lang.languageName)) && ((this.userDTO == null ? lang.userDTO == null : this.userDTO.sameAs(lang.userDTO))));
		}

	}

}
