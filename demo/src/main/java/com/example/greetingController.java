package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//faweifjo
@Controller
public class greetingController {

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    //여긴 컨트롤러부분이다 하하하하하하하하하하
    //ㅁㅈ더래ㅓㅁㄷㅈ래머ㅑㅈ
    @RequestMapping("/textarea")
    public String tutorialTextarea(Model model){
    	model.addAttribute("Notes","please contact me \n this example by \n http://stackoverflow.com/questions/27839870/access-textarea-with-spring-thymeleaf \n");
    	return "textarea";
    }
}