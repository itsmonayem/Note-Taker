package com.spring.notetaker.entities;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String body;
    private Date noteAddingTime;
    @ManyToOne
    private User user;

    public Note() {
    }

    public Note(int id, String title, String body, Date noteAddingTime, User user) {
        this.id = id;
        this.title = title;
        this.body = body;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
}
