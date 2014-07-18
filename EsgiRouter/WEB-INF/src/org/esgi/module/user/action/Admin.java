package org.esgi.module.user.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.Comment;
import org.esgi.orm.my.model.File;
import org.esgi.orm.my.model.Subject;
import org.esgi.web.action.AbstractAction;
import org.esgi.web.action.IContext;



public class Admin extends AbstractAction{
	@Override
	public void execute(IContext context) throws Exception {
		// TODO Auto-generated method stub

		String content = context.getRequest().getParameter("commentContent");


		if(content != null){
			try{
				Map<String, Object> where = new HashMap<>();
				where.put("commentContent", (Object)content);
				Comment com =  (Comment) (ORM.find((Class<Object>)(Object)Comment.class, new String[]{"*"}, where, null, null, null)).get(0);

				ORM.remove((Class<Object>)(Object)Comment.class, 2);

				if(ORM.remove((Class<Object>)(Object)File.class, com.getId()))
				{
					String response = "{\"result\": \"true\",\"error\":false}";
					context.getResponse().setContentType("application/json");
					context.getResponse().setContentLength(response.length());
					context.getResponse().getWriter().write(response);
				}
			}catch(Exception e){

				String response = "{\"result\": \"false\",\"error\":true}";
				context.getResponse().setContentType("application/json");
				context.getResponse().setContentLength(response.length());
				context.getResponse().getWriter().write(response);

			}
		}


		String comment = (String)context.getParameter("path");
		context.setParameter("path", "admin");
		if(comment != null){
			try{
				context.setSubjects((ArrayList<String>) getSubjectList(comment));

			}catch(Exception e){

			}
		};

	}

	@Override
	public String getRoute() {
		// TODO Auto-generated method stub
		return "/admin/(.*)?/";
	}

	@Override
	public String[] getRewriteGroups() {
		return new String[]{"path"};
	}

	private String getCommentsFromSubject(int subjectId) {
		Map<String, Object> where = new HashMap<>();
		where.put("subjectId", subjectId);
		List<Object> checkCom= ORM.find((Class<Object>)(Object)Comment.class,new String[]{"*"}, where, new String[]{"fileId"}, 10,0);
		if ( !checkCom.isEmpty() ){
			StringBuffer comments = new StringBuffer("[");

			// [{comment:"dede",file:"path"},]
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

	private List<String> getSubjectList(String subjectName){
		List<String>  jsonSubjects= new ArrayList<String>();


		Map<String, Object> whereB = new HashMap<>();
		whereB.put("subjectName", subjectName);
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
