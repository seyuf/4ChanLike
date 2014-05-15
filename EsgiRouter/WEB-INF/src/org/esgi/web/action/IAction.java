package org.esgi.web.action;

public interface IAction {
	public void execute(IContext context) throws Exception;
	public String getRoute();
	public String getLayout();
	public String[] getRewriteGroups();
	//public Map<String, >
	
}
