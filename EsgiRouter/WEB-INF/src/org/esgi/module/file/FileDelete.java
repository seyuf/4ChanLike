package org.esgi.module.file;

import java.io.File;

import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;

public class FileDelete implements IAction {

	@Override
	public void execute(IContext context) throws Exception {
		
		
		String filepath = context.getRequest().getPathInfo().replace("/file/delete/","");
		filepath = context.getRequest().getServletContext().getRealPath("/")+filepath;
		filepath = "/"+filepath.substring(1,filepath.length()-1);
		
		if (filepath != null) {
			File file = new File(filepath);
			if(!file.isDirectory()){
				System.out.println("in delete");
				System.out.println(filepath);
				file.delete();
			}
		}
		
		File repo = new File((String) context.getRequest().getServletContext().getRealPath("/")+"uploaded_files");
		String path = (String) context.getParameter("path");
		
		if (null != path) {
			repo = new File(repo, path);
		}
		File[] list = repo.listFiles();
		context.getResponse().setContentType("text/html");
		context.setAttribute("files", list);
	}

	@Override
	public String getRoute() {
		return "/file/delete(/.+)?/";//(/.+)?(/(.*)(.ini|txt|gz|jar|war|vm|js))";
	}

	@Override
	public String getLayout() {
		// TODO Auto-generated method stub
		return "file/file_delete.vm";
	}
	@Override
	public String[] getRewriteGroups() {
		return new String[]{"path"};
	}
	
}
