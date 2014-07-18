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
		
		if(action.getLayout() == null){
			IAction a = null;
			
				a  = router.find(action.getRoute(), context);
			
			
			if (a != null) {
				try {
					a.execute(context);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return ;
		}
		
		
		
		List<String> list = new LinkedList<String>() ;
		
		
		
		try {
			list = JSONExtractorClass.getDependencies(context.getRequest().getServletContext().getRealPath("/")+"layout/"+action.getLayout()+".js");
			
			
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

		
		
		try {
			context.getResponse().getWriter().write(context.getFragment(list.get(list.size()-1)).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
