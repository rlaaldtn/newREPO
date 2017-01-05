package com.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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

@Controller
public class greetingController {
	
	protected final Logger logger = LoggerFactory.getLogger(greetingController.class);
	
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
    	
    	return "chatting";
    }
    
    @ResponseBody
    @RequestMapping(value="/chatting", method= RequestMethod.POST)
    public List<Message> onChatting(@RequestBody final Message message) {
    	chatHistory.putMessageList(message);
    	System.out.println(message.getContent());
    	return chatHistory.getMessageList();
    }
    
}