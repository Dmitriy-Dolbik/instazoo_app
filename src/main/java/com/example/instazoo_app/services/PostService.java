package com.example.instazoo_app.services;

import com.example.instazoo_app.dto.PostDTO;
import com.example.instazoo_app.exceptions.PostNotFoundException;
import com.example.instazoo_app.models.ImageModel;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.ImagesRepository;
import com.example.instazoo_app.repositories.PostsRepository;
import com.example.instazoo_app.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    private final ImagesRepository imagesRepository;

    @Autowired
    public PostService(PostsRepository postsRepository, UsersRepository usersRepository, ImagesRepository imagesRepository) {
        this.postsRepository = postsRepository;
        this.usersRepository = usersRepository;
        this.imagesRepository = imagesRepository;
    }
    //Переписать с помощью Mapper'a
    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);
        LOG.info("Saving Post for User: {}", user.getEmail());
        return postsRepository.save(post);
    }
    public List<Post> getAllPosts() {
        return postsRepository.findAllByOrderByCreatedDateDesc();
    }
    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postsRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post cannot be found for username: " + user.getEmail()));
    }
    public List<Post> getAllPostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postsRepository.findAllByUserOrderByCreatedDateDesc(user);
    }
    public Post likePost(Long postId, String username) {
        Post post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(u -> u.equals(username)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postsRepository.save(post);
    }
    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imagesRepository.findByPostId(post.getId());
        postsRepository.delete(post);
        imageModel.ifPresent((imagesRepository::delete));
    }
    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return usersRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
    }
}