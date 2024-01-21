package com.spring.notetaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("loggedIn","Yeah!!");
        return "normal/index";
    }
}
