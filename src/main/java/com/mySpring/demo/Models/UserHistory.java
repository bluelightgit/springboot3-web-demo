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
}