package com.spring.notetaker.controller;

import com.spring.notetaker.dao.NoteRepository;
import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.LoggedInfo;
import com.spring.notetaker.helper.Message;
import com.spring.notetaker.helper.Pagination;
import com.spring.notetaker.helper.SearchParam;
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
    public String showUsers(Model model,@RequestParam("page") Optional<Integer> page,
                            @RequestParam("pageSize") Optional<Integer> pageSize) {
        model.addAttribute("loggedStatus",LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);
        System.out.println(LoggedInfo.ROLE);



        Pageable pageable = PageRequest.of(page.orElse(1)-1, pageSize.orElse(5));
        Page<User> userList = this.userRepository.findAll("ROLE_USER", pageable);

        model.addAttribute("userList", userList);

        Pagination paginationInfo = new Pagination();
        paginationInfo.setCurrentPage(page.orElse(1));
        paginationInfo.setTotalPages(userList.getTotalPages());
        paginationInfo.setQueries("/admin/show-users?pageSize="+pageSize.orElse(5)+"&page=");

        model.addAttribute("paginationInfo",paginationInfo);
        SearchParam searchParam = new SearchParam();
        searchParam.setPageSize(pageSize.orElse(5).toString());
        model.addAttribute("searchParam",searchParam);

        return "admin/show-users";
    }




    @GetMapping("/search-users")
    private String searchUsers(@RequestParam("username") Optional<String> username,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("pageSize") Optional<Integer> pageSize,
                               Model model) {
        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);


        Pageable pageable = PageRequest.of(page.orElse(1)-1,pageSize.orElse(5));
        Page<User> userList = this.userRepository.findUserByNameContaining(username.orElse(""),pageable);

        model.addAttribute("userList", userList);


        Pagination pagination = new Pagination();
        pagination.setCurrentPage(page.orElse(1));
        pagination.setTotalPages(userList.getTotalPages());
        pagination.setQueries("/admin/search-users?username="+username.orElse("")+"&pageSize="+pageSize.orElse(5)+"&page=");

        model.addAttribute("paginationInfo",pagination);
        SearchParam searchParam = new SearchParam();
        searchParam.setUsername(username.orElse(""));
        searchParam.setPageSize(pageSize.orElse(5).toString());
        model.addAttribute("searchParam",searchParam);
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
        model.addAttribute("loggedStatus",LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);


        Pagination paginationInfo = new Pagination();
        paginationInfo.setPageSize(pageSize.orElse(5));

        Pageable pageable = PageRequest.of(page.orElse(1)-1, paginationInfo.getPageSize());
        Page<Note> notePage = this.noteRepository.findAll(pageable);

        model.addAttribute("noteList", notePage);


        paginationInfo.setCurrentPage(page.orElse(1));
        paginationInfo.setTotalPages(notePage.getTotalPages());
        paginationInfo.setQueries("/admin/show-notes?pageSize="+pageSize.orElse(5)+"&page=");

        SearchParam searchParam = new SearchParam();
        searchParam.setPageSize(pageSize.orElse(5).toString());

        model.addAttribute("paginationInfo",paginationInfo);
        model.addAttribute("searchParam",searchParam);
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

        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
        model.addAttribute("loggedRole",LoggedInfo.ROLE);


        User user = this.userRepository.getUserByUserName(principal.getName());



        Pageable pageable = PageRequest.of(page.orElse(1)-1, pageSize.orElse(5));

        Page<Note> notePage = this.noteRepository.findByTitleContainingAndDescriptionContainingAndUser(title, description, username.orElse(user.getName()), pageable);

        //Pagination
        Pagination paginationInfo = new Pagination();
        paginationInfo.setCurrentPage(page.orElse(1));
        paginationInfo.setTotalPages(notePage.getTotalPages());
        paginationInfo.setQueries("/admin/search-notes?title="+title+"&description="+description+"&username="+username.orElse(user.getName())+"&pageSize="+pageSize.orElse(5)+"&page=");

        SearchParam searchParam = new SearchParam();
        searchParam.setTitle(title);
        searchParam.setDescription(description);
        searchParam.setUsername(username.orElse(user.getName()));
        searchParam.setPageSize(pageSize.orElse(5).toString());

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
