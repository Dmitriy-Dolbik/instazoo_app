package com.example.instazoo_app.services;

import com.example.instazoo_app.dto.PostDTO;
import com.example.instazoo_app.exceptions.NotFoundException;
import com.example.instazoo_app.models.Attachment;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.ImageRepository;
import com.example.instazoo_app.repositories.PostRepository;
import com.example.instazoo_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new NotFoundException(
                        "Post cannot be found for username: " + user.getEmail()));
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(user);
        log.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public List<Post> getAllPostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post cannot be found"));

        Optional<Long> userLiked = post.getLikedUsers()
                .stream()
                .filter(id -> id.equals(userId)).findAny();

        if (userLiked.isPresent()) {
            post.getLikedUsers().remove(userId);
        } else {
            post.getLikedUsers().add(userId);
        }
        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<Attachment> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent((imageRepository::delete));
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username : " + username));
    }
}
