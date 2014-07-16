
$(function(){
Esgi.module = Esgi.module || {}
Esgi.module.user = Esgi.module.user || {}

Esgi.module.user.Panel = function (cfg) {
	console.log("panel sub config",cfg);
	if(cfg.error){
		return;
	}
	///if(cfg.id == undefined)
		cfg.id = "#MainPanel";
	
	if( undefined == cfg.comments ){
		
        new  shan.html.panel({
             url : APP_CONTEXT+'/user/connect',
             renderTo : cfg.id,
             inputs : [
               {
                  type : "show",
                  comment : cfg.commentContent,
                  imgPath: '/EsgiRouter/res/img/'+cfg.filePath,
                  result:cfg
                }
                
             ]
        });
	}else{
		
		 new  shan.html.panel({
             url : APP_CONTEXT+'/user/connect',
             renderTo : cfg.id,
             inputs : [
               {
                  type : "show",
                  comment : cfg.comments[0].comment,
                  imgPath: '/EsgiRouter/res/img/'+cfg.comments[0].filePath,
                  result:cfg
                }
                
             ]
        });
		 for(var i = 1; i<cfg.comments.length; i++){
			 var comment  = {
					 commentContent: cfg.comments[i].comment,
					 filePath:cfg.comments[i].filePath,
					 id: "#subject"+cfg.subjectId,
					 subjectId: cfg.subjectId,
					 userName: cfg.userName
			 }
			 console.log("comment"+i, comment.imgPath);
			 new Esgi.module.user.commentPanel(comment, comment.subjectId);
		 }
	}
}

});

