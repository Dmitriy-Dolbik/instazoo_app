package com.example.instazoo_app.repositories;

import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {


    List<Post> findAllByUserOrderByCreatedDateDesc(User user);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndUser(Long id, User user);

    @Override
    @EntityGraph(attributePaths = {"likedUsers"})
    Optional<Post> findById(Long id);

    @Query("SELECT COUNT(p.id) FROM Post p WHERE p.id=:postId")
    Long determineLikesCount(@Param("postId") Long postId);
}


