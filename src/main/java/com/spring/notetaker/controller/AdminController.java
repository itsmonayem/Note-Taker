package com.spring.notetaker.controller;

import com.spring.notetaker.dao.NoteRepository;
import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    public AdminController(UserRepository userRepository, NoteRepository noteRepository) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    @ModelAttribute
    public void setLoggedInfo(Model model) {
        LoggedInfo.ROLE = "ROLE_ADMIN";
        LoggedInfo.STATUS = "loggedIn";
        model.addAttribute("loggedStatus",LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);
    }


    @GetMapping("/show-users")
    public String showUsers(Model model,@RequestParam("page") Optional<Integer> page,
                            @RequestParam("pageSize") Optional<Integer> pageSize) {



        Pageable pageable = PageRequest.of(page.orElse(1)-1, pageSize.orElse(5));
        Page<User> userList = this.userRepository.findAll("ROLE_USER", pageable);

        String queryRequest = "/admin/show-users?pageSize="+pageSize.orElse(5)+"&page=";
        Pagination paginationInfo  = CommonUtils.getPagination(new Pagination(),page.orElse(1),userList.getTotalPages(),queryRequest);

        SearchParam searchParam = new SearchParamBuilder().pageSize(pageSize.orElse(5).toString()).build();

        model.addAttribute("searchParam",searchParam);
        model.addAttribute("userList", userList);
        model.addAttribute("paginationInfo",paginationInfo);

        return "admin/show-users";
    }




    @GetMapping("/search-users")
    private String searchUsers(@RequestParam("username") Optional<String> username,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("pageSize") Optional<Integer> pageSize,
                               Model model) {

        Pageable pageable = PageRequest.of(page.orElse(1)-1,pageSize.orElse(5));
        Page<User> userList = this.userRepository.findUserByNameContaining(username.orElse(""),pageable);

        String queryRequest = "/admin/search-users?username="+username.orElse("")+"&pageSize="+pageSize.orElse(5)+"&page=";
        Pagination paginationInfo  = CommonUtils.getPagination(new Pagination(),page.orElse(1),userList.getTotalPages(),queryRequest);

        SearchParam searchParam = new SearchParamBuilder()
                .username(username.orElse(""))
                .pageSize(pageSize.orElse(5).toString())
                .build();

        model.addAttribute("searchParam",searchParam);
        model.addAttribute("userList", userList);
        model.addAttribute("paginationInfo",paginationInfo);

        return "admin/show-users";
    }



    @GetMapping("/delete-user/{id}/{currentPage}")
    public String deleteUser(@PathVariable("id") int id, @PathVariable("currentPage") Optional<Integer> currentPage, RedirectAttributes redirectAttributes) {
        this.userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message",new Message("User Delete Successful","alert-success"));
        return "redirect:/admin/show-users?page="+currentPage.orElse(0);
    }



    @GetMapping("/show-notes")
    public String showAllNotes(@RequestParam("page") Optional<Integer> page,
                               @RequestParam("pageSize") Optional<Integer> pageSize,
                               Model model) {

        Pageable pageable = PageRequest.of(page.orElse(1)-1, pageSize.orElse(5));
        Page<Note> notePage = this.noteRepository.findAll(pageable);

        String queryRequest = "/admin/show-notes?pageSize="+pageSize.orElse(5)+"&page=";
        Pagination paginationInfo  = CommonUtils.getPagination(new Pagination(),page.orElse(1),notePage.getTotalPages(),queryRequest);

        SearchParam searchParam = new SearchParamBuilder().pageSize(pageSize.orElse(5).toString()).build();

        model.addAttribute("paginationInfo",paginationInfo);
        model.addAttribute("searchParam",searchParam);
        model.addAttribute("noteList", notePage);
        return "admin/show-notes";
    }


    @GetMapping("/search-notes")
    public String searchNote(@RequestParam("page") Optional<Integer> page,
                             @RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("username") Optional<String> username,
                             @RequestParam("pageSize") Optional<Integer> pageSize,
                             Principal principal,
                             Model model) {

        User user = this.userRepository.getUserByUserName(principal.getName());
        Pageable pageable = PageRequest.of(page.orElse(1)-1, pageSize.orElse(5));
        Page<Note> notePage = this.noteRepository.findByTitleContainingAndDescriptionContainingAndUser(title, description, username.orElse(user.getName()), pageable);

        String queryRequest = "/admin/search-notes?title="+title+"&description="+description+"&username="+username.orElse(user.getName())+"&pageSize="+pageSize.orElse(5)+"&page=";
        Pagination paginationInfo  = CommonUtils.getPagination(new Pagination(),page.orElse(1),notePage.getTotalPages(),queryRequest);


        SearchParam searchParam = new SearchParamBuilder()
                .title(title).description(description)
                .username(username.orElse(user.getName()))
                .pageSize(pageSize.orElse(5).toString())
                .build();

        model.addAttribute("paginationInfo",paginationInfo);
        model.addAttribute("noteList",notePage);
        model.addAttribute("searchParam",searchParam);

        return "admin/show-notes";
    }



    @GetMapping("/delete-note/{id}/{currentPage}")
    public String deleteNote(@PathVariable("id") int id, @PathVariable("currentPage") Optional<Integer> currentPage, RedirectAttributes redirectAttributes) {
        this.noteRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message",new Message("Note Delete Successful","alert-success"));
        return "redirect:/admin/show-notes?page=" + currentPage.orElse(0);
    }
}
