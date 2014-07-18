package org.esgi.orm.my.model;

import org.esgi.orm.my.annotations.ORM_CLASSNAME;
import org.esgi.orm.my.annotations.ORM_EXTRA;
import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_RELATION;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;
import org.esgi.orm.my.interfaces.IComment;
import org.esgi.orm.my.interfaces.IFile;
import org.esgi.orm.my.interfaces.ISubject;

@ORM_SCHEMA("4shan")
@ORM_TABLE("Comment")
public class Comment implements IComment {
	
	@ORM_PK
	@ORM_EXTRA("AUTO_INCREMENT")
	@ORM_FIELD("int")
	public Integer commentId;
	@ORM_FIELD("TEXT")
	public String commentContent;
	@ORM_FIELD("int")
	@ORM_RELATION("manyToOne")
	@ORM_CLASSNAME("org.esgi.orm.my.model.Subject")
	public ISubject subjectID;
	@ORM_FIELD("int")
	@ORM_RELATION("manyToOne")
	@ORM_CLASSNAME("org.esgi.orm.my.model.File")
	public IFile fileID;
	
	@Override
	public int getId() {
		return this.commentId;
	}

	@Override
	public String getContent() {
		return this.commentContent;
	}

	@Override
	public void setContent(String content) {
		this.commentContent = content;
	}

	@Override
	public ISubject getSubject() {
		return this.subjectID;
	}

	@Override
	public void addSubject(ISubject subject) {
		this.subjectID = subject;
	}

	@Override
	public IFile getFile() {
		return this.fileID;
	}

	@Override
	public void setFile(IFile file) {
		this.fileID = file;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Comment) {
			if (super.equals(obj)) {
				return true;
			} else {
				Comment comment = (Comment) obj;
				if (comment.commentId == this.commentId) {
					return true;
				}
			}
		}
		return false;
	}

}
