package org.esgi.orm.my;

public interface IORM {

	/** Create or update a record in db. */
	public Object _save(Object o);
	
	/** return an instance of clazz */
	public Object _load(Class clazz, Object id);
	
	/** delete an record from clazz persistence layer */
	public boolean _remove(Class clazz, Object id);
	
	
	
}