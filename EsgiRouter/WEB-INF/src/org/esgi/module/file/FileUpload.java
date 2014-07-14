package org.esgi.module.file;

import java.io.Console;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;

import com.fasterxml.jackson.databind.util.JSONPObject;

public class FileUpload implements IAction {

	@Override
	public void execute(IContext context) throws Exception {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(context.getRequest());
		
		if (isMultipart) {
		
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();
	
			// Configure a repository (to ensure a secure temp location is used)
			File repository = (File) context.getAttribute("javax.servlet.context.tempdir");
			factory.setRepository(repository);
	
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
	
			List<FileItem> items = null;
			// Parse the request
			try {
				items = upload.parseRequest(context.getRequest());
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			if (items != null) {
				Iterator<FileItem> iter = items.iterator();
				while (iter.hasNext()) {
				    FileItem item = iter.next();

				    if (!item.isFormField()) {
				    	File uploadedFile = new File(((String) context.getRequest().getServletContext().getRealPath("/")+"res/img"+"/"+item.getName()));
				    	
				        try {
							item.write(uploadedFile);
						} catch (Exception e) {
							e.printStackTrace();
						}
				    }
				}
			}
		}
		//context.getResponse().setContentType("text/html");
		//JSONPObject test = new JSONPObject("result", "file have bee, uploaded");
		String sended = "{result: 'file have been uploaded'}";
		context.getResponse().setContentType("text/javascript");
		context.getResponse().setContentLength(sended.length());
		context.getResponse().getWriter().write(sended);
	
		
		
	}

	@Override
	public String getRoute() {
		return "file/upload";
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
