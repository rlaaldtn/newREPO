package com.example;

public class Message {

	public String content;
	public String sender;
	public String receiver;
	
	public Message() {}
	
	public Message(String message, String sender, String receiver) {
		this.content = message;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public String getMessage() {
		return this.content;
	}
	
	public void setMessage(String message) {
		this.content = message;
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
}
