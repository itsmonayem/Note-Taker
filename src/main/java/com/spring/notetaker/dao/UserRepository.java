package com.spring.notetaker.dao;

import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email=:email")
    User getUserByUserName(@Param("email") String email);
    @Query("select u from User u where u.role=:role")
    Page<User> findAll(String role,Pageable pageable);
}
