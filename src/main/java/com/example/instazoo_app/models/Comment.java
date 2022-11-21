package com.example.instazoo_app.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private Long userId;
    @Column(columnDefinition = "text", nullable = false)
    private String message;

    public Comment(Long id, String username, Long userId, String message) {
        this.id = id;
        this.username = username;
        this.userId = userId;
        this.message = message;
    }

    @Column(updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    private Post post;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }
}
