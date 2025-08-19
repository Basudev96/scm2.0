package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserServices userServices;

    @RequestMapping("/add")
    //add contact page: handler
    public String addContactView(Model model) {
        
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,Authentication authentication, HttpSession session) {

        //process the form data


        //1 validation form

        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder()
            .content("Please correct the errors in the form")
            .type(MessageType.red)
            .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userServices.getUserByEmail(username);

        //form - convert to entity
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setUser(user);


        contactService.save(contact);

        //process the form data
        System.out.println(contactForm);

        session.setAttribute("message", 
        Message.builder()
            .content("Contact added successfully!")
            .type(MessageType.green)
            .build());

        return "redirect:/user/contacts/add";

    }

    //edit contact page: handler

}
