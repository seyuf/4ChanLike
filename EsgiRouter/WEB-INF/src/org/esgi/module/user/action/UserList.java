package org.esgi.module.user.action;

import org.esgi.web.action.AbstractAction;
import org.esgi.web.action.IContext;

public class UserList extends AbstractAction{
	
	public String getRoute() {
		return "user/list";
	}

	public void execute(IContext context) {
		System.out.println("In User List");
		
	}

	@Override
	public String[] getRewriteGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
