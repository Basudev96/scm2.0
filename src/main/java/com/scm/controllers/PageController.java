package com.scm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {

    @RequestMapping("/home")    
    public String home(Model model){
        System.out.println("Home page handler");
        model.addAttribute("name", "Smart Contact Manager");
        model.addAttribute("firstName", "Basudev");
        model.addAttribute("github", "https://github.com/Basudev96");
        return "home";
    }

    //About route

    @RequestMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin", false);
        System.out.println("About page Loading....");
        return "about";
    }

    //Service route

    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("Services page Loading....");
        return "services";
    }
}
