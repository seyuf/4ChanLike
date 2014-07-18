var global = this,
    loadMyLib = function(onloaded){

       global.Esgi = {};
       global.Esgi.html = {};
      


///
///  FORM
///

    /**
    **  @cfg.renderTo : Dom css identifier
    **  @cfg.url : url to submit form.
    **  @cfg.inputs : list of inputs cfg
    */
    global.Esgi.html.Form = function(cfg) {
      var me = this;
      
       me.cfg = cfg;
       me.render();
       me.initInputs(me);
    };
    
    
    global.Esgi.html.Form.prototype = {
    
      initInputs : function(curForm){
              var me = this;
              me._inputs = {};
              $.each(this.cfg.inputs, function(idx, item) {
                item.renderTo = me.el;
                me._inputs[item.name] = new Esgi.html.inputs[item.type](item, curForm);     
               
              });
      },
      addInput : function(input) {


      },
      render : function(){
    	  if(this.cfg.renderTo == '#container-comment2')
    		  this.el = $("<form class='form-horizontal' role='form' id='comForm'/>");
    	  else
    		  this.el = $("<form class='form-horizontal' role='form' />");

         $(this.cfg.renderTo).append(this.el);
        
      },
      onButtonClick : function(e) {
    	  
          var me = this, data = {};
          var file = {}; 
          
          if (me._inputs['comment'].getValue() == ''){
        	  alert('must fill the comment field');
      		console.log("no comment");
      		return false;
          }
          
          if(this.cfg.subjectId != undefined){
        	  data['commentId'] = this.cfg.subjectId;
          }
    
          $.each(me._inputs, function(key, item) {
            data[key] = item.getValue();
            if(key == 'image'){
            	file = item.el;
            }

          })
          
          var saveFile = function(){
        	  var t = new FormData();
        	  try{
        		  file  = $(file).find(':input')[0].files[0];
        		  data['image'] = data['email']+file.name;
        		  
        	  }catch (e) {
        		  console.log("Exception", e);
        		  find = {};
        	  }
        	 
        	  data['comment'] = data['comment']+"*"+data['pseudo']+"*";
        	  try{
        	  t.append('file',file,data['email']+file.name);
        	  }catch (e) {
        		  console.log("file upload Exception", e);
    			  sendData();

        	  }
        	  
        	 
        	 

        	  $.ajax({ 
        		  url : me.cfg. FileUrl,
        		  type: "POST",
        		  data:t,
        		  cache:false,
        		  processData:false,
        		  contentType:false,
        		  success: function( response ) { 
        			  sendData();
        			 
        		  },
        		  error:function(datas, statuts, error){
        			  console.log("error"+error);
        		  }
        	  });

          };
         
          
          var sendData = function(){
        	  data['category'] =  $('.active').attr('id');
        	  $.ajax({
        		  url : me.cfg.url,
        		  method : 'POST',
        		  data : data,

        		  success : function(response, status) {
        			  
        			  if(me.cfg.panel != null){
        				  response.result.id = me.cfg.panel;
        				  new Esgi.module.user.commentPanel(response.result);
        			  }else{
        				  response.result.id = "#MainPanel";
        				  new Esgi.module.user.Panel(response.result);
        			  }
        			 

        		  },
        		  error: function (data, status, erreur){

        			  console.log("error data",data.getResponseHeader("content-type"));
        			  console.log("status   ",status,"  cause", erreur);
        		  }

        	  });
          };
          
          saveFile(sendData);
          
          e.preventDefault();
          
          return false;
      },
      onButtonFind: function(e){
    	  var me = this;
    	  if (me._inputs['comment'].getValue() == ''){
        	  alert('must fill the subject name field');
      		return false;
          }
    	  var subject = me._inputs['comment'].getValue();
    	  document.location.href = "http://localhost:8080/EsgiRouter/admin/"+subject+"/";
    	  e.preventDefault();
    	  
      }

}

//
//   INPUTS
//
     var commons = {
          init : function(){
        	  
              var me = this;
              $(me.cfg.renderTo).append(me.el);
             
          },
          getValue : function(){
              return $(this.el).find(":input").val();
          }  
     };

    Esgi.html.inputs = {};
    Esgi.html.inputs.Text = function(cfg){
       var me = this;
       me.cfg = cfg;
       var input = $("<input type='text' name='"+cfg.name+"' class='form-control'/>");
       
       var col1 = $("<div class='col-sm-4'/>").append(input);
       var label = $("<label class='control-label col-sm-3'>"+cfg.label+"</label>");
       me.el = $("<div class='form-group form-group-sm'/>").append(label);
       me.el = (me.el).append(col1);
       this.init();

    };
    Esgi.html.inputs.Text.prototype = commons;

    Esgi.html.inputs.Password = function(cfg){
       var me = this;
      me.cfg = cfg;
       me.el = $("<input type='password'/>");
       this.init();

    };
    Esgi.html.inputs.Password.prototype = commons;

    
    
    Esgi.html.inputs.Select = function(cfg){
       var me = this;
        me.cfg = cfg;
       me.el = $("<select/>");
       me.init();
    };
    Esgi.html.inputs.Select.prototype = commons;
    
    // file input
    Esgi.html.inputs.File = function(cfg){
        var me = this;
        me.cfg = cfg;
        var input = $("<input name='"+cfg.name+"'type='file'/>");
        var col1 = $("<div class='col-sm-4'/>").append(input);
        var label = $("<label class='control-label col-sm-3'>File</label>");
        me.el = $("<div class='form-group form-group-sm'/>").append(label);
        me.el = (me.el).append(col1);
        this.init();
        
     };
     Esgi.html.inputs.File.prototype = commons;
     
     // textarea
     Esgi.html.inputs.TextArea = function(cfg){
         var me = this;
        me.cfg = cfg;
         var input = $("<textarea  name='"+cfg.name+"' class='form-control' rows='3'>");
         var col1 = $("<div class='col-sm-4'/>").append(input);
         var label = $("<label class='control-label col-sm-3'>"+cfg.label+"</label>");
         me.el = $("<div class='form-group form-group-sm'/>").append(label);
         me.el = (me.el).append(col1);
         this.init();

      };
      Esgi.html.inputs.TextArea.prototype = commons;
      
      
      // email inputs
      Esgi.html.inputs.Email = function(cfg){
          var me = this;
         me.cfg = cfg;         
         var input = $("<input  name='"+cfg.name+"' type='email' class='form-control'/>");
         var col1 = $("<div class='col-sm-4'/>").append(input);
         var label = $("<label class='control-label col-sm-3'>Email</label>");
         me.el = $("<div class='form-group form-group-sm'/>").append(label);
         me.el = (me.el).append(col1);
         
          this.init();
      };
      Esgi.html.inputs.Email.prototype = commons;
      
      //validate button
      Esgi.html.inputs.Button = function(cfg, parentCon){
          var me = this;
         me.cfg = cfg;         
         var inputS = $("<a class='btn btn-default'>"+cfg.label+" </a>");
         inputS.bind("click",function(){
        	 $('#myModal2').modal('hide');
        	 //$('#myModal2').modal('hide');  
        	 $('#myModal').modal('hide'); 
        	 if(cfg.subjectId){
        		 $(document).scrollTop( $("#subject"+cfg.subjectId).offset().top);
        	 }else{
        		 //$(document).scrollTop( $("#subject"+cfg.subjectId).offset().top);
        	 }
        	 });
         var col1 = $("<div class='col-sm-offset-1 col-sm-4'/>").append(inputS);
         me.el = $("<div class='form-group form-group-sm'/>").append(col1);
         inputS.on('click', function(e){parentCon.onButtonClick(e)});
         this.init(); 
      };
      Esgi.html.inputs.Button.prototype = commons;
      
      Esgi.html.inputs.find = function(cfg, parentCon){
          var me = this;
         me.cfg = cfg;         
         var inputS = $("<a class='btn btn-default'>"+cfg.label+" </a>");
         inputS.bind("click",function(){
        	 $('#myModal2').modal('hide');
        	 //$('#myModal2').modal('hide');  
        	 $('#myModal').modal('hide'); 
        	 if(cfg.subjectId){
        		 $(document).scrollTop( $("#subject"+cfg.subjectId).offset().top);
        	 }else{
        		 //$(document).scrollTop( $("#subject"+cfg.subjectId).offset().top);
        	 }
        	 });
         var col1 = $("<div class='col-sm-offset-1 col-sm-4'/>").append(inputS);
         me.el = $("<div class='form-group form-group-sm'/>").append(col1);
         inputS.on('click', function(e){
        	 
        	 parentCon.onButtonFind(e);
         });
         this.init(); 
      };
      Esgi.html.inputs.find.prototype = commons;

}

$(loadMyLib);




