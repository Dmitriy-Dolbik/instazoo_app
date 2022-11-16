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
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "caption")
    private String caption;
    @Column(name = "location")
    private String location;

    @ElementCollection
    private Set<Long> likedUsers = new HashSet<>();
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "post", cascade=CascadeType.REFRESH, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate(){
        this.createdDate = LocalDateTime.now();
    }
}
