package com.example.instazoo_app;

import com.example.instazoo_app.exceptions.NotFoundException;
import com.example.instazoo_app.facade.PostFacade;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.ImageRepository;
import com.example.instazoo_app.repositories.PostRepository;
import com.example.instazoo_app.repositories.UserRepository;
import com.example.instazoo_app.services.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class PostServiceTest {
    private static PostService postService;
    private static PostRepository postRepository;
    private static UserRepository userRepository;
    private static ImageRepository imageRepository;
    private static PostFacade postFacade;
    private static final String EMAIL = "jony@yandex.com";

    @BeforeAll
    static void setup(){
        postRepository = Mockito.mock(PostRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        imageRepository = Mockito.mock(ImageRepository.class);
        postFacade = Mockito.mock(PostFacade.class);

        postService = new PostService(postRepository, userRepository, imageRepository, postFacade);
    }

    @Test
    void getPostByIdTest(){
        // Given
        Principal principal = Mockito.mock(Principal.class);

        User user = Mockito.mock(User.class);
        Mockito.when(userRepository.findUserByUsername(null)).thenReturn(
                Optional.of(user));

        Optional<Post> post = Optional.of(new Post(2L, "title", "caption", "location"));
        Mockito.when(postRepository.findPostByIdAndUser(any(), any())).thenReturn(post);

        //When
        Post postById = postService.getPostById(2L, principal);

        //Then
        Assertions.assertEquals("title", postById.getTitle());
        Assertions.assertEquals("caption", postById.getCaption());
    }
    @Test
    void lackOfPostByUserIdShouldThrowException(){
        // Given
        Principal principal = Mockito.mock(Principal.class);

        User user = Mockito.mock(User.class);
        Mockito.when(user.getEmail()).thenReturn(EMAIL);

        Mockito.when(userRepository.findUserByUsername(any())).thenReturn(
                Optional.of(user));

        //When+Then
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            postService.getPostById(2L, principal);
        });
        Assertions.assertEquals("Post cannot be found for username: " + EMAIL
                , notFoundException.getMessage());
    }
}
