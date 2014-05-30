package org.esgi.orm.my.model;

import java.util.Date;

import org.esgi.orm.my.annotations.ORM_EXTRA;
import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;
import org.esgi.orm.my.interfaces.ISubject;
import org.esgi.orm.my.interfaces.IUser;

@ORM_SCHEMA("4shan")
@ORM_TABLE("Subject")
public class Subject implements ISubject {
	
	@ORM_PK
	@ORM_EXTRA("AUTO_INCREMENT")
	@ORM_FIELD("int")
	public Integer subjectID;
	@ORM_FIELD("TEXT")
	public String subjectName;
	@ORM_FIELD("datetime")
	public Date date;
	public IUser userId;

	@Override
	public int getId() {
		return this.subjectID;
	}

	@Override
	public String getName() {
		return this.subjectName;
	}

	@Override
	public void setName(String name) {
		this.subjectName = name;
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public IUser getUser() {
		return this.userId;
	}

}
