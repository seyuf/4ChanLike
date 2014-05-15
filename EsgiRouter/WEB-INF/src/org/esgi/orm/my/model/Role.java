package org.esgi.orm.my.model;

import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;

@ORM_SCHEMA("esgi")
@ORM_TABLE("role")
public class Role {
	
	@ORM_PK
	@ORM_FIELD("varchar (20)")
	public String id;
	@ORM_FIELD("varchar (50)")
	public String value;
	
	@Override
	public String toString() {
		return "Role [id=" + id + ", value=" + value + "]";
	}

}
