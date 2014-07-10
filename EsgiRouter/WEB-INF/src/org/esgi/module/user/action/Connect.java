package org.esgi.module.user.action;

import java.sql.Date;

import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.User;
import org.esgi.orm.my.model.Comment;
import org.esgi.orm.my.model.Subject;
import org.esgi.web.action.AbstractAction;
import org.esgi.web.action.IContext;

public class Connect extends AbstractAction{
	@Override
	public String getRoute() {
		return "/user/connect";
	}
	@Override
	public String getLayout() {
		return null; //"file/file_list.vm";
	}
	@Override
	public void execute(IContext context) throws Exception {
		
		System.out.println(context.getRequest().getParameter("login"));
		//ORM.createTable(User.class);
		User toto = new User();
		Comment com = new Comment();
		Subject sub = new Subject();
		
		toto.setPseudo(context.getRequest().getParameter("pseudo"));
		//toto.userPseudo = context.getRequest().getParameter("pseudo");
		System.out.println("request pseudo!!!!!!!"+context.getRequest().getParameter("pseudo"));
		toto.setMail(context.getRequest().getParameter("email"));
		try{
		System.out.println( ORM.save(toto) );
		}catch(Exception e){
			System.out.println("User exception"+e.getMessage());
		}
		
		System.out.println("user id is:"+toto.getId());
		
		sub.setName("toto");
		sub.setDate(Date.valueOf("2014-07-09")); 
		sub.userId = toto;
		try{
		System.out.println( ORM.save(sub));
		}catch(Exception e){
			System.out.println("subject exception"+e.getMessage());
		}

		
		com.setContent(context.getRequest().getParameter("comment"));
		com.addSubject(sub);
		try{
		System.out.println( ORM.save(com));
		}catch(Exception e){
			System.out.println("comment Exception"+e.getMessage());
		}

		
		//System.out.println( ORM.save(toto));
		//System.out.println( ORM.save(sub));

		/*toto.roles = null;
	    toto.login = (context.getRequest().getParameter("login"));
	    toto.password = context.getRequest().getParameter("password");
	    System.out.println( ORM.save(toto));*/
		
	}
}
