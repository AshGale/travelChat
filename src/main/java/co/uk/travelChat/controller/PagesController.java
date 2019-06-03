package co.uk.travelChat.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    //this loads the index page from temples when / is called
    @GetMapping({"/","/home","/travelChat"})
    public String index() {
        return "index";
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
}
