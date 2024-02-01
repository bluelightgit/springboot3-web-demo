package com.mySpring.demo.Models;

import jakarta.persistence.*;


@Entity
@Table(name = "user_history")
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long newsId;

    // getters and setters

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }
}