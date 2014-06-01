package org.esgi.orm.my.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ORM_CLASSNAME {
	
	String value();

}
