package org.esgi.orm.my.model;

import org.esgi.orm.my.annotations.ORM_EXTRA;
import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;
import org.esgi.orm.my.interfaces.IComment;
import org.esgi.orm.my.interfaces.IFile;

@ORM_SCHEMA("4shan")
@ORM_TABLE("FIle")
public class File implements IFile {
	
	@ORM_PK
	@ORM_EXTRA("AUTO_INCREMENT")
	@ORM_FIELD("int")
	public Integer fileID;
	@ORM_FIELD("TEXT")
	public String filePath;
	public IComment commentId;

	@Override
	public int getId() {
		return this.fileID;
	}

	@Override
	public String getPath() {
		return this.filePath;
	}

	@Override
	public void setPath(String path) {
		this.filePath = path;
	}

	@Override
	public IComment getComment() {
		return this.commentId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof File) {
			if (super.equals(obj)) {
				return true;
			} else {
				File file = (File) obj;
				if (file.fileID == this.fileID) {
					return true;
				}
			}
		}
		return false;
	}

}
