


$(function(){
	Esgi.module = Esgi.module || {}
	Esgi.module.user = Esgi.module.user || {}

	Esgi.module.user.AdminCommentPanel = function (cfg) {

		new  shan.html.panel({
			url : APP_CONTEXT+'/user/connect',
			renderTo : cfg.id,
			inputs : [
			          {
			        	  type : "AdminCommentPanel",
			        	  comment : cfg.commentContent,
			        	  imgPath: '/EsgiRouter/res/img/'+cfg.filePath,
			        	  result:cfg
			          }

			          ]
		});
		
	}

});

