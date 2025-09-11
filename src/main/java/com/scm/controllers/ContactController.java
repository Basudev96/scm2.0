package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserServices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserServices userServices;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/add")
    // add contact page: handler
    public String addContactView(Model model) {

        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
            Authentication authentication, HttpSession session) {

        // process the form data

        // 1 validation form

        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Please correct the errors in the form")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userServices.getUserByEmail(username);
        logger.info("file Information: {}", contactForm.getContactImage().getOriginalFilename());

        String filename = UUID.randomUUID().toString();

        String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
        // form - convert to entity
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(filename);
        contact.setUser(user);

        contactService.save(contact);

        // process the form data
        System.out.println(contactForm);

        session.setAttribute("message",
                Message.builder()
                        .content("Contact added successfully!")
                        .type(MessageType.green)
                        .build());

        return "redirect:/user/contacts/add";

    }

    // View contacts

    @RequestMapping
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Authentication authentication,
            Model model) {

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userServices.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "user/contacts";
    }

    // search handler
    @RequestMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {
            

            logger.info("Field: {}, Keyword: {}", contactSearchForm.getField(), contactSearchForm.getValue());

            var user = userServices.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

            Page<Contact> pageContact = null;

            if (contactSearchForm.getField().equalsIgnoreCase("name")) {
                pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
                pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            } else if (contactSearchForm.getField().equalsIgnoreCase("phoneNumber")) {
                pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            }

            logger.info("Page Contacts: {}", pageContact);
            model.addAttribute("contactSearchForm", contactSearchForm);
            model.addAttribute("pageContact", pageContact);
            model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

            return "user/search";
    }

}
