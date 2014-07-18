package org.esgi.module.index;

import org.esgi.web.action.AbstractAction;
import org.esgi.web.action.IContext;

import java.io.Console;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.Categorie;
import org.esgi.orm.my.model.File;
import org.esgi.orm.my.model.User;
import org.esgi.orm.my.model.Comment;
import org.esgi.orm.my.model.Subject;
import org.esgi.web.action.AbstractAction;
import org.esgi.web.action.IContext;

public class Index extends AbstractAction{

	@Override
	public String getRoute() {
		return "/index/(.*)?/";
	}

	@Override
	public String[] getRewriteGroups() {
		return new String[]{"path"};
	}


	public void execute(IContext context) throws Exception {
		context.getResponse().setContentType("text/html");
		try{
			context.setSubjects((ArrayList<String>) getSubjectList((String)context.getParameter("path")));

		}catch(Exception e){


		}
	}



	private String getCommentsFromSubject(int subjectId) {
		Map<String, Object> where = new HashMap<>();
		where.put("subjectId", subjectId);
		List<Object> checkCom= ORM.find((Class<Object>)(Object)Comment.class,new String[]{"*"}, where, new String[]{"fileId"}, 10,0);
		if ( !checkCom.isEmpty() ){
			StringBuffer comments = new StringBuffer("[");

			int sizeCom = checkCom.size();
			for (int i = 0; i < sizeCom ; i++) {
				Comment com = (Comment) checkCom.get(i);
				comments.append("{\"comment\":\"");
				comments.append(com.getContent());

				String file = null;
				if((file = getFileFromComment(com.getId()))!= null){
					comments.append("\", \"filePath\":\""+file+"\"}");

				}else{
					comments.append("\"}");
				}
				if(i == (sizeCom -1)){

				}else{
					comments.append(",");
				}
			}
			comments.append("]");
			return comments.toString();
		}
		else {
			return("{\"error\":\"there is no comment\"}");

		}

	}


	private String getFileFromComment(int commentId) {
		Map<String, Object> where = new HashMap<>();
		where.put("commentId", commentId);
		List<Object> checkFile= ORM.find((Class<Object>)(Object)File.class,new String[]{"*"}, where, null, null, null);
		if (!checkFile.isEmpty() ){

			return ((File)checkFile.get(0)).getPath();
		}
		else {
			return "";

		}

	}

	private List<String> getSubjectList(String category){
		List<String>  jsonSubjects= new ArrayList<String>();

		Map<String, Object> where = new HashMap<>();
		where.put("categorieName", category);
		Categorie catego = null;
		List<Object> cat = ORM.find((Class<Object>)(Object)Categorie.class,new String[]{"*"}, where, null, null,null);
		if (!cat.isEmpty() ){
			catego = (Categorie) cat.get(0);
		}else{
			jsonSubjects.add("{\"error\":\"there is no subject\"}");
			return jsonSubjects;
		}

		Map<String, Object> whereB = new HashMap<>();
		whereB.put("categorieId", catego.getId());
		List<Object> checkSub= ORM.find((Class<Object>)(Object)Subject.class,new String[]{"*"}, whereB, new String[]{"date"}, 10,0);
		if (!checkSub.isEmpty() ){
			for (int i = 0; i < checkSub.size(); i++) {
				Subject subIter = (Subject) checkSub.get(i);
				StringBuffer subRes = new StringBuffer("{\"subjectId\":\""+subIter.getId()+"\",\"subjectName\":\""+subIter.getName()
						+"\",\"comments\": ");
				subRes.append(getCommentsFromSubject(subIter.getId()));
				subRes.append("}");
				jsonSubjects.add(subRes.toString());
			}
			return jsonSubjects;
		}
		else {
			jsonSubjects.add("{\"error\":\"there is no subject\"}");
			return jsonSubjects;
		}
	}

}
