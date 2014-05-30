package org.esgi.orm.my.interfaces;

import java.util.Date;
import java.util.List;

public interface ISubject {
	
	public int getId();
	public String getName ();
	public void setName (String name);
	public Date getDate ();
	public void setDate (Date date);
	public List<IComment> getComments ();
	public void addComment (IComment comment);

}
