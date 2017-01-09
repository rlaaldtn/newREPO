package com.example;

import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.WebSocketContainer;

import org.springframework.boot.autoconfigure.web.ServerProperties.Session;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

@ClientEndpoint
public class ChatClientEndpoint {
	private Session usrSession = null;
	private MessageHandler messagehandler;
	
	public ChatClientEndpoint(final URI endpointURI){
		try{
			WebSocketContainer container = new ContainerProvider.getWebSocketContainer();
			container.connectToServer(this,endpointURI);
		}
		catch(Exception e){
			throw new RuntimeException(e);			
		}
	}
	@OnOpen
	public void onOpen(final Session usrSession){
		this.usrSession=usrSession;
	}
	@OnClose
	public void onClose(final Session usrSession, final CloseReason closeReason){
		this.usrSession = usrSession;
	}
	@OnMessage
	public void onMessage(final String message){
		if(messagehandler != null)
			messagehandler.handleMessage(message);
	}
}
