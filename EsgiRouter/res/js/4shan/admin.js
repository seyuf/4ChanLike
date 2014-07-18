
$(function(){
	Esgi.module = Esgi.module || {}
	Esgi.module.user = Esgi.module.user || {}

	Esgi.module.user.Admin = function (cfg) {
		
		new Esgi.html.Form({
			url : APP_CONTEXT+'/admin/admin/',
			renderTo : cfg.id,
			inputs : [
			          {
			        	  type : "Text",
			        	  name : 'comment',
			        	  label : 'titre du sujet' 
			          }
			          ,{
			        	  type : "find",
			        	  name : 'submit',
			        	  label: 'Post'  
			          }
			          ],
			 
		});
	}

});

