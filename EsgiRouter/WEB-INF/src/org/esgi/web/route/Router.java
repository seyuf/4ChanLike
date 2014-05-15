package org.esgi.web.route;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;

public class Router {

private static Map<String, IAction> _actions = new HashMap<String, IAction>();
	
	public IAction find(String url, IContext context) {
		IAction action = _actions.get(url);
		if (null != action)
			return action;
		Pattern p;
		for (IAction anAction : _actions.values()) {
			p = Pattern.compile(anAction.getRoute());
			Matcher m = p.matcher(url);
			if (m.find()) {
				String[] groupsName = anAction.getRewriteGroups();
				if (null != groupsName)
				   for (int i=1; i<=groupsName.length && i<=m.groupCount(); i++)
					context.setParameter(groupsName[i-1], m.group(i));
				return anAction;
			}
		}
		return null;
	}
	
	public void register(IAction action){
		_actions.put(action.getRoute(), action);
	}
	

}
