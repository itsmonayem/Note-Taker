package com.spring.notetaker.entities;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String description;
    private Date noteAddingTime;
    @ManyToOne
    private User user;

    public Note() {
    }

    public Note(int id, String title, String description, Date noteAddingTime, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.noteAddingTime = noteAddingTime;
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

    public Date getNoteAddingTime() {
        return noteAddingTime;
    }

    public void setNoteAddingTime(Date noteAddingTime) {
        this.noteAddingTime = noteAddingTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", noteAddingTime=" + noteAddingTime +
                '}';
    }
}
