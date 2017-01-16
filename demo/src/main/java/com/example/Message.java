package com.example;

public class Message {

	public String content;
	public String channel;
	
	public Message() {}
	
	public Message(String message, String channel) {
		this.content = message;
		this.channel = channel;
	}
	
	public String getMessage() {
		return this.content;
	}
	
	public void setMessage(String message) {
		this.content = message;
	}
	
	public String getChannel() {
		return this.channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
}
