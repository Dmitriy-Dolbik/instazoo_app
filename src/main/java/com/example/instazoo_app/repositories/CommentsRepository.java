package com.example.instazoo_app.repositories;

import com.example.instazoo_app.models.Comment;
import com.example.instazoo_app.models.Post;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepository {
    List<Comment> findAllByPost(Post post);
    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
}
