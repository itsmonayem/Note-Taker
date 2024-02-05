package com.spring.notetaker.dao;

import com.spring.notetaker.entities.Note;
import com.spring.notetaker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    Page<Note> findNotesByUser(User user, Pageable pageable);

//    @Query("select note from  Note note where note.user.id=:id and note.title LIKE concat('%',:title,'%') and note.description like concat('%',:description,'%') ")
//    Page<Note> findNotesByUserForSearch(@Param("id") int id,@Param("title") String title,@Param("description") String description, Pageable pageable);
    @Query("select note from  Note note where note.title LIKE concat('%',:title,'%') and note.description like concat('%',:description,'%') and note.user.name LIKE concat('%',:username,'%')")
    Page<Note> findByTitleContainingAndDescriptionContainingAndUser(@Param("title") String title, @Param("description") String description, @Param("username") String username, Pageable pageable);
}
