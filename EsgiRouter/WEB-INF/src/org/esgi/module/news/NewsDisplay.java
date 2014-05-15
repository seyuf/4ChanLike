package org.esgi.module.news;

import java.io.IOException;

import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;

public class NewsDisplay implements IAction {

	@Override
	// TODO Add Throw Exception IO.
	public void execute(IContext context) throws IOException {
		context.getResponse().getWriter().println("IN NEWS DISPLAY");
	}

	@Override
	public String getRoute() {
		return "actualite/(.+)\\.html";
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
