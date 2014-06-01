package model;

import java.util.Date;

import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.Subject;
import org.esgi.orm.my.model.User;
import org.junit.Test;

import junit.framework.TestCase;

public class SubjectTest extends TestCase {

	@Test
	public void testSave() {
		User user = new User();
		user.userMail = "test@test.com";
		user.userPseudo = "pseudo";
		ORM.save(user);
		Subject subject = new Subject();
		subject.date = new Date();
		subject.subjectName = "test";
		subject.userId = user;
		ORM.save(subject);
		Subject result = (Subject) ORM.load(Subject.class, subject.subjectID);
		assertEquals(subject, result);
	}

	@Test
	public void testDelete() {
		User user = new User();
		user.userMail = "test@test.com";
		user.userPseudo = "pseudo";
		ORM.save(user);
		Subject subject = new Subject();
		subject.date = new Date();
		subject.subjectName = "test";
		subject.userId = user;
		ORM.save(subject);
		int subjectID = subject.subjectID;
		ORM.remove(Subject.class, subjectID);
		Subject result = (Subject) ORM.load(Subject.class, subjectID);
		assertNull(result);
	}

}
