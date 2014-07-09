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
        this.el = $("<form class='form-horizontal' role='form'/>");
         $(this.cfg.renderTo).append(this.el);
        
      },
      onButtonClick : function(e) {
    	  console.log(e);
          var me = this, data = {};
    
          $.each(me._inputs, function(key, item) {
            data[key] = item.getValue();
          })
          
          $.ajax({
            url : me.cfg.url,
            method : 'POST',
            data : data,
            success : function(response) {
              alert('NICE');
            }

          });
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
              return $(this.el).find(":input").val();
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
        var input = $("<input name='"+cfg.name+"'type='file'/>");
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
      Esgi.html.inputs.Button = function(cfg, parentCon){
          var me = this;
         me.cfg = cfg;         
         var inputS = $("<a class='btn btn-default'>"+cfg.label+" </a>");
         var col1 = $("<div class='col-sm-offset-1 col-sm-4'/>").append(inputS);
         me.el = $("<div class='form-group form-group-sm'/>").append(col1);
         inputS.on('click', function(e){parentCon.onButtonClick(e)});
         this.init(); 
      };
      Esgi.html.inputs.Button.prototype = commons;

}

$(loadMyLib);




