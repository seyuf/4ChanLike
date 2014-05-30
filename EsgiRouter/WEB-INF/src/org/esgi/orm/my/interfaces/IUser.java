package org.esgi.orm.my.interfaces;

import java.util.List;

public interface IUser {
	
	public int getId ();
	public String getMail();
	public void setMail(String mail);
	public String getPseudo();
	public void setPseudo(String pseudo);
	public List<ISubject> getSubjects ();
	public void addSubject (ISubject subject);

}
