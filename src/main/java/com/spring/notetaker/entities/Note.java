package com.spring.notetaker.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String description;
    private String noteAddingTime;
    private String image;
    @ManyToOne
    @JsonIgnore
    private User user;

    public Note() {
    }

    public Note(int id, String title, String description, String noteAddingTime, String image, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.noteAddingTime = noteAddingTime;
        this.image = image;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoteAddingTime() {
        return noteAddingTime;
    }

    public void setNoteAddingTime(String noteAddingTime) {
        this.noteAddingTime = noteAddingTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", noteAddingTime=" + noteAddingTime +
                ", image='" + image + '\'' +
                '}';
    }
}
