package org.esgi.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IContext {
	public Object getAttribute(String key);
	public void setAttribute(String key, Object o);
	public Object getParameter(String key);
	public void setParameter(String key, Object o);
	public Properties getProperties();
	// Object instance must have a toString implementation.
	public Object getFragment(String fragment);
	public void setFragment(String fragment, Object o);
	public String getTitle();
	public void setTitle(String title);
	
	public void setJsDependency(String scriptPath);
	public List<String> getJsUrls();
	
	public String setCssDependency(String cssRepo);
	
	public String setInlineCSS(String cssRule);
	public String addRawHeader(String raw);
	
	public void setDescription(String description);
	public String getDescription();
	
	public void setKeyword(String keyword);
	public String getKeyword();
	public String getCssDependency();
	public List<String> getSubjects() ;
	public void setSubjects(ArrayList<String> subjectIn ) ;
	//public String getjsdependency();
	//public String getinlineCSS(String css);
	//public String addRawHeader();
	public HttpServletRequest getRequest();
	public HttpServletResponse getResponse();
	public String getPropertyInVelocity(String prop);
	void addOnJsReady(String str);

}