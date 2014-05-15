package org.esgi.module.user.action;

import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;

public class UserList implements IAction{
	
	public String getRoute() {
		return "user/list";
	}

	public void execute(IContext context) {
		System.out.println("In User List");
		
	}

	@Override
	public String getLayout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRewriteGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
