package org.esgi.web.layout;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.esgi.web.action.IContext;

public class VelocityRender {
	
	public StringWriter execute(IContext context, String file) {
		
		//String [] splited =  file.split("/");
		file+=".vm";
		System.out.println("velo file:"+file);
	
		//System.exit(0);
		Properties p = new Properties();
	    p.setProperty("file.resource.loader.path", ((String) context.getRequest().getServletContext().getRealPath("/")) + "view/");
	    Velocity.init( p );
	    
		VelocityContext velocityContext = new VelocityContext();
	
		/*velocityContext.put( "name", new String("Velocity") );*/
		/*velocityContext.put( "context", IContext.class );*/
		/*velocityContext.*/
		velocityContext.put("context", context );
		//velocityContext.put("test", new String("toto") );
	
		Template template = null;
		
	
		try
		{
		   template = Velocity.getTemplate(file);
		}
		catch( ResourceNotFoundException rnfe )
		{
			System.out.println("template not found");
			
		   // couldn't find the template
		}
		catch( ParseErrorException pee )
		{
		  // syntax error: problem parsing the template
		}
		catch( MethodInvocationException mie )
		{
		  // something invoked in the template
		  // threw an exception
		}
		catch( Exception e )
		{}
	
		StringWriter sw = new StringWriter();
	
		template.merge( velocityContext, sw );
		//System.out.println(sw);
		return sw;
	}

}
