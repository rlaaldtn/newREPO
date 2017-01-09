function connect(){
	sock=new WebSocket('ws://'+location.hostname+':8080/amway-frontendapp-war/websockethandler');
	sock.onopen=function(e){
		console.log('open');
	};
	sock.onmessage = function(e){
		console.log('message',e.data);
		showGreeting(e.data);
	};
	sock.onclose = function(e){
		console.log('close');
	};
	sock.onerror = function(e){
		console.log('error');
	};
	setConnected(true);
}

function sendMessage(){
	var message =$('#message').val();
	var usrname =$('#usrname').val();

	var sned= {
		message:message,
		usrname:usrname
	};
	sock.send(JSON.stringify(send));

}

function disconnect(){
	if(sock !=null){
		sock.close();
	}
	setConnected(false);
	console.log('Disconnected');
}