package com.example.instazoo_app.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String caption;
    private String location;
    private Long likes;

    @ElementCollection
    private Set<Long> likedUsers = new HashSet<>();
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "post", cascade=CascadeType.REFRESH, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
