package com.spring.notetaker.controller;


import com.spring.notetaker.dao.NoteRepository;
import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/user")
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<List<Note>> search(@PathVariable("query") String query, Principal principal) {
        System.out.println("query");
        User user = this.userRepository.getUserByUserName(principal.getName());
        List<Note> list = this.noteRepository.findByTitleContainingOrDescriptionContainingAndUser(query,query, user);
        return ResponseEntity.ok(list);
    }
}
