


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
			    
				
				if(this.cfg.renderTo == "#MainPanel"){
					this.el = $("<div class='panel panel-default'></div");
				}else{
					this.el = $("<table class='table  table-striped'></table>");
				}
			   
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
				if(me.el1 != undefined){
					$(me.cfg.renderTo).append(me.el1);
					$(me.cfg.renderTo).append(me.el2);
				}else{
					$(me.cfg.renderTo).append(me.el);
				}
				

			},
			getValue : function(){
				return $(this.el).find(":input").val();
			}  
	};

//	Inputs in panel

	shan.html.inputs = {};
	shan.html.inputs.show = function(cfg){

		var me = this;
		var sliceCommment = cfg.comment.split("*", 2);
		me.cfg = cfg;
		var postCom = function() {
			new Esgi.module.user.Comment({ id : '#container-comment2'}, cfg.result.subjectId);$('#myModal2').modal('show')
		};


		//me.el = $("<div class='panel panel-default'></div");
		var createNewSubject =$("<button class='btn btn-success btn-xs pull-right' style='margin-left:10px;' data-toggle='modal' data-target='#myModal'>subject</button>");
		var createNewComment =$("<button class='btn btn-danger btn-xs pull-right' onclick='$(function(){new Esgi.module.user.Comment({ id : \"#container-comment2\"},{subId: "+cfg.result.subjectId+", panel:\"#subject"+cfg.result.subjectId+"\"});$(\"#myModal2\").modal(\"show\");})'>comment</button>");

		me.el1= $( "<div class='panel-heading'></div>").append( 
				$("<h4 class='panel-title'></h4")
				.append($("<a data-toggle='collapse' data-parent='#MainPanel' href='#subject"+cfg.result.subjectId+"'>"+cfg.result.subjectName+"</a>"))
				.append(createNewSubject).append(createNewComment)
		);

		var collapse = $( "<div id='subject"+cfg.result.subjectId+"' class='panel-collapse collapse in'></div>");
		var body = $("<div class='panel-body'></div>");
		var subdiv = $("<div class='row'></div>");
		var object = $("<object style='width:100%;' data='"+cfg.imgPath+"' type='image/png'></object>");
		var img = $("<img class='img img-responsive zoom'src='/EsgiRouter/res/img/base_img2.jpg' />");
		
		object.append(img)
		var div6_1 = $("<div class='col-md-6'></div>").append(object);
		div6_1.bind("click",function(){
			$("#container-pictures").append($("<img class='img img-responsive zoom' style='width:100%;' src='"+cfg.imgPath+"' />"));
			$("#myModal3").modal("show");console.log("toto");
			});
		var div6_2 = $("<div class='col-md-6'>"+sliceCommment[0]+"</div>");

		$(subdiv).append(div6_1);
		$(subdiv).append(div6_2);
		$(body).append(subdiv);
		$(collapse).append(body);
		me.el2 = collapse;
		this.init();

	};
	shan.html.inputs.show.prototype = commons;
	
	
	shan.html.inputs.comment = function(cfg){
		var sliceCommment = cfg.comment.split("*", 2);
		var me = this;
		me.cfg = cfg;
		
		var thead1 = $("<thead><tr><th style='width: 10%'>"+sliceCommment[1]+"</th><th style='width: 45%'></th><th style='width: 45%'></th></tr></thead>");
		var tbody1 = $("<tbody></tbody>");
		var tabTr = $("<tr></tr>").append($("<td></td>"));
		var object = $("<object style='width:100%;'  data='"+cfg.imgPath+"' type='image/png'></object>");
		var img = $("<img class='img img-responsive zoom'src='/EsgiRouter/res/img/base_img2.jpg' />");
		
		object.append(img);
		var colImg = $("<td></td>").append(object);
		colImg.bind("click",function(){
			$("#container-pictures").append($("<img  style='width:100%;' src='"+cfg.imgPath+"' />"));
			$("#myModal3").modal("show");console.log("toto");
			});
		var colComment = $("<td><div>"+sliceCommment[0]+"</div></td>");
		$(tabTr).append(colImg);
		$(tabTr).append(colComment);
		$(tbody1).append(tabTr);
		me.el1 = thead1;
		me.el2 = tbody1;
		this.init();

	};
	shan.html.inputs.comment.prototype = commons;
	
	
	
	
	
	shan.html.inputs.AdminCommentPanel = function(cfg){
		var sliceCommment = cfg.comment.split("*", 2);
		var me = this;
		me.cfg = cfg;
		
		var removeComment =$("<button class='btn btn-danger btn-md'>remove</button>");
		var tab = $("<table class='table  table-striped'></table>");
		var thead1 = $("<thead><tr><th style='width: 15%'>"+sliceCommment[1]+"</th><th style='width: 40%'></th><th style='width: 45%'></th></tr></thead>");
		var tbody1 = $("<tbody></tbody>");
		var tabTr = $("<tr></tr>").append($("<td></td>").append(removeComment));
		var object = $("<object data='"+cfg.imgPath+"' type='image/png'></object>");
		var img = $("<img class='img img-responsive zoom'src='/EsgiRouter/res/img/base_img2.jpg' />");
		tab.bind("click", function(){ tab.remove()});
		removeComment.bind("click", function(){
			var data = {};
			data['commentContent'] =  cfg.comment ;
       	  $.ajax({
       		  url : me.cfg.url,
       		  method : 'POST',
       		  data : data,

       		  success : function(response, status) {

       			 if(response.result){
       				 
       			 }
       			
       			tab.trigger("click");
       		  },
       		  error: function (data, status, erreur){

       			  console.log("error data",data.getResponseHeader("content-type"));
       			  console.log("status   ",status,"  cause", erreur);
       		  }
       		  
       	  });

		});
		object.append(img);
		var colImg = $("<td></td>").append(object);
		colImg.bind("click",function(){
			$("#container-pictures").append($("<img  style='width:100%;' src='"+cfg.imgPath+"' />"));
			$("#myModal3").modal("show");console.log("toto");
			});
		var colComment = $("<td><div>"+sliceCommment[0]+"</div></td>");
		$(tabTr).append(colImg);
		$(tabTr).append(colComment);
		$(tbody1).append(tabTr);
		
		 (tab).append(thead1);
		(tab).append(tbody1);
		me.el = tab;

		this.init();

	};
	shan.html.inputs.AdminCommentPanel.prototype = commons;
};

$(loadMyLib2);
