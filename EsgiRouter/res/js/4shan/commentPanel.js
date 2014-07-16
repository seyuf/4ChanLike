
$(function(){
	Esgi.module = Esgi.module || {}
	Esgi.module.user = Esgi.module.user || {}

	Esgi.module.user.commentPanel = function (cfg) {
		console.log("comment Panel",cfg.filePath);

		new  shan.html.panel({
			url : APP_CONTEXT+'/user/connect',
			renderTo : cfg.id,
			inputs : [
			          {
			        	  type : "comment",
			        	  comment : cfg.commentContent,
			        	  imgPath: '/EsgiRouter/res/img/'+cfg.filePath,
			        	  result:cfg
			          }

			          ]
		});
		
	}

});

