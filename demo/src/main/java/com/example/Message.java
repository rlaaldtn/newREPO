package com.example;

public class Message {

	private String content;
	
	public Message(String message) {
		this.content = message;
	}
	
	public String getMessage() {
		return this.content;
	}
	
	public void setMessage(String message) {
		this.content = message;
	}
	
}
