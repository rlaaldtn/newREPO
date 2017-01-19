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
    @SendTo("/chatting/001/")
    public String greeting(@RequestBody Message message) throws Exception {
    	logger.info(message.getMessage());
    	logger.info(message.getChannel());
        return message.getMessage();
    }
    
    @RequestMapping(value="/guid", method=RequestMethod.POST)
    public String getGuid(@RequestBody final String guid) {
    	return repository.findByCustomerId(guid).getId();
    }
    
    @ResponseBody
    @RequestMapping(value="/getmatching", method=RequestMethod.POST)
    public String getMatching(@RequestBody final String searcher) {

    	if(!queue.contains(searcher)) {															//지금 들어온사람이 새롭게 들어온거면
    		Customer newCustomer = new Customer(searcher, new Date());							//고객 객체를 새로 만들고 
    		repository.save(newCustomer);														//몽고디비에 고객을 저장하고
    		queue.add(newCustomer.getId());														//서버 고객 대기 큐에도 넣음
    	
    	}    	
    	
    	System.out.println("큐 사이즈 : "+queue.size());											//큐 사이즈 출력
    	for(Customer customer : repository.findAll()) {									
    		System.out.println("디비에 저장된 고객 : " + customer.toString());						//디비에 저장된 고객들 출력
    	}
    	
    	String currentMatchingId = repository.findByCustomerId(searcher).getMatchingId();		//들어온 사람에게 매칭 상대된 상대의 id를 저장 (없으면 empty가 들어감)
    	
    	if(currentMatchingId.compareTo("empty")!=0) {											//들어온사람의 매칭 상대가 이미 있으면
    		System.out.println("status))   already matched!");									//상대 id 리턴 
    		return currentMatchingId;
    	}
    	
    	else {																					//들어온사람의 매칭 상대가 없고
    		System.out.println("status))   not matched...");										
	    	if(queue.size() > 1) {																//기다리는 사람이 있으면
	    		System.out.println("status))   someone's waiting... matching...");
	    		String tmp1 = queue.poll();														//서로 매칭해주고 들어온 사람의 상대 리턴
	    		String tmp2 = queue.poll();
	    		
	    		Customer waitingCustomer1 = repository.findByCustomerId(tmp1);
	    		waitingCustomer1.setMatchingId(tmp2);
	    		repository.save(waitingCustomer1);
	    		
	    		Customer waitingCustomer2 = repository.findByCustomerId(tmp2);
	    		waitingCustomer2.setMatchingId(tmp1);
	    		repository.save(waitingCustomer2);
	    		
	    		if (searcher.compareTo(tmp1) == 0) {
	    			return tmp2;
	    		}
	    		else {
	    			return tmp1;
	    		}
	    	}
	    	
	    	else {																				//기다리는 사람이 없으면
	    		System.out.println("status))   nobody's waiting...holdon...");
	    		return "empty";																	//없는 상태로 리턴
	    	}
    	}
    	
    }
    
    @RequestMapping(value="/", method={RequestMethod.GET})
    public String startChatting() {
    	
    	return "index";
    } 
}