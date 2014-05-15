var onJqueryReady = function(){
  alert('2 - DOM Is load, Jquery IS READY');
  a(b);  
}

$(onJqueryReady);


var a = function(callback) {
            alert('3 - ASK A QUESTION');
            callback();
    },
    b = function() {
           alert('4 - HERE IS THE RESPONSE');
    };  

alert('1 - OK I\'M FIRST');

