
package org.esgi.module.file;

import java.io.File;

import org.esgi.web.action.AbstractAction;
import org.esgi.web.action.IContext;

public class FileList extends AbstractAction {

	@Override
	public void execute(IContext context) throws Exception {
		//System.exit(1);
		File repo = new File((String) context.getRequest().getServletContext().getRealPath("/"));
		String path = (String) context.getParameter("path");
		
		if (null != path) {
			repo = new File(repo, path);
		}
		
		File[] list = repo.listFiles();
		context.getResponse().setContentType("text/html");
		context.setAttribute("files", list);
		
	}
	
	public String getRoute() {
		return "/file/list(/.+)?/";
		
	}

	public String[] getRewriteGroups() {
		return new String[]{"path"};
	}
	
	public String getLayout () {
		return "file/file_list.vm";
	}
}
