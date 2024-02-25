package com.mySpring.demo.models.news.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserUploadedNews implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String url;

    private String imageUrl;

    private String tag;

    private Long publishTime;

    private Long views;

    private Long userId;

    private String username;

    private int status; // 0: pending, 1: approved, 2: rejected
}
