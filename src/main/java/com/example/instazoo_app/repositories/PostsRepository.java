package com.example.instazoo_app.repositories;

import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
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

    @Query(value = "SELECT COUNT(p.post_id) FROM post_liked_users p WHERE p.post_id=:postId",
            nativeQuery = true)
    Long countLikes(@Param("postId") Long postId);
}


