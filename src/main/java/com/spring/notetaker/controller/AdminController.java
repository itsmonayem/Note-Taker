package com.spring.notetaker.controller;

import com.spring.notetaker.dao.NoteRepository;
import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.LoggedInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    @ModelAttribute
    public void setLoggedInfo() {
        LoggedInfo.ROLE = "ROLE_ADMIN";
        LoggedInfo.STATUS = "loggedIn";
    }


    @GetMapping("/show-users")
    public String showUsers(Model model,@RequestParam("page") Optional<Integer> page) {
        model.addAttribute("loggedStatus",LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);
        System.out.println(LoggedInfo.ROLE);

        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        Page<User> userList = this.userRepository.findAll("ROLE_USER", pageable);

        model.addAttribute("userList", userList);
        model.addAttribute("currentPage", page.orElse(0));
        model.addAttribute("totalPages", userList.getTotalPages());

        return "admin/show-users";
    }



    @GetMapping("/delete-user/{id}/{currentPage}")
    public String deleteUser(@PathVariable("id") int id, @PathVariable("currentPage") Optional<Integer> currentPage) {
        this.userRepository.deleteById(id);
        return "redirect:/admin/show-users?page="+currentPage.orElse(0);
    }



    @GetMapping("/show-notes")
    public String showAllNotes(@RequestParam("page") Optional<Integer> page, Model model) {
        model.addAttribute("loggedStatus",LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);

        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        Page<Note> notePage = this.noteRepository.findAll(pageable);

        model.addAttribute("noteList", notePage);
        model.addAttribute("currentPage", page.orElse(0));
        model.addAttribute("totalPages", notePage.getTotalPages());
        return "admin/show-notes";
    }



    @GetMapping("/delete-note/{id}")
    public String deleteNote(@PathVariable("id") int id) {
        this.noteRepository.deleteById(id);
        return "redirect:/admin/show-notes";
    }
}
