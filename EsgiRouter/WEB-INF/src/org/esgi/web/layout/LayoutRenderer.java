package org.esgi.web.layout;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import org.esgi.json.JSONExtractorClass;
import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;
import org.esgi.web.route.Router;

public class LayoutRenderer {
	
	public void render(IAction action, IContext context, Router router) {
	
		// TODO Load JSON Layout, 
		// execute each action according to the dependency tree.
		// For each each action, if a template exists, execute it with the context action.
		// And set result in fragment.
		//List<String> list = new ArrayList<String>();
	
		//System.out.println("json"+context.getRequest().getServletContext().getRealPath("/")+"layout/default.js");
		List<String> list = new LinkedList<String>() ;
		/*
		list.add("__CURRENT_");
		list.add("shared/main_footer.vm");
		list.add("temlates :shared/main_header.vm");
		list.add("shared/html");
		*/
		
		try {
			list = JSONExtractorClass.getDependencies(context.getRequest().getServletContext().getRealPath("/")+"layout/default.js");
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for (String value : list) {
			
			
		
			IAction a = null;
			if (value.equalsIgnoreCase("__CURRENT__")) {
				//a = router.find(action.getRoute(), context);
				a  = router.find(action.getRoute(), context);
			}
			
			if (a != null) {
				try {
					a.execute(context);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if ((a!= null && a.getLayout() != null)) {
				VelocityRender velocity = new VelocityRender();
				String[] parts = a.getClass().getName().split("\\.");
				String view = parts[parts.length-2]+"/"+parts[parts.length-1].toLowerCase();
				StringWriter sw = velocity.execute(context, view);
				// Velocity.merge.....
				context.setFragment(value, sw);
			} else {
				VelocityRender velocity = new VelocityRender();
				
				StringWriter sw = velocity.execute(context, value);
				// Velocity.merge.....
				context.setFragment(value, sw);
			}
			
			
		}

		//System.out.println("Renderer : " + (String)context.getFragment(list.get(list.size()-1)).toString());
		
		try {
			context.getResponse().getWriter().write(context.getFragment(list.get(list.size()-1)).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
