package com.spring.notetaker.helper;

import java.sql.Time;

public class SearchParamBuilder {
    private String title;
    private String description;
    private String username;
    private Time time;
    private String searchFrom;
    private String pageSize;
    public SearchParamBuilder title(String title) {
        this.title = title;
        return this;
    }

    public SearchParamBuilder description(String description) {
        this.description = description;
        return this;
    }
    public SearchParamBuilder username(String username) {
        this.username = username;
        return this;
    }
    public SearchParamBuilder time(Time time) {
        this.time = time;
        return this;
    }
    public SearchParamBuilder searchFrom(String searchFrom) {
        this.searchFrom = searchFrom;
        return this;
    }
    public SearchParamBuilder pageSize(String pageSize) {
        this.pageSize = pageSize;
        return this;
    }



    public SearchParam build() {
        return new SearchParam(title,description,username,time,searchFrom, pageSize);
    }
}
