package org.esgi.orm.my;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esgi.orm.my.model.Role;
import org.esgi.orm.my.model.User;



public class Application {
	public static void main(String[] args) {
		User u = new User(), u2;

		System.out.println(Arrays.toString(User.class.getAnnotations()));
		System.out.println(Arrays.toString(u.getClass().getDeclaredAnnotations()));
		ORM.createTable(User.class);
		u.roles = null;
		u = (User) ORM.save(u);
		System.out.println(u);
		u.password = "TOTO";
		ORM.save(u);
		u2 = (User) ORM.load(User.class, u.id);
		System.out.println(u2); // Display toto as password.
		
		System.out.println("FALSE "+(u == u2));
		
		System.out.println(Arrays.toString(User.class.getDeclaredFields()));
		
		String[] projection = {"id", "login", "password"};
		Map<String, Object> where = new HashMap<>();
		where.put("password", "TOTO");
		String[] orderby = {"password"};
		List<Object> users = ORM.find(User.class, projection, where, orderby, 4, null);
		System.out.println(users);
		
		u.id = 8;
		ORM.remove(User.class, u.id);
		
		ORM.createTable(Role.class);
		Role role1 = new Role();
		role1.id = "admin";
		role1.value = "Administrateur";
		ORM.save(role1);
		System.out.println(role1);
		Role role2 = new Role();
		role2.id = "user";
		role2.value = "utilisateur";
		ORM.save(role2);
		System.out.println(role2);
		
		/*u2.roles.add(role1);
		u2.roles.add(role1);
		ORM.save(u2);
		
		User u3 = (User) ORM.load(User.class, u2.id);
		System.out.println(u3); // Display toto as password.*/
		
	}
}
