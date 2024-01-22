package com.spring.notetaker.controller;

import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.LoggedStatus;
import com.spring.notetaker.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;



    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title","Home Page");
        model.addAttribute("loggedStatus", LoggedStatus.STATUS);
        return "home";
    }

    @GetMapping("/signup")
    public String signUpForm (Model model) {
        model.addAttribute("title","Signup");
        model.addAttribute("user",new User());
        return "signup";
    }



    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("title","About us");
        model.addAttribute("loggedStatus",LoggedStatus.STATUS);
        return "about";
    }




    @PostMapping("/do-register")
    public String registerUser(@ModelAttribute User user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model) {
        try {
            if (!agreement) {
                System.out.println("You have not agreed");
                throw new Exception("You have not agreed with terms and condition");
            }

            user.setRole("ROLE_USER");
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            this.userRepository.save(user);

        } catch (Exception e) {
            model.addAttribute("user",user);
            model.addAttribute("message",new Message("Something went wrong!!"+e.getMessage(),"alert-danger"));
        }
        return "signup";
    }


    @GetMapping("/login")
    public String login(Model model,Principal principal) {
        model.addAttribute("title","Login Page");
        if (principal == null) LoggedStatus.STATUS = "loggedOut";
        return "login";
    }
}
