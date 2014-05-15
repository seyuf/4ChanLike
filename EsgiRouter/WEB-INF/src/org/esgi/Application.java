package org.esgi;
import org.esgi.module.news.NewsDisplay;
import org.esgi.module.user.action.UserCreate;
import org.esgi.module.user.action.UserList;


public class Application {
	public static void main(String[] args) {
		
		FrontController front = new FrontController();
		
		front.registerAction(new UserCreate());
		front.registerAction(new UserList());
		front.registerAction(new NewsDisplay());
		/*
		front.service("user/create");
		front.service("user/list");
		front.service("actualite/kjdshlfjhsdljkhfsjqhdjlfk.html");
		front.service("actualite/une-nouvelle-actu-jlfk.html");
		*/
	}
}
