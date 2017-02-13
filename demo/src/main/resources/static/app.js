var stompClient = null;
var guid = "";
var sendingMsg = {};
var matchingid = "empty";

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var intervalFunction = setInterval(function() {
      console.log("searching...");
      $.post('/getmatching', guid, function(data){matchingid = data});
      console.log("GUID:" + guid);
      console.log("MATCH: " + matchingid);
      if(matchingid != 'empty') clearInterval(intervalFunction);
    }, 2000);
    // $.post('/getmatching', guid, function(data){matchingid = data});
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/chatting/001', function (message) {
            console.log(message.body);
            showGreeting(message.body);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    sendingMsg.content = $("#input").val();
    sendingMsg.channel = guid;
    console.log(sendingMsg);
    stompClient.send("/app/transfer", {}, JSON.stringify(sendingMsg));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
    //스크롤 자동으로 내리기 함수 ( 인터넷에서 복붙)
                $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);

}

function generateUUID() {
    var d = new Date().getTime();
    if(window.performance && typeof window.performance.now === "function"){
        d += performance.now(); //use high-precision timer if available
    }
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
}

$(function () {
    $( "#send" ).click(function() { sendMessage(); });
});

$(document).ready(function() {
  guid = generateUUID();
  console.log(guid);
  connect();
});

$(window).on("beforeunload", function() {
  disconnect();
});


