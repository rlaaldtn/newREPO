function transfer() {

  var message = {};
  message.content = $('#message').value;

  $.ajax({
    contentType: 'application/json;charset=UTF-8',
    type: 'POST',
    url: 'chatting',
    dataType: 'json',
    processData: false,
    data: JSON.stringify(message)
  });

});
