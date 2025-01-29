package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserServices;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    private UserServices userServices;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }
        logger.info("Adding logged in user information to the model");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User Logged in : {} ", username);
        // fetch user data from database
        User user = userServices.getUserByEmail(username);
        logger.info("User Data : {}", user.toString());
        System.out.println("User Profile : ");
        logger.info("User Name : {}", user.getName());
        logger.info("User Email : {}", user.getEmail());
        model.addAttribute("loggedInUser", user);

    }

}
