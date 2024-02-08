package com.spring.notetaker.controller;

import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.LoggedInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    private final UserRepository userRepository;
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Value("${project.image}")
    private String path;


    @GetMapping("/")
    public String home(Model model,Principal principal) {
        if (principal == null) LoggedInfo.STATUS = "loggedOut";
        else {
            String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);

            LoggedInfo.ROLE = user.getRole();
            LoggedInfo.STATUS = "loggedIn";
            System.out.println(LoggedInfo.ROLE);
            model.addAttribute("loggedRole",LoggedInfo.ROLE);
        }

        model.addAttribute("title", "Home Page");
        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        return "home";
    }



    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("title", "About us");
        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        return "about";
    }
}
