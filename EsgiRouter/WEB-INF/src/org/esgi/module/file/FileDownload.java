package org.esgi.module.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;

public class FileDownload implements IAction {

	@Override
	public void execute(IContext context) throws Exception {
		
		String filepath = context.getProperties().getProperty("controllerPath").replace("/file/download/","") ;
		filepath =context.getProperties().getProperty("realPath")+"/"+filepath;
		System.out.println("bobo:"+ context.getParameter("path")+" "+filepath);
		File f = new File(filepath);
		if (f != null) {
			String url = URLConnection.guessContentTypeFromName(filepath);
			context.getResponse().setContentType(url);
			 context.getResponse().setHeader("Content-disposition","attachment; filename="+f.getName());
	         OutputStream out = context.getResponse().getOutputStream();
	         FileInputStream in = new FileInputStream(f);
	         byte[] buffer = new byte[4096];
	         int length;
	         while ((length = in.read(buffer)) > 0){
	            out.write(buffer, 0, length);
	         }
	         in.close();
	         out.flush();
		}
		//context.getResponse().setContentType("text/html");
		
		
	}

	@Override
	public String getRoute() {
		return "/file/download(/.+)?/"; //(.*)(.ini|txt|gz|jar|war|vm|css|js)";
	}

	@Override
	public String getLayout() {
		// TODO Auto-generated method stub
		 return null;//"file/file_download.vm";
	}

	@Override
	public String[] getRewriteGroups() {
		// TODO Auto-generated method stub
		return new String[]{"path"};
	}
	
}
