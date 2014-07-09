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
		sub.setName("toto");
		sub.setDate(Date.valueOf("2014-07-09"));
		com.setContent(context.getRequest().getParameter("comment"));
		toto.setPseudo(context.getRequest().getParameter("pseudo"));
		toto.setMail(context.getRequest().getParameter("email"));
		System.out.println( ORM.save(toto));
		System.out.println( ORM.save(sub));
		System.out.println( ORM.save(com));

		/*toto.roles = null;
	    toto.login = (context.getRequest().getParameter("login"));
	    toto.password = context.getRequest().getParameter("password");
	    System.out.println( ORM.save(toto));*/
		
	}
}
