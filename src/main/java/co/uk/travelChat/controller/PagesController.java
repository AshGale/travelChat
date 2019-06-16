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

    @GetMapping("/commute")
    public String commute() {
        return "commute";
    }

    @GetMapping("/layover")
    public String layover() {
        return "layover";
    }

    @GetMapping("/train")
    public String train() {
        return "train";
    }

    @GetMapping("/bus")
    public String bus() {
        return "bus";
    }

    @GetMapping("/walk")
    public String walk() {
        return "walk";
    }

    @GetMapping("/explore")
    public String explore() {
        return "explore";
    }

    @GetMapping("/my/account")
    public String my_account() {
        return "my/account";
    }


}
