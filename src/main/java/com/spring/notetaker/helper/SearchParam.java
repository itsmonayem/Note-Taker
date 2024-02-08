package com.spring.notetaker.helper;

import java.sql.Time;

public class SearchParam {
    private String title;
    private String description;
    private String username;
    private Time time;
    private String searchFrom;
    private String pageSize;

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public SearchParam() {
    }

    public SearchParam(String title, String description, String username, Time time, String searchFrom, String pageSize) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.time = time;
        this.searchFrom = searchFrom;
        this.pageSize = pageSize;
    }

    public SearchParam(String title, String pageSize) {
        this.title = title;
        this.pageSize = pageSize;
    }


    public String getSearchFrom() {
        return searchFrom;
    }

    public void setSearchFrom(String searchFrom) {
        this.searchFrom = searchFrom;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
