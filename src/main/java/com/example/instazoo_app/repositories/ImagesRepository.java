package com.example.instazoo_app.repositories;

import com.example.instazoo_app.models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByUserId(Long userId);
    Optional<ImageModel> findByPostId(Long postId);
}
