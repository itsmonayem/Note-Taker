package com.spring.notetaker.controller;

import com.spring.notetaker.dao.NoteRepository;
import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.LoggedStatus;
import com.spring.notetaker.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @ModelAttribute
    public void setLoggedStatus(){
        LoggedStatus.STATUS = "loggedIn";
    }

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("loggedStatus",LoggedStatus.STATUS);
        return "normal/index";
    }

    @GetMapping("/add-note")
    public String formAddNote(Model model) {
        model.addAttribute("title","Add new note");
        model.addAttribute("loggedStatus",LoggedStatus.STATUS);

        model.addAttribute("note",new Note());

        return "normal/form-note";
    }


    @PostMapping("/do-add-note")
    public String doAddNote (@ModelAttribute Note note, Principal principal, Model model) {
        model.addAttribute("loggedStatus",LoggedStatus.STATUS);
        System.out.println(note);
        User user = this.userRepository.getUserByUserName(principal.getName());

        note.setNoteAddingTime(new Date());
        note.setUser(user);
        this.noteRepository.save(note);


        return "normal/form-note";
    }


    @GetMapping("/show-notes/{page}")
    public String showNotes(@PathVariable("page") Integer page, Model model, Principal principal) {
        model.addAttribute("loggedStatus",LoggedStatus.STATUS);
        User user = this.userRepository.getUserByUserName(principal.getName());

        Pageable pageable = PageRequest.of(page, 5);
        Page<Note> noteList = this.noteRepository.findNotesByUser(user, pageable);

        model.addAttribute("noteList",noteList);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",noteList.getTotalPages());

        System.out.println(noteList);
        return "normal/show-notes";
    }


    @GetMapping("/delete/{id}")
    public String deleteNotes(@PathVariable("id")int id, Model model, Principal principal){
        model.addAttribute("loggedStatus",LoggedStatus.STATUS);

        Optional<Note> optionalNote = this.noteRepository.findById(id);
        Note note = optionalNote.get();

        User user = this.userRepository.getUserByUserName(principal.getName());
        if(note.getUser() != user) {
            System.out.println("You have no access of this note");
            model.addAttribute("message",new Message("You have no access of this note","alert-danger"));
        } else {
            user.getNoteList().remove(note);
            this.userRepository.save(user);
        }

        return "redirect:/user/show-notes/0";
    }



    @GetMapping("/update/{id}")
    public String updateNote(@PathVariable("id")int id, Model model, Principal principal) {
        model.addAttribute("loggedStatus","loggedIn");

        Optional<Note> optionalNote = this.noteRepository.findById(id);
        Note note = optionalNote.get();

        User user = this.userRepository.getUserByUserName(principal.getName());
        if(note.getUser() != user) {
            System.out.println("You have no access of this note");
            model.addAttribute("message",new Message("You have no access of this note","alert-danger"));
        } else {
            model.addAttribute("note",note);
        }
        return "normal/update-note";
    }


    @PostMapping("/do-update-note/{id}")
    public String doUpdateNote(@ModelAttribute Note note,@PathVariable("id")int id, Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());

        note.setId(id);
        note.setNoteAddingTime(this.noteRepository.findById(id).get().getNoteAddingTime());
        note.setUser(user);

        this.noteRepository.save(note);
        return "redirect:/user/show-notes/0";
    }


}
