package com.mySpring.demo.models.user.pojos;

import com.mySpring.demo.models.user.pojos.User;
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

    public UserHistory() {
    }

    public UserHistory(User user, Long newsId) {
        this.user = user;
        this.newsId = newsId;
    }

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