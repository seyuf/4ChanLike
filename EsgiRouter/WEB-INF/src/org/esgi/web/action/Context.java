package org.esgi.web.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Context implements IContext {
	
	public HttpServletRequest request;
	public HttpServletResponse response;
	public Properties properties;
	public Map<String, Object> mapParameters = new HashMap<>();
	public Map<String, Object> mapFragment = new HashMap<>();
	public Map<String, Object> mapAttribut = new HashMap<>();
	public List<String> onJsReady = new ArrayList<>();
	public List<String> jsUrls = new ArrayList<>();
	public List<String> subjectsData = new ArrayList<>();
	
	
	public void setSubjects(ArrayList<String> subjectIn ) {
		this.subjectsData = subjectIn;
	}
	public List<String> getSubjects() {
		return subjectsData;
	}

	public Context (HttpServletRequest request, HttpServletResponse response, Properties properties) {
		this.request = request;
		this.response = response;
		this.properties = properties;
	}

	
	@Override
	public Object getAttribute(String key) {
		return this.mapAttribut.get(key);
	}

	@Override
	public void setAttribute(String key, Object o) {
		this.mapAttribut.put(key, o);
	}
	
	@Override
	public Object getParameter(String key) {
		return mapParameters.get(key);
	}

	@Override
	public void setParameter(String key, Object o) {
		mapParameters.put(key, o);
	}

	@Override
	public HttpServletRequest getRequest() {
		return this.request;
	}

	@Override
	public HttpServletResponse getResponse() {
		return this.response;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}
	
	@Override
	public Object getFragment(String fragment) {
		return mapFragment.get(fragment);
	}
	
	@Override
	public void setFragment(String fragment, Object o) {
		mapFragment.put(fragment, o);
	}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return this.getTitle();
	}
	
	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		 mapAttribut.put("title", title);
	}

	

	@Override
	public void setJsDependency(String scriptPath) {
		
		jsUrls.add(scriptPath);
		/*
		// TODO Auto-generated method stub
		String scriptList = new String();
		String root = this.getProperties().getProperty("serverRoot");
		//File repo = new File( this.getProperties().getProperty("realPath")+scriptPath+"/");
		File repo = new File( this.getProperties().getProperty("realPath")+"res/"+scriptPath+"/");
		//System.out.println("im on depen "+this.getProperties().getProperty("realPath")+scriptPath);
		//System.exit(0);
		if(repo.exists()){
			File[] list = repo.listFiles();
			if (null !=list){
				for (File child : list) {
					if( child.isFile() ){
						scriptList += "<script type=\"text/javascript\" src=\""+root+"/res/"+scriptPath+(child.getName())+"\"></script>";
						//scriptList += "<link rel=\"stylesheet\" type=\"text/css\" href=\""+root+"/res/"+cssRepo+"/"+(child.getName())+"\">";
					}
				}
			}
		}
		else{
			//System.out.println("do not exist"+ this.getProperties().getProperty("realPath")+scriptPath+"/");
			scriptList = "<script type=\"text/javascript\" src=\""+scriptPath+"\"></script>";	
		}
		//System.out.println("im on depen "+scriptList);
		//System.exit(0);
		return scriptList; 
		*/
		
	}
	

	@Override
	public void setDescription(String title) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void setKeyword(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setCssDependency(String cssRepo) {
		// TODO Auto-generated method stub
				String scriptList = new String();
				String root = this.getProperties().getProperty("serverRoot");
				File repo = new File( this.getProperties().getProperty("realPath")+"res/"+cssRepo+"/");
				//System.out.println("im on depen "+this.getProperties().getProperty("realPath")+"res/"+cssRepo+"/");
				//System.exit(0);
				if(repo.exists()){
					File[] list = repo.listFiles();
					if (null !=list){
						for (File child : list) {
							if( child.isFile() ){
								scriptList += "<link rel=\"stylesheet\" type=\"text/css\" href=\""+root+"/res/"+cssRepo+"/"+(child.getName())+"\">";
							}
						}
					}
				}
				else{
					System.out.println("do not exist"+ this.getProperties().getProperty("realPath")+cssRepo+"/");
					scriptList = "<script type=\"text/javascript\" src=\""+cssRepo+"\"></script>";	
				}
				//System.out.println("im on depen "+scriptList);
				//System.exit(0);
				return scriptList; 
				
	}
	
	
	
	@Override
	public String setInlineCSS(String cssRule) {
		// TODO Auto-generated method stub
		return "style=\""+ cssRule+"\"";
	}

	@Override
	public String addRawHeader(String r) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getjsdependency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "toto";
	}

	@Override
	public String getKeyword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCssDependency() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getPropertyInVelocity(String prop) {
		// TODO Auto-generated method stub
			return (String)( this.getProperties().getProperty("rootServer"));
		
	}
	
	
	@Override
	public void addOnJsReady(String str) {
		onJsReady.add(str);
	}
	public List<String> getOnJsReady(){
		return onJsReady;
	}


	@Override
	public List<String> getJsUrls() {
		// TODO Auto-generated method stub
		return this.jsUrls;
	}

}
