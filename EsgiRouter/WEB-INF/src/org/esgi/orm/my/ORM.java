package org.esgi.orm.my;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.esgi.orm.my.annotations.ORM_CLASSNAME;
import org.esgi.orm.my.annotations.ORM_COMPOSITION;
import org.esgi.orm.my.annotations.ORM_EXTRA;
import org.esgi.orm.my.annotations.ORM_FIELD;
import org.esgi.orm.my.annotations.ORM_PK;
import org.esgi.orm.my.annotations.ORM_RELATION;
import org.esgi.orm.my.annotations.ORM_SCHEMA;
import org.esgi.orm.my.annotations.ORM_TABLE;

public class ORM implements IORM {

	static ORM instance;
	private Connection connectionMySQL;
	private String host, login, mdp;
	private int port;
	private Map<Class, Boolean> createTable;
	
	static {
		instance = new ORM();
	}
	
	public ORM() {
		this.host = "localhost";
		this.port = 3306;
		this.login = "root";
		this.mdp = "";
		this.createTable = new HashMap<>();
	}
	
	public static Object save(Object o) {
		return instance._save(o);
	}

	public static Object load(Class clazz, Object id) {
		return instance._load(clazz, id);
	}

	public static boolean remove(Class clazz, Object id) {
		return instance._remove(clazz, id);
	}

	public static List<Object> find(Class clazz, String[] projection, Map<String, Object> where, String[] orderby, Integer limit, Integer offset) {
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
            System.out.println(e.getMessage());
        }
		this._createTable(clazz);
	}
	
	public void _createTable(Class<Object> clazz) {
		if (this.createTable.get(clazz) == null || !this.createTable.get(clazz)) {
			String tableName = this.getTableName(clazz);
			String sql = "CREATE TABLE " + tableName + " (";
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0 ; i < fields.length ; i++) {
				if (fields[i].isAnnotationPresent(ORM_RELATION.class)) {
					Class listClass = this.getListClass(fields[i]);
					this._createTable(listClass);
					String listValue = fields[i].getAnnotation(ORM_RELATION.class).value();
					if (listValue.equals("oneToOne")) {
						/* relation 1..1, un users ne possède qu'un rôle et un rôle ne 
						 * peut être lié qu'à un user. On aura donc une clé étrangère 
						 * dans la table user qui sera lié à un id unique de la table role */
						sql += fields[i].getName() + " " + this.getFieldTypeByClass(this.getPrimaryField(listClass.getDeclaredFields())) + " UNIQUE NOT NULL";
						if (i < fields.length -1) {
							sql += ",";
						}
					} else if (listValue.equals("oneToMany")) {
						/* relation 1..*, un users possède plusieurs rôle et un rôle ne 
						 * peut être lié qu'à un user. On aura donc une clé étrangère 
						 * dans la table rôle qui sera lié à un id de la table user */
						if (i == fields.length -1) {
							sql = sql.substring(0, sql.length()-1);
						}
						Field primaryField1 = this.getPrimaryField(fields);
						String alterTable = "ALTER TABLE " + this.getTableName(listClass) + " ADD " + 
						primaryField1.getName() + "_" + tableName + " " + this.getFieldTypeByClass(primaryField1);
						try {
				            PreparedStatement prepare = connectionMySQL.prepareStatement(alterTable);
				            prepare.execute();
				            prepare.close();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
					} else if (listValue.equals("manyToOne")) {
						/* relation *..1, un users ne possède qu'un rôle et un rôle 
						 * peut être lié à plusieurs user. On aura donc une clé étrangère 
						 * dans la table user qui sera lié à un id de la table role */
						sql += fields[i].getName() + " " + this.getFieldTypeByClass(this.getPrimaryField(listClass.getDeclaredFields())) + " NOT NULL";
						if (i < fields.length -1) {
							sql += ",";
						}
					} else {
						/* relation *..*, un users possède plusieurs rôle et un rôle 
						 * peut être lié à plusieurs user. On aura donc une table association
						 * qui contiendra un id de user et un id de rôle */
						if (i == fields.length -1) {
							sql = sql.substring(0, sql.length()-1);
						}
						String createAssocTable = "CREATE TABLE " + tableName + "_" + this.getTableName(listClass) + " (";
						Field primaryField1 = this.getPrimaryField(fields);
						Field primaryField2 = this.getPrimaryField(listClass.getDeclaredFields());
						createAssocTable += primaryField1.getName() + "_" + tableName  + " " + this.getFieldTypeByClass(primaryField1) + ",";
						createAssocTable += primaryField2.getName() + "_" + this.getTableName(listClass) + " " + this.getFieldTypeByClass(primaryField2);
						createAssocTable += ")";
						try {
				            PreparedStatement prepare = connectionMySQL.prepareStatement(createAssocTable);
				            prepare.execute();
				            prepare.close();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
					}
				} else {
					sql += fields[i].getName() + " " + this.getFieldTypeByClass(fields[i]);
					if (this.isPrimaryField(fields[i])) {
						sql += " PRIMARY KEY";
					}
					if (fields[i].isAnnotationPresent(ORM_EXTRA.class)) {
						String[] extras = fields[i].getAnnotation(ORM_EXTRA.class).value().split(";");
						for (String extra : extras) {
							sql += " " + extra;
						}
					}
					if (i < fields.length -1) {
						sql += ",";
					}
				}
			}
			sql += ")";
			try {
	            PreparedStatement prepare = connectionMySQL.prepareStatement(sql);
	            prepare.execute();
	            prepare.close();
	            this.createTable.put(clazz, true);
	            System.out.println("create table : " + tableName);
	            for (int i = 0 ; i < fields.length ; i++) {
	            	if (fields[i].isAnnotationPresent(ORM_CLASSNAME.class) && 
	            		(fields[i].getAnnotation(ORM_RELATION.class).value().equals("oneToOne") ||
	            		fields[i].getAnnotation(ORM_RELATION.class).value().equals("manyToOne")		)) {
	            		this.addForeignKey(tableName, fields[i], this.getTableName(this.getListClass(fields[i])), this.getPrimaryField(this.getListClass(fields[i]).getDeclaredFields()));
	            	}
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	
	@Override
	public Object _load(Class clazz, Object id) {
		this.connectToDatabase(clazz);
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
			
			try {
		        PreparedStatement prepare = connectionMySQL.prepareStatement(sql);
		        this.addPreparedIdField(prepare, 1, id);
		        
		        ResultSet result = prepare.executeQuery();
		        Field[] fields = o.getClass().getDeclaredFields();
		        if (result.next()) {
		        	for (int i = 0 ; i < fields.length ; i++) {
		        		if (fields[i].isAnnotationPresent(ORM_RELATION.class)) {
		        			String value = fields[i].getAnnotation(ORM_RELATION.class).value();
		        			if (value.equals("oneToOne") || value.equals("manyToOne")) {
		        				fields[i].set(o, this._load(this.getListClass(fields[i]), result.getObject(fields[i].getName())));
		        			} else {
		        				fields[i].set(o, this.setListAttributValue(fields[i], o, value));
		        			}
		        		} else {
		        			this.setAttributValue(fields[i], o, result);
		        		}
		        	}
		        } else {
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
		this.connectToDatabase(clazz);
		String tableName = this.getTableName(clazz);
		Field primaryField = this.getPrimaryField(clazz.getDeclaredFields());
		String sql = "DELETE FROM " + tableName + " WHERE "+primaryField.getName()+" = ?";
		try {
            PreparedStatement prepare = connectionMySQL.prepareStatement(sql);
            this.addPreparedIdField(prepare, 1, id);
            prepare.execute();
		} catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}

	public List<Object> _find(Class clazz, String[] projection, Map<String, Object> where, String[] orderby, Integer limit, Integer offset) {
		this.connectToDatabase(clazz);
		List<Object> list = null;
		String sql = "SELECT ";
		for (int i = 0 ; i < projection.length ; i++) {
			sql += projection[i] + ((i < projection.length -1) ? ", " : "");
		}
		String tableName = this.getTableName(clazz);
		sql += " FROM " + tableName;
		if (where != null && !where.isEmpty()) {
			sql += " WHERE ";
			Iterator<String> keys = where.keySet().iterator();
			int indice = 0;
			int nbElement = where.size();
			while (keys.hasNext()) {
				sql += keys.next() + " = ?" + ((indice < nbElement-1) ? " AND " : "");
				indice++;
			}
		}
		if (orderby != null && orderby.length > 0) {
			sql += " ORDER BY ";
			for (int i = 0 ; i < orderby.length ; i++) {
				sql += orderby[i] + ((i < orderby.length -1) ? ", " : "");
			}
		}
		if (limit != null && limit > 0) {
			sql += " Limit ";
			if (offset != null && offset > 0) {
				sql += offset + ", ";
			}
			sql += limit;
		}
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
		this.connectToDatabase((Class<Object>)o.getClass());
		Field[] fields = o.getClass().getDeclaredFields();
		Field idField = this.getPrimaryField(fields);
		String tableName = this.getTableName((Class<Object>)o.getClass());
		
		if (this.getFieldValue(idField, o) != null) {
			Object loadObject = this._load(o.getClass(), this.getFieldValue(idField, o));
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
			if (this.isTableField(fields[i], o)) {
				nbField++;
			}
		}
		int fieldViewed = 0;
		for (int i = 0 ; i < fields.length ; i++) {
			if (this.isTableField(fields[i], o)) {
				fieldViewed++;
				insertRequest += fields[i].getName() + ((fieldViewed < nbField) ? "," : "");
			}
		}
		insertRequest += ") VALUES (";
		for (int i = 0 ; i < nbField ; i++) {
			insertRequest += "?" + ((i < nbField -1) ? "," : "");
		}
		insertRequest += ")";
		try {
			PreparedStatement prepare = connectionMySQL.prepareStatement(insertRequest, Statement.RETURN_GENERATED_KEYS);
			int indice = 1;
			for (int i = 0 ; i < fields.length ; i++) {
				if (this.isTableField(fields[i], o)) {
					if (fields[i].isAnnotationPresent(ORM_RELATION.class)) {
						this.addPreparedField(prepare, indice, this.getPrimaryField(this.getListClass(fields[i]).getDeclaredFields()), this.getFieldValue(fields[i], o));
					} else {
						this.addPreparedField(prepare, indice, fields[i], o);
					}
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
		for (int i = 0 ; i < fields.length ; i++) {
			if (fields[i].isAnnotationPresent(ORM_RELATION.class)) {
				if (fields[i].getAnnotation(ORM_RELATION.class).value().equals("manyToMany")) {
					if (this.getFieldValue(fields[i], o) != null) {
						this.insertListObject(fields[i], o);
					}
				} else if (fields[i].getAnnotation(ORM_RELATION.class).value().equals("oneToMany")) {
					this.updateListObjectOneMany(tableName, fields[i], o);
				}
			}
		}
		return o;
	}
	
	private Object updateRequest (Object o, Field[] fields, String tableName) {
		String updateRequest = "UPDATE " + tableName + " SET ";
		int nbField = 0;
		for (int i = 0 ; i < fields.length ; i++) {
			if (this.isTableField(fields[i], o) && !this.isPrimaryField(fields[i])) {
				nbField++;
			}
		}
		int fieldViewed = 0;
		for (int i = 0 ; i < fields.length ; i++) {
			if (this.isTableField(fields[i], o) && !this.isPrimaryField(fields[i])) {
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
					if (fields[i].isAnnotationPresent(ORM_RELATION.class)) {
						if (this.isTableField(fields[i], o)) {
							this.addPreparedField(prepare, indice, this.getPrimaryField(this.getListClass(fields[i]).getDeclaredFields()), this.getFieldValue(fields[i], o));
							indice++;
						}
					} else {
						this.addPreparedField(prepare, indice, fields[i], o);
						indice++;
					}
				}
			}
			this.addPreparedField(prepare, indice, field, o);
			System.out.println(updateRequest);
	        prepare.execute();
	        prepare.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
		for (int i = 0 ; i < fields.length ; i++) {
			if (fields[i].isAnnotationPresent(ORM_RELATION.class)) {
				if (fields[i].getAnnotation(ORM_RELATION.class).value().equals("manyToMany")) {
					if (this.getFieldValue(fields[i], o) != null) {
						this.updateListObject(fields[i], o);
					}
				} else if (fields[i].getAnnotation(ORM_RELATION.class).value().equals("oneToMany")) {
					this.updateListObjectOneMany(tableName, fields[i], o);
				}
			}
		}
		return null;
	}
	
	private Field getPrimaryField (Field[] fields) {
		for (int i = 0 ; i < fields.length ; i++) {
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
		try {
			if (field.getType().equals(Integer.class)) {
				prepare.setInt(indice, (int)this.getFieldValue(field, o));
			} else if (field.getType().equals(String.class)) {
				prepare.setString(indice, (String)this.getFieldValue(field, o));
			} else if (field.getType().equals(Date.class)) {
				try {
					SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
					prepare.setString(indice, formater.format((Date)this.getFieldValue(field, o)));
				} catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void addPreparedIdField (PreparedStatement prepare, int indice, Object id) {
		try {
			if (id.getClass().equals(Integer.class)) {
	        	prepare.setInt(1, (int) id);
	        } else if (id.getClass().equals(String.class)) {
	        	prepare.setString(1, (String) id);
	        }
		} catch (Exception e) {
            e.printStackTrace();
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
		try {
			if (field.getType().equals(Integer.class)) {
				field.set(o, result.getInt(field.getName()));
			} else if (field.getType().equals(String.class)) {
				field.set(o, result.getString(field.getName()));
			}
		} catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private List setListAttributValue (Field field, Object o, String value) {
		Class listClass = this.getListClass(field);
		Field[] fields = listClass.getDeclaredFields();
		Field primaryField = this.getPrimaryField(o.getClass().getDeclaredFields());
		List list = null;
		if (value.equals("oneToMany")) {
			String[] projection = new String[fields.length];
			for (int i = 0 ; i < fields.length ; i++) {
				projection[i] = fields[i].getName();
			}
			Map<String, Object> where = new HashMap<>();
			
			where.put(primaryField.getName(), this.getFieldValue(primaryField, o));
			list = this._find(listClass, projection, where, null, null, null);
		} else {
			String tableName = this.getTableName((Class<Object>)o.getClass());
			String assocTableName = this.getTableName(listClass);
			Field listPrimaryField = this.getPrimaryField(listClass.getDeclaredFields());
			String assocField = listPrimaryField.getName() + "_" + assocTableName;
			String selectAssocTable = "SELECT "+assocField;
			selectAssocTable += " FROM " + tableName + "_" + assocTableName + " WHERE ";
			selectAssocTable += primaryField.getName() + "_" + tableName  + " = ?";
			try {
	            PreparedStatement prepare = connectionMySQL.prepareStatement(selectAssocTable);
	            this.addPreparedField(prepare, 1, primaryField, o);
	            ResultSet result = prepare.executeQuery();
	            list = new ArrayList<>();
		        while (result.next()) {
		        	list.add(this._load(listClass, result.getObject(assocField)));
		        }
	            prepare.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		return list;
	}
	
	private Class getClassFromList (Field field) {
		Class classList = null;
		if(field.getType().equals(List.class)) {
			try {
				Object o = field.getType().newInstance();
				List list = (List) field.get(o);
				System.out.println("List class : " + list.toArray().getClass());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return classList;
	}
	
	private Class getListClass (Field field) {
		Class listClass = null;
		if (field.isAnnotationPresent(ORM_CLASSNAME.class)) {
			try {
				listClass = Class.forName(field.getAnnotation(ORM_CLASSNAME.class).value());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			listClass = field.getType();
		}
		return listClass;
	}
	
	private void insertListObject (Field field, Object object) {
		Class listClass = this.getListClass(field);
		Field primaryField1 = this.getPrimaryField(object.getClass().getDeclaredFields());
		Field primaryField2 = this.getPrimaryField(listClass.getDeclaredFields());
		String insertRequest = "INSERT INTO " + this.getTableName((Class<Object>)object.getClass()) + "_" + this.getTableName(listClass) + " (";
		insertRequest += primaryField1.getName() + "_" + this.getTableName((Class<Object>)object.getClass()) + "," + primaryField2.getName() + "_" + this.getTableName(listClass);
		insertRequest += ") VALUES (?,?)";
		if (field.getType().equals(List.class)) {
			try {
				List list = (List)field.get(object);
				for (Object o : list) {
					try {
			            PreparedStatement prepare = connectionMySQL.prepareStatement(insertRequest);
			            this.addPreparedField(prepare, 1, primaryField1, object);
			            this.addPreparedField(prepare, 2, primaryField2, o);
			            prepare.execute();
			            prepare.close();
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateListObject (Field field, Object object) {
		Class listClass = this.getListClass(field);
		Field primaryField1 = this.getPrimaryField(object.getClass().getDeclaredFields());
		Field primaryField2 = this.getPrimaryField(listClass.getDeclaredFields());
		String tableName = this.getTableName((Class<Object>)object.getClass()) + "_" + this.getTableName(listClass);
		String field1 = primaryField1.getName() + "_" + this.getTableName((Class<Object>)object.getClass());
		String field2 = primaryField2.getName() + "_" + this.getTableName(listClass);
		String selectRequest = "SELECT * FROM " + tableName;
		selectRequest += " WHERE " + field1+ " = ?";
		List list = (List)this.getFieldValue(field, object);
		try {
	        PreparedStatement prepare = connectionMySQL.prepareStatement(selectRequest);
			this.addPreparedField(prepare, 1, primaryField1, object);
			ResultSet result = prepare.executeQuery();
        	System.out.println("list obj : " + list);
	        while (result.next()) {
	        	Object id = result.getObject(field2);
	        	Object o = this._load(listClass, id);
	        	System.out.println("list obj load : " + o);
	        	if (!list.contains(o)) {
	        		String deleteRequest = "DELETE FROM "+ tableName + " WHERE ";
	        		deleteRequest += field1 + " = ? AND " + field2 + " = ?";
	        		try {
	                    PreparedStatement deletePrepare = connectionMySQL.prepareStatement(deleteRequest);
	                    this.addPreparedField(deletePrepare, 1, primaryField1, object);
	                    deletePrepare.setString(2,  id.toString());
	                    deletePrepare.execute();
	        		} catch (Exception e) {
	                    e.printStackTrace();
	                }
	        	}
	        }
	        result.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void updateListObjectOneMany (String tableName, Field field, Object o) {
		Object fieldObj = this.getFieldValue(field, o);
		if (fieldObj != null) {
			List list = (List) fieldObj;
			for (Object obj : list) {
				obj = this._save(obj);
				String update = "UPDATE " + this.getTableName((Class<Object>)obj.getClass()) + " SET ";
				update += this.getPrimaryField(o.getClass().getDeclaredFields()).getName() + "_" + tableName + " = ?";
				update += " WHERE " + this.getPrimaryField(obj.getClass().getDeclaredFields()).getName() + " = ?";
				try {
					PreparedStatement prepare = connectionMySQL.prepareStatement(update);
					this.addPreparedField(prepare, 1, this.getPrimaryField(o.getClass().getDeclaredFields()), o);
					this.addPreparedField(prepare, 2, this.getPrimaryField(obj.getClass().getDeclaredFields()), obj);
			        prepare.execute();
			        prepare.close();
				} catch (Exception e) {
		            e.printStackTrace();
		        }
			}
		}
	}
	
	private boolean isTableField (Field field, Object o) {
		return this.getFieldValue(field, o) != null && 
				(!field.isAnnotationPresent(ORM_RELATION.class) || 
					(field.isAnnotationPresent(ORM_RELATION.class) && 
						(field.getAnnotation(ORM_RELATION.class).value().equals("oneToOne") ||
							field.getAnnotation(ORM_RELATION.class).value().equals("manyToOne"))));
	}
	
	private void addForeignKey (String tableName, Field field, String refTableName, Field refField) {
		String sql = "ALTER TABLE " + tableName + 
				" ADD CONSTRAINT fk_"+ tableName + "_" + field.getName() +
				" FOREIGN KEY ("+field.getName()+") REFERENCES " +
				refTableName + "("+refField.getName()+")";
		if (field.isAnnotationPresent(ORM_COMPOSITION.class)) {
			sql += " ON DELETE CASCADE";
		}
		System.out.println("foreign key : " + sql);
		try {
            PreparedStatement prepare = connectionMySQL.prepareStatement(sql);
            prepare.execute();
            prepare.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}