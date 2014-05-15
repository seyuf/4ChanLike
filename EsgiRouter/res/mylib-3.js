var onJqueryReady = function(){
     console.log("-->>" ,this);
}

onJqueryReady.call({ toto : 1});
onJqueryReady.call(this);
onJqueryReady();

$(onJqueryReady);

