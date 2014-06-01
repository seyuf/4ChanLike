package model;

import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.File;
import org.junit.Test;

import junit.framework.TestCase;

public class FileTest extends TestCase {

	@Test
	public void testSave() {
		File file = new File();
		file.filePath = "test";
		ORM.save(file);
		File result = (File) ORM.load(File.class, file.fileID);
		assertEquals(file, result);
	}

	@Test
	public void testDelete() {
		File file = new File();
		file.filePath = "test";
		ORM.save(file);
		int fileID = file.fileID;
		ORM.remove(File.class, fileID);
		File result = (File) ORM.load(File.class, fileID);
		assertNull(result);
	}

}
