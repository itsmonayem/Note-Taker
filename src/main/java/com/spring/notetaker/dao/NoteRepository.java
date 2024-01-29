package com.spring.notetaker.dao;

import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    Page<Note> findNotesByUser(User user, Pageable pageable);

//    @Query("select note from  Note note where ")
//    Page<Note> findByTitleContainingOrDescriptionContainingAndUser(String query, String query2, User user, Pageable pageable);
    Page<Note> findNotesByTitleContainingAndDescriptionContainingAndUser(String query, String query2, User user, Pageable pageable);
    Page<Note> findByTitleContainingOrDescriptionContaining(String query, String query2, Pageable pageable);
}
