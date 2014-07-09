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
       me.initInputs();
       
       
       me.submit = $("<a class=\"btn btn-success\">send</a>");
       $(me.cfg.renderTo).append(me.submit);
       me.submit.on('click', function(e){me.onButtonClick(e)});
    };

    global.Esgi.html.Form.prototype = {
         initInputs : function(){
              var me = this;
              me._inputs = {};
              $.each(this.cfg.inputs, function(idx, item) {
                item.renderTo = me.el;
                me._inputs[item.name] = new Esgi.html.inputs[item.type](item);            
              });
      },
      addInput : function(input) {


      },
      render : function(){
        this.el = $("<form class='form-horizontal' role='form'/>");
         $(this.cfg.renderTo).append(this.el)
      },
      onButtonClick : function(e) {
          var me = this, data = {};
          //console.log(me._inputs);
          $.each(me._inputs, function(key, item) {
            //data[key] = item.getValue();
              data[key] = item.getValue($("[name="+key+"]").val());

            console.log($("[name="+key+"]").val());
            	
          });
          $.ajax({
            url : me.cfg.url,
            method : 'POST',
            data : data,
            success : function(response) {
              alert('NICE');
            }

          })
          e.preventDefault();
          return false;
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
              return $(this.el).val();
          }
     };

    Esgi.html.inputs = {};
    Esgi.html.inputs.Text = function(cfg){
       var me = this;
       me.cfg = cfg;
       var input = $("<input type='text' name='"+cfg.name+"' class='form-control'/>");
       var col1 = $("<div class='col-sm-4'/>").append(input);
       var label = $("<label class='control-label col-sm-1'>"+cfg.label+"</label>");
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
        var input = $("<input type='file'/>");
        var col1 = $("<div class='col-sm-4'/>").append(input);
        var label = $("<label class='control-label col-sm-1'>File</label>");
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
         var label = $("<label class='control-label col-sm-1'>"+cfg.label+"</label>");
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
         var label = $("<label class='control-label col-sm-1'>Email</label>");
         me.el = $("<div class='form-group form-group-sm'/>").append(label);
         me.el = (me.el).append(col1);
         
          this.init();
      };
      Esgi.html.inputs.Email.prototype = commons;
      
      //validate button
      Esgi.html.inputs.Button = function(cfg){
          var me = this;
         me.cfg = cfg;         
         var input = $("<button class='btn btn-default'>"+cfg.label+" </button>");
         var col1 = $("<div class='col-sm-offset-1 col-sm-4'/>").append(input);
         me.el = $("<div class='form-group form-group-sm'/>").append(col1);
         this.init();
         //$(me.cfg.renderTo).append(me.el);
         me.el.on('click', function(e){global.Esgi.html.Form.me.onButtonClick(e)});
          
      };
      Esgi.html.inputs.Button.prototype = commons;

    
    

}

$(loadMyLib);

$(document).ready(function(){
	$('.nav li').click(function(event){
		$('.nav li.active').removeClass('active');
		
		$(this).addClass('active');
		console.log('in my function');
		//event.preventDefault();
		
	});
});


