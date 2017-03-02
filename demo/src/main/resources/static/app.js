var stompClient = null;
var guid = "";
var sendingMsg = {};
var matchingid = "empty";
var prev_sender = "";
var sendDisabled = true;

function bindInput() {
  $( "#input" ).bind("input propertychange", function() {
    var text = $( "#input" ).val();
    if( !text.replace(/\s/g, '').length ) {
      $( "#send" ).prop("disabled", true);
      sendDisabled = true;
    }
    else {
      $( "#send" ).prop("disabled", false);
      sendDisabled = false;
    }
  });
}

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
    $("#scroll-option").append("<div class='alert alert-warning'><strong>대화 상대 찾는 중...</strong></div>");
    if(matchingid == "empty") {
      var intervalFunction = setInterval(function() {
        $.post("/getmatching", guid, function(data){matchingid = data});
        if(matchingid != "empty") {
          clearInterval(intervalFunction);
          $(".alert-warning").remove();
          $("#scroll-option").append("<div class='alert alert-success'><strong>연결되었습니다.</strong></div>");
          bindInput();
          window.setTimeout(function() {
            $(".alert-success").fadeTo(500, 0).slideUp(500, function() {
                $(this).remove();
            });
          }, 5000);
        }
      }, 2000);
    }
    var socket = new SockJS("/gs-guide-websocket");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log(frame);
        setConnected(true);
        stompClient.subscribe("/chatting/001", function (message) {
            showMessage(message.body);
        });
    });
}

function resetInput() {
  $( "#input" ).val("");
  $( "#input" ).focus();
  $( "#send" ).prop("disabled", true);
  sendDisabled = true;
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
}

function sendMessage(msg) {
    sendingMsg.content = msg;
    sendingMsg.sender = guid;
    sendingMsg.receiver = matchingid;
    stompClient.send("/app/transfer", {}, JSON.stringify(sendingMsg));
    resetInput();
}

function showMessage(message) {
      jsonmessage = JSON.parse(message);
      jsonmessage.content = jsonmessage.content.replace(/\n/g, '<br>');
      console.log("SENDING MSG CONTENT ::: "+jsonmessage.content);
      if(jsonmessage.content != " ") {
        if(jsonmessage.sender == prev_sender) {
          if(jsonmessage.sender == matchingid.slice(0,-1)) {
            $("#conversation").append("<div class='container'><div class='talk-bubble-left round'><div class='talktext'>" + jsonmessage.content + "</div></div></div>");
            $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);
          }
          else if(jsonmessage.sender == guid) {
            $("#conversation").append("<div class='container'><div class='talk-bubble-right round'><div class='talktext'>" + jsonmessage.content + "</div></div></div>");
            $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);
          }
        }
        else {
          if(jsonmessage.sender == matchingid.slice(0,-1)) {
            $("#conversation").append("<div class='container'><div class='talk-bubble-left tri-right left-top round'><div class='talktext'>" + jsonmessage.content + "</div></div></div>");
            $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);
          }
          else if(jsonmessage.sender == guid) {
            $("#conversation").append("<div class='container'><div class='talk-bubble-right tri-right right-top round'><div class='talktext'>" + jsonmessage.content + "</div></div></div>");
            $('#scroll-option').scrollTop($('#scroll-option')[0].scrollHeight);
          }
        }
      }
      else {
        $("#scroll-option").append("<div class='alert alert-info'><strong>상대가 떠났습니다.</strong></div>");
        $("#input").unbind();
        $("#input").prop("disabled", true);
        sendDisabled = true;
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
    $( "#send" ).click(function() { sendMessage( $( "#input" ).val() ); });
    $( "#input" ).keydown(function(e) {
      if(e.keyCode == 13 && !e.shiftKey) {
        e.preventDefault();
        if(!sendDisabled) sendMessage( $( "#input" ).val() );
      }
    });

});

$(document).ready(function() {
  guid = generateUUID();
  connect();
  $( "#input" ).focus();
  $( "#send" ).prop("disabled", true);
});

$(window).on("beforeunload", function() {
  sendMessage(" ");
  disconnect();
});
