package com.example.instazoo_app.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String caption;
    private String location;
    private String username;
    private Long likes;
    private Set<Long> likedUsers;
}
