package model;


import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.Comment;
import org.junit.Test;

import junit.framework.TestCase;

public class CommentTest extends TestCase {

	@Test
	public void testSave() {
		Comment comment = new Comment();
		comment.commentContent = "test";
		ORM.save(comment);
		Comment result = (Comment) ORM.load(Comment.class, comment.commentId);
		assertEquals(comment, result);
	}

	@Test
	public void testDelete() {
		Comment comment = new Comment();
		comment.commentContent = "test";
		ORM.save(comment);
		int commentId = comment.commentId;
		ORM.remove(Comment.class, commentId);
		Comment result = (Comment) ORM.load(Comment.class, commentId);
		assertNull(result);
	}

}
