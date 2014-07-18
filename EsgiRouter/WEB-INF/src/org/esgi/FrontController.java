package org.esgi;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.esgi.module.file.FileDelete;
import org.esgi.module.file.FileDownload;
import org.esgi.module.file.FileList;
import org.esgi.module.file.FileUpload;
import org.esgi.module.index.Index;
import org.esgi.module.user.action.Admin;
import org.esgi.module.user.action.Connect;
import org.esgi.module.user.action.UserCreate;
import org.esgi.module.user.action.UserList;
import org.esgi.web.action.Context;
import org.esgi.web.action.IAction;
import org.esgi.web.action.IContext;
import org.esgi.web.layout.LayoutRenderer;
import org.esgi.web.route.Router;

/**
 * Simulate an Servlet.
 * @author michaelthomas
 *
 */
public class FrontController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	Router router = new Router();
	Properties properties = new Properties();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String configFile = config.getServletContext().getInitParameter("config");
		
		String path = config.getServletContext().getRealPath("/");

		try {
			properties.load(new FileInputStream(path +"/" + configFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
		config.getServletContext().getRealPath(path);
		properties.put("realPath", config.getServletContext().getRealPath("/"));
		properties.put("serverRoot", config.getServletContext().getContextPath());
		
		registerAction(new UserCreate());
		registerAction(new UserList());
		registerAction(new FileDelete());
		registerAction(new FileDownload());
		registerAction(new FileUpload());
		registerAction(new FileList());
		registerAction(new Index());
		registerAction(new Admin());
		registerAction(new Connect());
		
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String url = request.getPathInfo();
		properties.put("controllerPath", request.getPathInfo());
		IContext context = createContext(request, response);
		IAction action = router.find(url, context);
		System.out.println("action : " + action);
		if (null != action){
			if (null == action.getLayout()) {
				try {
					action.execute(context);
				} catch (Exception e) {
					throw new ServletException(e);
				}
			} else {
				
				LayoutRenderer layoutRenderer= new LayoutRenderer();
				layoutRenderer.render(action, context, this.router);
			}
		}

	}

	private IContext createContext(HttpServletRequest request, HttpServletResponse response) {
		return new Context(request, response, this.properties);
	}

	public void registerAction(IAction action) {
		router.register(action);
	}
}
