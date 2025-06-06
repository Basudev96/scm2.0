package com.scm.helper;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {


        // if we logged in with email or password then how we can get email

        if (authentication instanceof OAuth2AuthenticationToken) {

            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

            var clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();

            String username = "";

            if (clientId.equalsIgnoreCase("google")) {
                // Sign in with Google
                System.out.println("Getting email from google");
                username=oauth2User.getAttribute("email").toString();

            }else if(clientId.equalsIgnoreCase("github")){
                // Sign in with Github
                System.out.println("Getting email from github");
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                    : oauth2User.getAttribute("login").toString() + "@gmail.com";
            }
            
            return username;
        }else{
            System.out.println("Getting data from local database");
            return authentication.getName();
        }

        
    }

}
