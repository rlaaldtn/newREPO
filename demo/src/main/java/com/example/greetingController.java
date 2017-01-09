package com.example;

<<<<<<< HEAD
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
=======
import java.util.Date;
import java.util.LinkedList;
>>>>>>> 9c25ce8bdd12f369d3a1507b0e9904418fd0bf3f
import java.util.Queue;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
=======
>>>>>>> 9c25ce8bdd12f369d3a1507b0e9904418fd0bf3f

@Controller
public class greetingController extends TextWebSocketHandler {
	
	protected final Logger logger = LoggerFactory.getLogger(greetingController.class);
	Queue<String> q= new LinkedList<String>();
	@Autowired
	private CustomerRepository repository;

	Queue<String> queue = new LinkedList<String>();
	
    @RequestMapping(value="/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }
    
    @RequestMapping(value="/chatting", method={RequestMethod.GET})
    public String startChatting() {
    	UUID uuid = UUID.randomUUID();
    	String guid = uuid.toString();
    	logger.info(guid);
    	Customer newCustomer = new Customer(guid, new Date());
    	
    	repository.save(newCustomer);
    	queue.add(newCustomer.getId());
    	
    	System.out.println(queue.size());
    	
    	if(queue.size() > 1) {
    		
    		String tmp1 = queue.poll();
    		String tmp2 = queue.poll();
    		
    		Customer waitingCustomer1 = repository.findByCustomerId(tmp1);
    		waitingCustomer1.setMatchingId(tmp2);
    		repository.save(waitingCustomer1);
    		
    		Customer waitingCustomer2 = repository.findByCustomerId(tmp2);
    		waitingCustomer2.setMatchingId(tmp1);
    		repository.save(waitingCustomer2);
    		
    	}
    	
    	else {
    		//pending
    		
    	}
    	
    	
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
<<<<<<< HEAD
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
=======
    }    
>>>>>>> 9c25ce8bdd12f369d3a1507b0e9904418fd0bf3f
}