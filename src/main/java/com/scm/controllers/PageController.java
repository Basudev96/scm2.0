package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class PageController {

    @Autowired
    private UserServices userServices;

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

    //Contact Page
    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    //login

    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    //Register

    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        //userForm.setName("Basudev");
        model.addAttribute("userForm", userForm);
        return new String("register");
    }
    
    //Processing register
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session) {
        System.out.println("Registering user...");
        //fetch data
        System.out.println(userForm);
        //validate data

        if(rBindingResult.hasErrors()){
            System.out.println("Error: "+rBindingResult.toString());
            return "register";
        }        
        //save data to database
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .phoneNumber(userForm.getPhoneNumber())
        // .about(userForm.getAbout())
        // .profilePic("https://avatars.githubusercontent.com/u/72853349?v=4")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setProfilePic("https://avatars.githubusercontent.com/u/72853349?v=4");

        User savedUser = userServices.saveUser(user );
        //message = "User registered successfully"
        System.out.println("User saved: "+savedUser.toString());

        Message message = Message.builder().content("User registered successfully").type(MessageType.green).build();

        session.setAttribute("message", message);
        //redirect to login page
        return "redirect:/register";
    }
    
}
