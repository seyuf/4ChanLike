var global = this,
    loadMyLib = function(){


      global.Esgi = {};



       global.Esgi.html = {};


      global.Esgi.html.Form = function(){
          console.log("CREATE FORM INSTANCE");
          this.add = function(i) {
            this.value = (undefined != this.value && this.value + i) || i;
          }
      };

      global.Esgi.html.Form.prototype = {
            mul : function(i) {
                  alert('PROTO');
            }
      }

      var obj = new Esgi.html.Form(), 
          obj2 = new Esgi.html.Form();
    

      console.log("Object : ", obj);
      obj.value = -9;
      obj.add(3);
      console.log("Object : ", obj);
      obj.add(5);
      console.log("Object : ", obj);
      
      obj2.mul = function() {
          alert("IN OBJ INSTANCE");
            obj.mul.call(this);
//          this.__proto__.mul();
      }

      obj.mul();
      obj2.mul();


      console.log(obj, obj2);

    }

$(loadMyLib);

