package com.spring.notetaker.controller;


import com.spring.notetaker.dao.NoteRepository;
import com.spring.notetaker.dao.UserRepository;
import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import com.spring.notetaker.helper.LoggedInfo;
import com.spring.notetaker.helper.Pagination;
import com.spring.notetaker.helper.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Controller
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

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

        Pagination paginationInfo = new Pagination();
        paginationInfo.setPageSize(pageSize.orElse(5));

        System.out.println(page);
        Pageable pageable = PageRequest.of(page.orElse(0), paginationInfo.getPageSize());
        Page<Note> notePage;

        notePage = this.noteRepository.findByTitleContainingAndDescriptionContainingAndUser(title, description, username.orElse(user.getName()), pageable);

        //Pagination

        paginationInfo.setCurrentPage(page.orElse(0));
        paginationInfo.setTotalPages(notePage.getTotalPages());
        paginationInfo.setQueries("/search-notes?title="+title+"&description="+description+"&username="+username.orElse(user.getName())+"&pageSize="+pageSize.orElse(5)+"&page=");

        SearchParam searchParam = new SearchParam();
        searchParam.setTitle(title);
        searchParam.setDescription(description);
        searchParam.setUsername(username.orElse(user.getName()));

        model.addAttribute("paginationInfo",paginationInfo);
        model.addAttribute("noteList",notePage);
        model.addAttribute("searchParam",searchParam);


        return "search-note-result";
    }

//    @GetMapping("/search-users")
//    private String searchUsers(@RequestParam("username") Optional<String> username,
//                               @RequestParam("page") Optional<Integer> page, Model model) {
//        model.addAttribute("loggedStatus", LoggedInfo.STATUS);
//        model.addAttribute("loggedRole",LoggedInfo.ROLE);
//
//
//        Pageable pageable = PageRequest.of(page.orElse(0),5);
//        Page<User> userList = this.userRepository.findUserByNameContaining(username.orElse(""),pageable);
//
//        model.addAttribute("userList", userList);
//        model.addAttribute("username",username.orElse(""));
//
//        Pagination pagination = new Pagination();
//        pagination.setCurrentPage(page.orElse(0));
//        pagination.setTotalPages(userList.getTotalPages());
//        pagination.setQueries("/search-users?username="+username.orElse("")+"&page=");
//
//        model.addAttribute("paginationInfo",pagination);
//        return "admin/search-user-list";
//    }
}
