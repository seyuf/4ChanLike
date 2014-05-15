package org.esgi.orm.my;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.esgi.orm.my.annotations.ORM_EXTRA;
import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;

public class ORM implements IORM {

	static ORM instance;
	private Connection connectionMySQL;
	private String host;
	private int port;
	private String login;
	private String mdp;
	
	static {
		instance = new ORM();
	}
	
	public ORM() {
		this.host = "localhost";
		this.port = 3306;
		this.login = "root";
		this.mdp = "mamama";
	}
	
	public static void createTable(Class clazz) {
		instance.connectToDatabase(clazz);
		instance._createTable(clazz);
	}
	
	public static Object save(Object o) {
		instance.connectToDatabase((Class<Object>)o.getClass());
		return instance._save(o);
	}

	public static Object load(Class clazz, Object id) {
		instance.connectToDatabase(clazz);
		return instance._load(clazz, id);
	}

	public static boolean remove(Class clazz, Object id) {
		instance.connectToDatabase(clazz);
		return instance._remove(clazz, id);
	}

	public static List<Object> find(Class clazz, String[] projection, Map<String, Object> where, String[] orderby, Integer limit, Integer offset) {
		instance.connectToDatabase(clazz);
		return instance._find(clazz, projection, where, orderby, limit, offset);
	}
	
	private void connectToDatabase (Class<Object> clazz) {
		String databaseName = "";
		if (clazz.isAnnotationPresent(ORM_SCHEMA.class)) {
			databaseName = clazz.getAnnotation(ORM_SCHEMA.class).value();
		} else {
			databaseName = clazz.getSimpleName().toLowerCase();
		}
		try {
			if (this.connectionMySQL == null || !this.connectionMySQL.getCatalog().equals(databaseName)) {
	        	Class.forName("com.mysql.jdbc.Driver");
	            String url = "jdbc:mysql://"+this.host+":"+this.port+"/"+databaseName;
	            this.connectionMySQL = DriverManager.getConnection(url, this.login, this.mdp);
			}
        } catch (Exception e) {
            System.out.println("probleme connexion");
            System.out.println(e.getMessage());
        }
	}
	
	
	
	public void _createTable(Class<Object> clazz) {
		String tableName;
		if (clazz.isAnnotationPresent(ORM_TABLE.class)) {
			tableName = clazz.getAnnotation(ORM_TABLE.class).value();
		} else {
			tableName = clazz.getSimpleName().toLowerCase();
		}
		String sql = "CREATE TABLE " + tableName + " (";
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0 ; i < fields.length ; i++) {
			if (fields[i].getType().equals(List.class)) {
				if (i == fields.length -1) {
					sql = sql.substring(0, sql.length()-1);
				}
			} else {
				sql += fields[i].getName() + " " + this.getFieldTypeByClass(fields[i]);
				if (this.isPrimaryField(fields[i])) {
					sql += " PRIMARY KEY";
					
				}
				if (fields[i].isAnnotationPresent(ORM_EXTRA.class)) {
					sql += " " + fields[i].getAnnotation(ORM_EXTRA.class).value();
				}
				if (i < fields.length -1) {
					sql += ",";
				}
			}
		}
		sql += ")";
		System.out.println("Create table : " + sql);
		try {
            PreparedStatement prepare = connectionMySQL.prepareStatement(sql);
            prepare.execute();
            prepare.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	@Override
	public Object _load(Class clazz, Object id) {
		Object o = null;
		if (id != null) {
			try {
				o = clazz.newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
			
			String tableName = this.getTableName((Class<Object>)o.getClass());
			Field primaryField = this.getPrimaryField(o.getClass().getDeclaredFields());
			String sql = "SELECT * FROM " + tableName + " WHERE "+primaryField.getName()+" = ?";
			
			System.out.println(sql);
			try {
		        PreparedStatement prepare = connectionMySQL.prepareStatement(sql);
		        if (id.getClass().equals(Integer.class)) {
		        	prepare.setInt(1, (int) id);
		        } else if (id.getClass().equals(String.class)) {
		        	prepare.setString(1, (String) id);
		        }
		        
		        ResultSet result = prepare.executeQuery();
		        Field[] fields = o.getClass().getDeclaredFields();
		        boolean resultResult = false;
		        while (result.next()) {
		        	for (int i = 0 ; i < fields.length ; i++) {
		        		this.setAttributValue(fields[i], o, result);
		        	}
		        	resultResult = true;
		        }
		        if (!resultResult) {
		        	o = null;
		        }
		        result.close();
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		return o;
	}

	@Override
	public boolean _remove(Class clazz, Object id) {
		String tableName = this.getTableName(clazz);
		Field primaryField = this.getPrimaryField(clazz.getDeclaredFields());
		String sql = "DELETE FROM " + tableName + " WHERE "+primaryField.getName()+" = ?";
		try {
            PreparedStatement prepare = connectionMySQL.prepareStatement(sql);
            if (id.getClass().equals(Integer.class)) {
	        	prepare.setInt(1, (int) id);
	        } else if (id.getClass().equals(String.class)) {
	        	prepare.setString(1, (String) id);
	        }
            prepare.execute();
		} catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}

	public List<Object> _find(Class clazz, String[] projection, Map<String, Object> where, String[] orderby, Integer limit, Integer offset) {
		List<Object> list = null;
		String sql = "SELECT ";
		for (int i = 0 ; i < projection.length ; i++) {
			sql += projection[i];
			if (i < projection.length -1) {
				sql += ", ";
			}
		}
		String tableName = this.getTableName(clazz);
		sql += " FROM " + tableName;
		if (where != null && !where.isEmpty()) {
			sql += " WHERE ";
			Iterator<String> keys = where.keySet().iterator();
			int indice = 0;
			int nbElement = where.size();
			while (keys.hasNext()) {
				String key = keys.next();
				sql += key + " = ?";
				if (indice < nbElement-1) {
					sql += " AND ";
				}
				indice++;
			}
		}
		if (orderby != null && orderby.length > 0) {
			sql += " ORDER BY ";
			for (int i = 0 ; i < orderby.length ; i++) {
				sql += orderby[i];
				if (i < orderby.length -1) {
					sql += ", ";
				}
			}
		}
		if (limit != null && limit > 0) {
			sql += " Limit ";
			if (offset != null && offset > 0) {
				sql += offset + ", ";
			}
			sql += limit;
		}
		
		System.out.println(sql);
		try {
	        PreparedStatement prepare = connectionMySQL.prepareStatement(sql);
			if (where != null && !where.isEmpty()) {
				Iterator<String> keys = where.keySet().iterator();
				int indice = 1;
				while (keys.hasNext()) {
					String key = keys.next();
					prepare.setString(indice, where.get(key).toString());
					indice++;
				}
			}
	        ResultSet result = prepare.executeQuery();
	        list = new ArrayList<>();
	        while (result.next()) {
	        	Object o = clazz.newInstance();
	        	Field[] fields = o.getClass().getDeclaredFields();
	        	for (int i = 0 ; i < fields.length ; i++) {
	        		this.setAttributValue(fields[i], o, result);
	        	}
	        	list.add(o);
	        }
	        result.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		return list;
	}

	@Override
	public Object _save(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		
		Field idField = this.getPrimaryField(fields);
		
		String tableName = this.getTableName((Class<Object>)o.getClass());
		
		if (idField != null) {
			Object loadObject = null;
			try {
				loadObject = this._load(o.getClass(), idField.get(o));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if (loadObject != null) {
				this.updateRequest(o, fields, tableName);
			} else {
				this.insertRequest(o, fields, tableName);
			}
		} else {
			this.insertRequest(o, fields, tableName);
		}
		
		return o;
	}
	
	private Object insertRequest (Object o, Field[] fields, String tableName) {
		String insertRequest = "INSERT INTO " + tableName + " (";
		int nbField = 0;
		for (int i = 0 ; i < fields.length ; i++) {
			if (this.getFieldValue(fields[i], o) != null /*&& !this.isPrimaryField(fields[i])*/) {
				nbField++;
			}
		}
		int fieldViewed = 0;
		for (int i = 0 ; i < fields.length ; i++) {
			if (this.getFieldValue(fields[i], o) != null /*&& !this.isPrimaryField(fields[i])*/) {
				fieldViewed++;
				insertRequest += fields[i].getName();
				if (fieldViewed < nbField) {
					insertRequest += ",";
				}
			}
		}
		insertRequest += ") VALUES (";
		for (int i = 0 ; i < nbField ; i++) {
			insertRequest += "?";
			if (i < nbField -1) {
				insertRequest += ",";
			}
		}
		insertRequest += ")";
		try {
			PreparedStatement prepare = connectionMySQL.prepareStatement(insertRequest, Statement.RETURN_GENERATED_KEYS);
			int indice = 1;
			for (int i = 0 ; i < fields.length ; i++) {
				if (this.getFieldValue(fields[i], o) != null /*&& !this.isPrimaryField(fields[i])*/) {
					this.addPreparedField(prepare, indice, fields[i], o);
					indice++;
				}
			}
	        prepare.execute();
	        ResultSet rs = prepare.getGeneratedKeys();
	        if (rs.next()){
	            o.getClass().getField(this.getPrimaryField(fields).getName()).set(o, rs.getInt(1));
	        }
	        prepare.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
		return o;
	}
	
	private Object updateRequest (Object o, Field[] fields, String tableName) {
		String updateRequest = "UPDATE " + tableName + " SET ";
		int nbField = 0;
		for (int i = 0 ; i < fields.length ; i++) {
			if (this.getFieldValue(fields[i], o) != null && !this.isPrimaryField(fields[i])) {
				nbField++;
			}
		}
		int fieldViewed = 0;
		for (int i = 0 ; i < fields.length ; i++) {
			if (this.getFieldValue(fields[i], o) != null && !this.isPrimaryField(fields[i])) {
				fieldViewed++;
				updateRequest += fields[i].getName() + " = ?";
				if (fieldViewed < nbField) {
					updateRequest += ",";
				}
			}
		}
		Field field = getPrimaryField(fields);
		updateRequest += " WHERE " + field.getName() + " = ?";
		try {
			PreparedStatement prepare = connectionMySQL.prepareStatement(updateRequest);
			int indice = 1;
			for (int i = 0 ; i < fields.length ; i++) {
				if (this.getFieldValue(fields[i], o) != null && !this.isPrimaryField(fields[i])) {
					this.addPreparedField(prepare, indice, fields[i], o);
					indice++;
				}
			}
			this.addPreparedField(prepare, indice, field, o);
	        prepare.execute();
	        prepare.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	private Field getPrimaryField (Field[] fields) {
		for (int i = 0 ; i < fields.length ; i++) {
			System.out.println(fields[i].getName() + " : " + fields[i].isAnnotationPresent(ORM_PK.class));
			if (fields[i].isAnnotationPresent(ORM_PK.class)) {
				return fields[i];
			}
		}
		return null;
	}
	
	private boolean isPrimaryField (Field field) {
		return field.isAnnotationPresent(ORM_PK.class);
	}
	
	private String getTableName (Class<Object> clazz) {
		String tableName;
		if (clazz.isAnnotationPresent(ORM_TABLE.class)) {
			tableName = clazz.getAnnotation(ORM_TABLE.class).value();
		} else {
			tableName = clazz.getSimpleName().toLowerCase();
		}
		return tableName;
	}
	
	private String getFieldTypeByClass (Field field) {
		if (field.isAnnotationPresent(ORM_FIELD.class)) {
			return field.getAnnotation(ORM_FIELD.class).value();
		} else {
			return field.getType().getSimpleName().toLowerCase();
		}
	}
	
	private void addPreparedField (PreparedStatement prepare, int indice, Field field, Object o) {
		if (field.getType().equals(Integer.class)) {
			try {
				prepare.setInt(indice, (int)this.getFieldValue(field, o));
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		} else if (field.getType().equals(String.class)) {
			try {
				prepare.setString(indice, (String)this.getFieldValue(field, o));
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	
	private Object getFieldValue (Field field, Object o) {
		Object fieldObject = null;
		try {
			fieldObject = field.get(o);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return fieldObject;
	}
	
	private void setAttributValue (Field field, Object o, ResultSet result) {
		if (field.getType().equals(Integer.class)) {
			try {
				field.set(o, result.getInt(field.getName()));
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		} else if (field.getType().equals(String.class)) {
			try {
				field.set(o, result.getString(field.getName()));
			} catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}

}
