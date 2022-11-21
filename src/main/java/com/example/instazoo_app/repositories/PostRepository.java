package com.example.instazoo_app.repositories;

import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    List<Post> findAllByUserOrderByCreatedDateDesc(User user);

    List<Post> findAllByOrderByCreatedDateDesc();

    Optional<Post> findPostByIdAndUser(Long id, User user);

    @Override
    @EntityGraph(attributePaths = {"likedUsers"})
    Optional<Post> findById(Long id);

    @Query(value = "SELECT COUNT(p.post_id) FROM post_liked_users p WHERE p.post_id=:postId",
            nativeQuery = true)
    Long countLikes(@Param("postId") Long postId);
    @Query(value = "SELECT plu.liked_users FROM post_liked_users plu WHERE plu.post_id=:postId",
    nativeQuery = true)// находим список всех id user'ов, которые лайкнули пост
    List<Long> findAllIdLikedUsersNames(@Param("postId") Long postId);
    @Query("SELECT u.username FROM User u WHERE u.id IN (:likedUsersIdList)")//получаем список имен по id
    List<String> findLikedUsersByPostId(@Param("likedUsersIdList") List<Long> likedUsersIdList);
}


