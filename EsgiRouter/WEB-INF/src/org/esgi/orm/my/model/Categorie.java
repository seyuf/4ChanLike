package org.esgi.orm.my.model;

import org.esgi.orm.my.annotations.ORM_EXTRA;
import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;
import org.esgi.orm.my.interfaces.ICategorie;;

@ORM_SCHEMA("4shan")
@ORM_TABLE("Categorie")
public class Categorie implements ICategorie {
	@ORM_PK
	@ORM_EXTRA("AUTO_INCREMENT")
	@ORM_FIELD("int")
	public Integer categorieId;
	@ORM_FIELD("TEXT")
	public String categorieName;

	@Override
	public int getId() {
		return this.categorieId;
	}

	@Override
	public String getName() {
		return this.categorieName;
	}

	@Override
	public void setName(String name) {
		this.categorieName = name;
	}
	
	@Override
	public String toString() {
		return "Categorie [id=" + categorieId + ", mail=" + categorieName + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			if (super.equals(obj)) {
				return true;
			} else {
				User user = (User) obj;
				if (user.userId == this.categorieId) {
					return true;
				}
			}
		}
		return false;
	}
}
