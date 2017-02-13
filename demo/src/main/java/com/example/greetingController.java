package com.example;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

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
    public Message greeting(@RequestBody Message message) throws Exception {
    	logger.info(message.getMessage());
        return message;
    }
    
    @RequestMapping(value="/guid", method=RequestMethod.POST)
    public String getGuid(@RequestBody final String guid) {
    	return repository.findByCustomerId(guid).getId();
    }
    
    @ResponseBody
    @RequestMapping(value="/getmatching", method=RequestMethod.POST)
    public String getMatching(@RequestBody final String searcher) {
    	
    	if(repository.exists(searcher)) {
    		String matchingId = repository.findByCustomerId(searcher).getMatchingId();
    		if(matchingId.equals("empty")) {
    			//TODO : matching stuff
    			if(queue.size() > 1) {
    				//someone found in waiting queue
    				String searcher_queue_first = queue.poll();
    				String searcher_queue_second = queue.poll();
    				
    				Customer customer_queue_first = repository.findByCustomerId(searcher_queue_first);
    				Customer customer_queue_second = repository.findByCustomerId(searcher_queue_second);
    				
    				customer_queue_first.setMatchingId(searcher_queue_second);
    				customer_queue_second.setMatchingId(searcher_queue_first);
    				
    				repository.save(customer_queue_first);
    				repository.save(customer_queue_second);
    				
    				Customer c = repository.findByCustomerId(searcher);
    				
    				return c.getMatchingId();
    			}
    			else {
    				//nobody found in waiting queue
    				return matchingId;
    			}
    		}
    		else {
    			return matchingId;
    		}
    	}
    	else {
    		Customer c = new Customer(searcher, new Date());
    		repository.save(c);
    		queue.add(searcher);
    		return "empty";
    	}
    	
    }
    
    @RequestMapping(value="/", method={RequestMethod.GET})
    public String startChatting() {
    	
    	return "index";
    } 
}