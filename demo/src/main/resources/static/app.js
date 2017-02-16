var stompClient = null;
var guid = "";
var sendingMsg = {};
var matchingid = "empty";
var prev_sender = "";

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
    if(matchingid == "empty") {
      var intervalFunction = setInterval(function() {
        $.post("/getmatching", guid, function(data){matchingid = data});
        if(matchingid != "empty") {
          clearInterval(intervalFunction);
          $("#scroll-option").append("<div class='alert alert-success' id='alert_connected'><strong>연결되었습니다.</strong></div>");
        }
      }, 2000);
    }
    var socket = new SockJS("/gs-guide-websocket");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log("Connected: " + frame);
        stompClient.subscribe("/chatting/001", function (message) {
            console.log(message.body);
            showMessage(message.body);
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
    sendingMsg.sender = guid;
    sendingMsg.receiver = matchingid;
    console.log(sendingMsg);
    stompClient.send("/app/transfer", {}, JSON.stringify(sendingMsg));
    $("#input").val("");
}

function showMessage(message) {
      jsonmessage = JSON.parse(message);
      if(jsonmessage.sender == prev_sender) {
        if(jsonmessage.sender == matchingid.slice(0,-1)) {
          $("#conversation").append("<div class='container'><div class='talk-bubble-left round'><div class='talktextleft'><p>" + jsonmessage.content + "</p></div></div></div>");
          $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);
        }
        else if(jsonmessage.sender == guid) {
          $("#conversation").append("<div class='container'><div class='talk-bubble-right round'><div class='talktextright'><p>" + jsonmessage.content + "</p></div></div></div>");
          $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);
        }
      }
      else {
        if(jsonmessage.sender == matchingid.slice(0,-1)) {
          $("#conversation").append("<div class='container'><div class='talk-bubble-left tri-right left-top round'><div class='talktextleft'><p>" + jsonmessage.content + "</p></div></div></div>");
          $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);
        }
        else if(jsonmessage.sender == guid) {
          $("#conversation").append("<div class='container'><div class='talk-bubble-right tri-right right-top round'><div class='talktextright'><p>" + jsonmessage.content + "</p></div></div></div>");
          $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);
        }
      }
      prev_sender = jsonmessage.sender;
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
    $( "#input" ).keydown(function(e) {
      if(e.keyCode == 13) sendMessage();
    });
});

$(document).ready(function() {
  guid = generateUUID();
  console.log(guid);
  connect();
  $( "#input" ).focus();
});

$(window).on("beforeunload", function() {
  disconnect();
});
