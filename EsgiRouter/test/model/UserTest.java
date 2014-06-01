package model;

import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.User;
import org.junit.Test;

import junit.framework.TestCase;

public class UserTest extends TestCase {

	@Test
	public void testSave() {
		User user = new User();
		user.userMail = "test@test.com";
		user.userPseudo = "pseudo";
		ORM.save(user);
		User result = (User) ORM.load(User.class, user.userId);
		assertEquals(user, result);
	}

	@Test
	public void testDelete() {
		User user = new User();
		user.userMail = "test@test.com";
		user.userPseudo = "pseudo";
		ORM.save(user);
		int userId = user.userId;
		ORM.remove(User.class, userId);
		User result = (User) ORM.load(User.class, userId);
		assertNull(result);
	}

}
