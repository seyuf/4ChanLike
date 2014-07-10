package org.esgi.orm.my.model;

import org.esgi.orm.my.annotations.ORM_EXTRA;
import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;
import org.esgi.orm.my.interfaces.IUser;

@ORM_SCHEMA("4shan")
@ORM_TABLE("User")
public class User implements IUser {
	@ORM_PK
	@ORM_EXTRA("AUTO_INCREMENT")
	@ORM_FIELD("int")
	public Integer userId;
	@ORM_FIELD("TEXT")
	public String userMail;
	@ORM_FIELD("TEXT")
	public String userPseudo;

	@Override
	public int getId() {
		return this.userId;
	}

	@Override
	public String getMail() {
		return this.userMail;
	}

	@Override
	public void setMail(String mail) {
		this.userMail = mail;
	}

	@Override
	public String getPseudo() {
		return this.userPseudo;
	}

	@Override
	public void setPseudo(String pseudo) {
		this.userPseudo = pseudo;
	}
	
	@Override
	public String toString() {
		return "User [id=" + userId + ", mail=" + userMail + ", pseudo=" + userPseudo+"]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			if (super.equals(obj)) {
				return true;
			} else {
				User user = (User) obj;
				if (user.userId == this.userId) {
					return true;
				}
			}
		}
		return false;
	}
}