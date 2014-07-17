package org.esgi.module.user.action;

import java.io.Console;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esgi.orm.my.ORM;
import org.esgi.orm.my.model.File;
import org.esgi.orm.my.model.User;
import org.esgi.orm.my.model.Comment;
import org.esgi.orm.my.model.Subject;
import org.esgi.web.action.AbstractAction;
import org.esgi.web.action.IContext;


public class Connect extends AbstractAction{
	@Override
	public String getRoute() {
		return "/user/connect";
	}
	@Override
	public String getLayout() {
		return null; //"file/file_list.vm";
	}
	@Override
	public void execute(IContext context) throws Exception {
		
		String pseudo = context.getRequest().getParameter("pseudo");
		String email = context.getRequest().getParameter("email");
		String filePath = context.getRequest().getParameter("image");
		String subjectName = context.getRequest().getParameter("subject");
		String comment = context.getRequest().getParameter("comment");
		String subjectID = context.getRequest().getParameter("commentId");
		
		//omment remov = new Comment();
		ORM.remove((Class<Object>)(Object)Comment.class, 9);
		
		if(subjectID != null){
			System.out.println("must set comment");
			String created = this.newComment(pseudo, email, subjectName, comment, filePath, subjectID);
			if(created == null){
				created = "{\"result\": \"the comment already exist\",\"error\":true}";
			}else{
				created = "{\"result\":"+created+", \"error\":false}";
			}

			context.getResponse().setContentType("application/json");
			context.getResponse().getWriter().write(created);
			
		}else{
			String created = newSubject(pseudo, email, subjectName, comment, filePath);
			if(created == null){
				created = "{\"result\": \"the subject already exist\",\"error\":true}";
			}else{
				created = "{\"result\":"+created+", \"error\":false}";
			}

			context.getResponse().setContentType("application/json");
			context.getResponse().getWriter().write(created);

		}

	}
	
	
	// set new subject with comments
	private String newSubject(String pseudo, String email, String subjectTitle, String CommentText, String filePath){
		
		
		Subject testSub = (Subject) this.databaseIntegrity((Class<Object>)(Object)Subject.class, "subjectName", subjectTitle);
		if(testSub != null){
			return null;
		}
		

		User toto;
		toto = (User) databaseIntegrity((Class<Object>)(Object)User.class, "userMail", email);
		if(toto == null){

			// set user
			toto = new User();
			toto.setPseudo(pseudo);
			toto.setMail(email);
			try{
				ORM.save(toto);
			}catch(Exception e){
				System.out.println("Expection while saving user ******"+e.getMessage());
				System.exit(0);

			}
		}

		// set subgject
		Subject sub = new Subject();
		sub.setName(subjectTitle);
		java.util.Date now = new java.util.Date();
		sub.setDate(new java.sql.Date(now.getTime())); 
		sub.userId = toto;
		try{
			System.out.println( ORM.save(sub));
		}catch(Exception e){
			System.out.println("Exception subject *****"+e.getMessage());
			System.exit(0);
		}

		// init comment
		Comment com = new Comment();
		com.setContent(CommentText);	
		try{
			System.out.println( ORM.save(com));
		}catch(Exception e){
			System.out.println("Exception comment *****"+e.getMessage());
			System.exit(0);
		}
		
		// init file
		File testFile =  new File();
		testFile.setPath(filePath);
		testFile.commentId = com;

		try{
			System.out.println( ORM.save(testFile));
		}catch(Exception e){
			System.out.println("subject exception"+e.getMessage());
			System.exit(0);

		}

		// update comment file and subject field
		com.addSubject(sub);
		com.setFile(testFile);
		try{
			System.out.println( ORM.save(com));
		}catch(Exception e){
			System.out.println("comment Exception"+e.getMessage());
			System.exit(0);

		}


		
			return "{\"subjectName\":\""+sub.subjectName+"\", \"subjectId\":\""+sub.getId()+"\", \"commentId\":\""+com.getId()+"\",\"commentContent\":\""+com.getContent()
					+"\",\"userName\":\""+toto.getPseudo()+"\",\"filePath\":\""+testFile.getPath()+"\"}";
		

		
	}
	//tableIn.class
	private Object databaseIntegrity(Class<Object> tableIn, String field, String value) {
		
		try{
			Map<String, Object> where = new HashMap<>();
			where.put(field, value);
			List<Object> checkSub= ORM.find(tableIn,new String[]{"*"}, where, null, null,null);
			System.out.println("isempty: "+checkSub.isEmpty());
			if (!checkSub.isEmpty() ){
				return checkSub.get(0);
			}
			else {
				return null;
			}
		}catch(Exception e){
			System.out.println("Ex in checking data integrity"+e.getMessage());
			return null;
		}
		
	}
	
	//add net comment
private String newComment(String pseudo, String email, String subjectTitle, String CommentText, String filePath, String subjectId){
		
		// if we were paid check if the subject exist 
	
		User toto;
		toto = (User) this.databaseIntegrity((Class<Object>)(Object)User.class, "userMail", email);
		//TODO update user pseudo
		if(toto == null){

			// set user
			toto = new User();
			toto.setPseudo(pseudo);
			toto.setMail(email);
			try{
				ORM.save(toto);
			}catch(Exception e){
				System.out.println("Expection while saving user ******"+e.getMessage());
				System.exit(0);

			}
		}

		// set subgject
		Subject sub = new Subject();
		sub.subjectID = Integer.valueOf(subjectId);
		sub.setName(subjectTitle);
		java.util.Date now = new java.util.Date();
		sub.setDate(new java.sql.Date(now.getTime())); 
		sub.userId = toto;
		

		// init comment
		Comment com = new Comment();
		com.setContent(CommentText);	
		try{
			System.out.println( ORM.save(com));
		}catch(Exception e){
			System.out.println("Exception comment *****"+e.getMessage());
			System.exit(0);
		}
		
		// init file
		File testFile =  new File();
		testFile.setPath(filePath);
		testFile.commentId = com;

		try{
			System.out.println( ORM.save(testFile));
		}catch(Exception e){
			System.out.println("subject exception"+e.getMessage());
			System.exit(0);

		}

		// update comment file and subject field
		com.addSubject(sub);
		com.setFile(testFile);
		try{
			System.out.println( ORM.save(com));
		}catch(Exception e){
			System.out.println("comment Exception"+e.getMessage());
			System.exit(0);

		}



		return "{\"subjectId\":\""+subjectId+"\", \"commentId\":\""+com.getId()+"\",\"commentContent\":\""+com.getContent()
				+"\",\"userName\":\""+toto.getPseudo()+"\",\"filePath\":\""+testFile.getPath()+"\"}";
	}
}
