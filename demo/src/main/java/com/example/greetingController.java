package com.example;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class greetingController {
	
	protected final Logger logger = LoggerFactory.getLogger(greetingController.class);
	
	@Autowired
	private CustomerRepository repository;

	Queue<String> queue = new LinkedList<String>();
	
    @RequestMapping(value="/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
    @MessageMapping("/transfer")
    @SendTo("/chatting/001")
    public String greeting(@RequestBody Message message) throws Exception {
    	logger.info(message.getMessage());
    	logger.info(message.getChannel());
        return message.getMessage();
    }
    
    @RequestMapping(value="/guid", method=RequestMethod.POST)
    public String getGuid(@RequestBody final String guid) {
    	return repository.findByCustomerId(guid).getId();
    }
    
    @RequestMapping(value="/", method={RequestMethod.GET})
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
    	
    	return "index";
    } 
}