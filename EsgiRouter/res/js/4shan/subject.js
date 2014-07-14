
$(function(){
Esgi.module = Esgi.module || {}
Esgi.module.user = Esgi.module.user || {}

Esgi.module.user.Subject = function (cfg) {
        new Esgi.html.Form({
             url : APP_CONTEXT+'/user/connect',
             FileUrl : APP_CONTEXT+'/file/upload',
             renderTo : cfg.id,
             inputs : [
               {
            	   type : "Text",
            	   name : 'subject',
            	   label : 'subject' 
               }
               ,{
                  type : "Text",
                  name : 'pseudo',
                  label : 'Alias' 
                }
                ,{
                  type : "Email",
                  name : 'email',
                  emptyText : 'Saisir votre email' 
                }
                ,{
                	type : "File",
                	name : 'image'
                }
                ,{
                	type : "TextArea",
                	name : 'comment',
                	label: 'comment'
                }
                ,{
                	type : "Button",
                	name : 'submit',
                	label: 'Post'
                }
             ]
        });
}

});

