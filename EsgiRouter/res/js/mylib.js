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
       me.submit = $("<button>Envoyer</button>");
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
        this.el = $("<form/>");
         $(this.cfg.renderTo).append(this.el)
      },
      onButtonClick : function(e) {
          var me = this, data = {};
          $.each(me._inputs, function(key, item) {
            data[key] = item.getValue();
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
     }

    Esgi.html.inputs = {};
    Esgi.html.inputs.Text = function(cfg){
       var me = this;
       me.cfg = cfg;
       me.el = $("<input/>");
       this.init();

    }

    Esgi.html.inputs.Text.prototype = commons;

    Esgi.html.inputs.Password = function(cfg){
       var me = this;
      me.cfg = cfg;
       me.el = $("<input type='password'/>");
       this.init();

    }
    Esgi.html.inputs.Password.prototype = commons;

    Esgi.html.inputs.Select = function(cfg){
       var me = this;
        me.cfg = cfg;
       me.el = $("<select/>");
       me.init();

    }
    Esgi.html.inputs.Select.prototype = commons;

}

$(loadMyLib);



