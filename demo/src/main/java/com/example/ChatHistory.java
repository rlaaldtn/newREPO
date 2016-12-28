package com.example;

import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
	
	private List<Message> messageList;
	private int lastCount;
	
	public ChatHistory() {
		this.messageList = new ArrayList<Message>();
		this.lastCount = 0;
	}

	public List<Message> getMessageList() {
		return messageList;
	}
	
	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}
	
	public int getLastCount() {
		return lastCount-1;
	}
	
	public void setLastCount() {
		this.lastCount += 1;
	}
	
	public void resetLastCount() {
		this.lastCount = 0;
	}
	
	public void putMessageList(Message m) {
		messageList.add(m);
		setLastCount();
	}
	
}