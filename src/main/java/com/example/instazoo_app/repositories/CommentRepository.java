package com.example.instazoo_app.repositories;

import com.example.instazoo_app.models.Comment;
import com.example.instazoo_app.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
