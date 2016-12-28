package com.example;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class greetingController {
	
	ChatHistory chatHistory = new ChatHistory();

    @RequestMapping(value="/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
    @RequestMapping(value="/chatting", method={RequestMethod.GET})
    public String startChatting() {
    	return "chatting";
    }
    
    @RequestMapping(value="/chatting", method={RequestMethod.POST})
    public String onChatting(@RequestParam(value="message", required=false) String message, Model model) {
    	model.addAttribute("msg", message);
    	System.out.println(message);
    	return "chatting";
    }
    
}