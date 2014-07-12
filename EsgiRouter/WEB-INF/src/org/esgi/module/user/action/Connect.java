package org.esgi.module.user.action;

import java.sql.Date;

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
		
		//System.out.println(context.getRequest().getParameter("login"));
		//ORM.createTable(User.class);
		
		Subject sub = new Subject();
		sub.subjectID = 1;
		sub = (Subject) ORM.load((Class<Object>)(Object)sub.getClass(), sub.getId());
		
		System.out.println("titre sujet"+sub.getName());
		
		/*
		User toto = new User();
		toto.setPseudo(context.getRequest().getParameter("pseudo"));
		toto.setMail(context.getRequest().getParameter("email"));
		try{
		System.out.println( ORM.save(toto) );
		}catch(Exception e){
			System.out.println("User exception"+e.getMessage());
			System.exit(0);

		}
		
		//System.out.println("user id is:"+toto.getId());
		Subject sub = new Subject();
		sub.setName("subject1");
		sub.subjectID = 1;
		sub.setDate(Date.valueOf("2014-07-09")); 
		sub.userId = toto;
		try{
		System.out.println( ORM.save(sub));
		}catch(Exception e){
			System.out.println("subject exception"+e.getMessage());
			System.exit(0);
		}
		
		//System.out.println("user id is:"+sub.getId());

		//Thread.sleep(1000);

		Comment com = new Comment();
		com.setContent(context.getRequest().getParameter("comment"));	
		try{
			System.out.println( ORM.save(com));
			}catch(Exception e){
				System.out.println("comment Exception"+e.getMessage());
				System.exit(0);
			}
		File testFile =  new File();
		testFile.setPath("/dkkde");
		testFile.commentId = com;

		try{
			System.out.println( ORM.save(testFile));
			}catch(Exception e){
				System.out.println("subject exception"+e.getMessage());
				System.exit(0);

			}

		//System.out.println("=================================>"+"comment id"+sub.getId()+" "+com.getId()+"fileId"+testFile.getId());
		com.addSubject(sub);
		com.setFile(testFile);
		
		try{
			System.out.println( ORM.save(com));
			}catch(Exception e){
				System.out.println("comment Exception"+e.getMessage());
				System.exit(0);

			}
		
		

		
		//System.out.println( ORM.save(toto));
		//System.out.println( ORM.save(sub));

		/*toto.roles = null;
	    toto.login = (context.getRequest().getParameter("login"));
	    toto.password = context.getRequest().getParameter("password");
	    System.out.println( ORM.save(toto));*/
		
	}
}
