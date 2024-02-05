package com.spring.notetaker.controller;

import com.spring.notetaker.dao.NoteRepository;
import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.LoggedInfo;
import com.spring.notetaker.helper.Message;
import com.spring.notetaker.helper.Pagination;
import com.spring.notetaker.helper.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Controller
@RequestMapping("/user")
public class NoteController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    @Value("${project.image}")
    private String rootImagePath;

    @ModelAttribute
    public void setLoggedStatus() {
        LoggedInfo.STATUS = "loggedIn";
        LoggedInfo.ROLE = "ROLE_USER";
    }





    @GetMapping("/add-note")
    public String formAddNote(Model model) {
        model.addAttribute("title", "Add new note");
        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);

        model.addAttribute("note", new Note());

        return "normal/add-note";
    }


    @PostMapping("/do-add-note")
    public String doAddNote(@ModelAttribute Note note, Principal principal, Model model, @RequestParam("note-image") MultipartFile file, RedirectAttributes redirectAttributes) {
        System.out.println(note);
        User user = this.userRepository.getUserByUserName(principal.getName());


        String pattern = "dd MMMM yyyy : hh:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        note.setNoteAddingTime(date);
        note.setUser(user);


        try {
            if (file.isEmpty()) {
                System.out.println("file is empty");
            } else {
                String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
                note.setImage(fileName);
                Path path = Paths.get(rootImagePath + "images/" + user.getId() + "/" + fileName);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
        this.noteRepository.save(note);

        redirectAttributes.addFlashAttribute("message",new Message("Note added successfully","alert-success"));
        return "redirect:/user/add-note";
    }


    @GetMapping("/show-notes")
    public String showNotes(@RequestParam("page") Optional<Integer> page,
                            @RequestParam("pageSize") Optional<Integer> pageSize,
                            Model model,
                            Principal principal) {

        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);

        User user = this.userRepository.getUserByUserName(principal.getName());

        Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5));
        Page<Note> noteList = this.noteRepository.findNotesByUser(user, pageable);

        model.addAttribute("noteList", noteList);
        model.addAttribute("currentPage", page.orElse(0));
        model.addAttribute("totalPages", noteList.getTotalPages());

        Pagination pagination = new Pagination();
        pagination.setCurrentPage(page.orElse(0));
        pagination.setTotalPages(noteList.getTotalPages());
        pagination.setPageSize(pageSize.orElse(5));
        pagination.setQueries("/user/show-notes?page=");

        model.addAttribute("paginationInfo",pagination);
        SearchParam searchParam = new SearchParam();
        searchParam.setPageSize(pageSize.orElse(5).toString());
        model.addAttribute("searchParam",searchParam);

        System.out.println(noteList);
        return "normal/show-notes";
    }




    @GetMapping("/search-notes")
    public String searchNote(@RequestParam("page") Optional<Integer> page,
                             @RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("username") Optional<String> username,
                             @RequestParam("pageSize") Optional<Integer> pageSize,
                             Principal principal,
                             Model model) {

        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);

        System.out.println("query");
        User user = this.userRepository.getUserByUserName(principal.getName());



        Pageable pageable = PageRequest.of(page.orElse(0), pageSize.orElse(5));
        Page<Note> notePage;

        notePage = this.noteRepository.findByTitleContainingAndDescriptionContainingAndUser(title, description, username.orElse(user.getName()), pageable);

        //Pagination
        Pagination paginationInfo = new Pagination();
        paginationInfo.setCurrentPage(page.orElse(0));
        paginationInfo.setTotalPages(notePage.getTotalPages());
        paginationInfo.setQueries("/user/search-notes?title="+title+"&description="+description+"&username="+username.orElse(user.getName())+"&pageSize="+pageSize.orElse(5)+"&page=");

        SearchParam searchParam = new SearchParam();
        searchParam.setTitle(title);
        searchParam.setDescription(description);
        searchParam.setUsername(username.orElse(user.getName()));
        searchParam.setPageSize(pageSize.orElse(5).toString());

        model.addAttribute("paginationInfo",paginationInfo);
        model.addAttribute("noteList",notePage);
        model.addAttribute("searchParam",searchParam);


        return "normal/show-notes";
    }






    @GetMapping("/delete/{id}")
    public String deleteNotes(@PathVariable("id") int id, Model model, Principal principal) {

        Optional<Note> optionalNote = this.noteRepository.findById(id);
        Note note = optionalNote.get();

        User user = this.userRepository.getUserByUserName(principal.getName());
        if (note.getUser() != user) {
            System.out.println("You have no access of this note");
            model.addAttribute("message", new Message("You have no access of this note", "alert-danger"));
        } else {
            user.getNoteList().remove(note);
            this.userRepository.save(user);
        }

        return "redirect:/user/show-notes";
    }


    @GetMapping("/update/{id}")
    public String updateNote(@PathVariable("id") int id, Model model, Principal principal) {
        model.addAttribute("loggedStatus", "loggedIn");
        model.addAttribute("loggedRole",LoggedInfo.ROLE);

        Optional<Note> optionalNote = this.noteRepository.findById(id);
        Note note = optionalNote.get();

        User user = this.userRepository.getUserByUserName(principal.getName());
        if (note.getUser() != user) {
            System.out.println("You have no access of this note");
            model.addAttribute("message", new Message("You have no access of this note", "alert-danger"));
        } else {
            model.addAttribute("note", note);
        }
        return "normal/update-note";
    }


    @PostMapping("/do-update-note/{id}")
    public String doUpdateNote(@ModelAttribute Note note, @PathVariable("id") int id, Model model, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());

        note.setId(id);
        note.setNoteAddingTime(this.noteRepository.findById(id).get().getNoteAddingTime());
        note.setUser(user);

        this.noteRepository.save(note);
        return "redirect:/user/show-notes";
    }


    @GetMapping("/view-note/{id}")
    public String viewNote(@PathVariable("id") int id, Model model, Principal principal) {
        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);

        Optional<Note> optionalNote = this.noteRepository.findById(id);
        Note note = optionalNote.get();

        User user = this.userRepository.getUserByUserName(principal.getName());
        if (note.getUser() != user) {
            System.out.println("You have no access of this note");
            model.addAttribute("message", new Message("You have no access of this note", "alert-danger"));
            return "redirect:/user/show-notes/0";
        } else {
            model.addAttribute("note", note);
            return "normal/view-note";
        }

    }
}
