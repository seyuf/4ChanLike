


var global2 = this,
loadMyLib2 = function(onloaded){

	global2.shan = {};
	global2.shan.html = {};

	global2.shan.html.panel = function(cfg){
		var me = this;
		me.cfg = cfg;
		me.render();
		me.initInputs(me);
	}

	global2.shan.html.panel.prototype = {
			initInputs : function(curForm){
				var me = this;
				me._inputs = {};
				$.each(this.cfg.inputs, function(idx, item) {
					item.renderTo = me.el;
					me._inputs[item.name] = new shan.html.inputs[item.type](item);  
					

				});
			},
			addInput : function(input) {


			},
			render : function(){
			    //this.el = $("<div class='row'></div>");
			    //this.el = $("<div class='panel-group' id='MainPanel'>");
			    this.el = $("<div class='panel panel-default'></div");
			    //this.el = (this.el).append(parentPanel);
				$(this.cfg.renderTo).append(this.el);

			},
			onButtonClick : function(e) {

				var me = this, data = {};


				return false;
			}

	};


	var commons = {
			init : function(){

				var me = this;
				$(me.cfg.renderTo).append(me.el1);
				$(me.cfg.renderTo).append(me.el2);
				

			},
			getValue : function(){
				return $(this.el).find(":input").val();
			}  
	};

//	Inputs in panel

	shan.html.inputs = {};
	shan.html.inputs.show = function(cfg){
		
		var me = this;
		
		me.cfg = cfg;
		var postCom = function() {
			new Esgi.module.user.Comment({ id : '#container-comment2'}, cfg.result.subjectId);$('#myModal2').modal('show')
		};
		
		
		//me.el = $("<div class='panel panel-default'></div");
		var createNewSubject =$("<button class='btn btn-success btn-xs pull-right' data-toggle='modal' data-target='#myModal'>post a subject</button>");
		//var createNewComment =$("<button class='btn btn-danger btn-xs pull-right' data-toggle='modal'  onclick='global.subId = 1;' data-target='#myModal2'>comment</button>");
		var createNewComment =$("<button class='btn btn-danger btn-xs pull-right' onclick='$(function(){new Esgi.module.user.Comment({ id : \"#container-comment2\"},"+cfg.result.subjectId+");$(\"#myModal2\").modal(\"show\");})'>comment</button>");

		me.el1= $( "<div class='panel-heading'></div>").append( 
				$("<h4 class='panel-title'></h4")
				.append($("<a data-toggle='collapse' data-parent='#MainPanel' href='#subject"+cfg.result.subjectId+"'>"+cfg.result.subjectName+"</a>"))
				.append(createNewSubject).append(createNewComment)
				);
		//me.el = (me.el).append(head);
		var collapse = $( "<div id='subject"+cfg.result.subjectId+"' class='panel-collapse collapse in'></div>");
		var body = $("<div class='panel-body'></div>");
		var subdiv = $("<div class='row'></div>");
		var img = $("<img class='img img-responsive' src='"+cfg.imgPath+"' />")
		var div6_1 = $("<div class='col-md-6'></div>").append(img);
		var div6_2 = $("<div class='col-md-6'>"+cfg.comment+"</div>");
		//divContent.append(div6_1);
		//divContent.append(div6_2);
		 $(subdiv).append(div6_1);
		 $(subdiv).append(div6_2);
		 $(body).append(subdiv);
		 $(collapse).append(body);
		 me.el2 = collapse;
		//$(me.cfg.renderTo).append(me.el);
		this.init();
		
	};
	shan.html.inputs.show.prototype = commons;

};

$(loadMyLib2);
