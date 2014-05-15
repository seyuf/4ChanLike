package org.esgi.orm.my.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.esgi.orm.my.annotations.ORM_EXTRA;
import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;

@ORM_SCHEMA("esgi")
@ORM_TABLE("user")
public class User {
	@ORM_PK
	@ORM_EXTRA("AUTO_INCREMENT")
	@ORM_FIELD("int")
	public Integer id;
	@ORM_FIELD("varchar (50)")
	public String login;
	@ORM_FIELD("varchar (50)")
	public String password;
	@ORM_FIELD("timestamp")
	public volatile Date connectedAt;
	public List<Role> roles = new ArrayList<>();
	
	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password
				+ ", connectedAt=" + connectedAt + ", roles = "+roles+"]";
	}
}