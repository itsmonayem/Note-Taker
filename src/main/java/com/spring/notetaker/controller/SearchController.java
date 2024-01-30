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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/search-notes")
    public String searchNote(@RequestParam("query") String query, @RequestParam("page") Optional<Integer> page, Principal principal, Model model) {
        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);

        System.out.println("query");
        User user = this.userRepository.getUserByUserName(principal.getName());

        System.out.println(page);
        Pageable pageable = PageRequest.of(page.orElse(0),5);
        Page<Note> notePage;

        if(Objects.equals(user.getRole(), "ROLE_ADMIN")) {
            notePage = this.noteRepository.findByTitleContainingOrDescriptionContaining(query, query, pageable);
        } else {
            System.out.println(user.getName());
            notePage = this.noteRepository.findNotesByUserForSearch(user.getId(), query, pageable);
        }
        model.addAttribute("searchQuery",query);
        model.addAttribute("noteList", notePage);
        model.addAttribute("currentPage", page.orElse(0));
        model.addAttribute("totalPages", notePage.getTotalPages());
        model.addAttribute("hitFrom","search");
        model.addAttribute("query",query);
        return "search-note-result";
    }
}
