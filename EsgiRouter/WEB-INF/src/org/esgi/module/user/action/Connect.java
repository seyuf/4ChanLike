package org.esgi.module.user.action;

import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.User;
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
		ORM.createTable(User.class);
		User toto = new User();
		toto.roles = null;
	    toto.login = (context.getRequest().getParameter("login"));
	    toto.password = context.getRequest().getParameter("password");
	    System.out.println( ORM.save(toto));
		
	}
}
