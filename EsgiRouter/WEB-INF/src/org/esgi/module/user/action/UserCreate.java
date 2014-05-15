package org.esgi.module.user.action;

import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;

public class UserCreate implements IAction {

	public String getRoute() {
		return "user/create";
	}
	
	public void execute(IContext context) {
		// TODO Auto-generated method stub
		System.out.println("In User Create");
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
