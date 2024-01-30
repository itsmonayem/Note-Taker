package com.spring.notetaker.dao;

import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    Page<Note> findNotesByUser(User user, Pageable pageable);

    @Query("select note from  Note note where note.user.id=:id and note.title LIKE concat('%',:query,'%') or note.description like concat('%',:query,'%') ")
    Page<Note> findNotesByUserForSearch(@Param("id") int id,@Param("query") String query, Pageable pageable);
    Page<Note> findByTitleContainingOrDescriptionContaining(String query, String query2, Pageable pageable);
}
