
$(function(){
Esgi.module = Esgi.module || {}
Esgi.module.user = Esgi.module.user || {}

Esgi.module.user.Panel = function (cfg) {
	console.log("index config",cfg);
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
	}
}

});

