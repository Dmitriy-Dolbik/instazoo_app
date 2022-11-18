package com.example.instazoo_app.repositories;

import com.example.instazoo_app.models.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findByUserId(Long userId);
    Optional<Attachment> findByPostId(Long postId);
}
