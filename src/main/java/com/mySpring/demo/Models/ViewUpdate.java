package com.mySpring.demo.Models;

public class ViewUpdate {
    private Long id;
    private Long views;

    public ViewUpdate(Long id, Long views) {
        this.id = id;
        this.views = views;
    }

    public Long getId() {
        return id;
    }

    public Long getViews() {
        return views;
    }
}