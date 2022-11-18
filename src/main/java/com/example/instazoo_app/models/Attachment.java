package com.example.instazoo_app.models;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Data
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String ServerFileName;
    @JsonIgnore
    private Long userId;
    @JsonIgnore
    private Long postId;
    @Transient
    private byte[] imageBytes;
}
