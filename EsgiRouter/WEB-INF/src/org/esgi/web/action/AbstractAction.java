package org.esgi.web.action;

public class AbstractAction implements IAction {

	@Override
	public void execute(IContext context) throws Exception {
		System.out.println("default exec");
	}

	@Override
	public String getRoute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRewriteGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLayout() {
		return "default";
	}
}
