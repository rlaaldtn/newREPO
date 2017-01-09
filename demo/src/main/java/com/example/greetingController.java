package com.example;

import java.io.IOException;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class greetingController extends TextWebSocketHandler {
	
	protected final Logger logger = LoggerFactory.getLogger(greetingController.class);
	Queue<String> q= new LinkedList<String>();
	@Autowired
	private CustomerRepository repository;
	
	private 
	ChatHistory chatHistory = new ChatHistory();

    @RequestMapping(value="/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
    @RequestMapping(value="/chatting", method={RequestMethod.GET})
    public String startChatting() {
    	UUID uuid = UUID.randomUUID();
    	String guid = uuid.toString();
    	logger.info(guid);
    	
    	repository.save(new Customer(guid));
    	
    	for(Customer customer : repository.findAll()) {
    		System.out.println(customer.toString());
    	}
    	q.offer(guid);
    	if(q.size() > 1){
    		String tmp1 = q.poll();
    		String tmp2 = q.poll();
    		Customer waiting1= repository.findByCustomerId(tmp1);
    		waiting1.setMatchingId(tmp2);
    		repository.save(waiting1);
    		
    		Customer waiting2= repository.findByCustomerId(tmp2);
    		waiting2.setMatchingId(tmp1);
    		repository.save(waiting2);
    		
    	}
    	return "chatting";
    }
    
    @ResponseBody
    @RequestMapping(value="/chatting", method= RequestMethod.POST)

    public List<Message> onChatting(@RequestBody final Message message) {
    	chatHistory.putMessageList(message);
    	System.out.println(message.getContent());
    	return chatHistory.getMessageList();
    }
    
    
    @RequestMapping({"/socketRun"})
    public String greetings(Map<String, Object> model){
    	logger.info("show socketRun page");
    	return "socketRun";
    }
    
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage text) throws InterruptedException, IOException{
    	logger.info(session.getId());
    	Thread.sleep(2000);
    	
    	Principal usr = session.getPrincipal();
    	logger.info(usr.toString());
    	logger.info(session.getAttributes().toString());
    	
    	TextMessage msg = new TextMessage(text.getPayload());
    	session.sendMessage(msg);
    }
}