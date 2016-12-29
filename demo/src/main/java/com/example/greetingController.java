package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @ResponseBody
    @RequestMapping(value="/chatting", method= RequestMethod.POST)
    public String onChatting(@RequestBody final Message message, Model model) {
    	model.addAttribute("msg", message.getContent());
    	
    	System.out.println(message.getContent());
    	return null;
    }
    
}