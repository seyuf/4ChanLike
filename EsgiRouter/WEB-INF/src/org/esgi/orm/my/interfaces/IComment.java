package org.esgi.orm.my.interfaces;

public interface IComment {
	
	public int getId ();
	public String getContent ();
	public void setContent (String content);
	public ISubject getSubject ();
	public void addSubject(ISubject subject);
	public IFile getFile ();
	public void setFile (IFile file);

}
