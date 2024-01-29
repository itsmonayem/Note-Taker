package com.spring.notetaker.controller;

import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.LoggedInfo;
import com.spring.notetaker.helper.Message;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Value("${project.image}")
    private String path;



    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("title", "Signup");
        model.addAttribute("user", new User());
        return "signup";
    }



    @PostMapping("/do-register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, RedirectAttributes redirectAttributes) {
        try {
            if (!agreement) {
                System.out.println("You have not agreed");
                throw new Exception("You have not agreed with terms and condition");
            }
            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("user", user);
                redirectAttributes.addFlashAttribute("message", new Message("Enter data correctly", "alert-danger"));
                return "redirect:/signup";
            }

            user.setRole("ROLE_USER");
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            this.userRepository.save(user);

//            Create folder for every user
            new File(path + "images/" + user.getId()).mkdir();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("message", new Message("Something went wrong!!" + e.getMessage(), "alert-danger"));
            return "redirect:/signup";
        }
        redirectAttributes.addFlashAttribute("message", new Message("Account created successfully", "alert-success"));
        return "redirect:/login";
    }





//    Custom Login
    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        model.addAttribute("title", "Login Page");
        if (principal == null) LoggedInfo.STATUS = "loggedOut";
        return "login";
    }
}
