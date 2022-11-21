package com.example.instazoo_app;

import com.example.instazoo_app.dto.CommentDTO;
import com.example.instazoo_app.exceptions.NotFoundException;
import com.example.instazoo_app.models.Comment;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.CommentRepository;
import com.example.instazoo_app.repositories.PostRepository;
import com.example.instazoo_app.repositories.UserRepository;
import com.example.instazoo_app.services.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class CommentServiceTest {
    private static final String EMAIL = "jack@gmail.com";
    private static CommentService commentService;
    private static CommentRepository commentRepository;
    private static PostRepository postRepository;
    private static UserRepository userRepository;
    private static ModelMapper modelMapper;

    @BeforeAll
    static void setup(){
        commentRepository = Mockito.mock(CommentRepository.class);
        postRepository = Mockito.mock(PostRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        modelMapper = Mockito.mock(ModelMapper.class);

        commentService = new CommentService(commentRepository, postRepository, userRepository, modelMapper);
    }
    @Test
    void saveCommentTest(){
        // Given
        Principal principal = Mockito.mock(Principal.class);
        CommentDTO commentDTO = Mockito.mock(CommentDTO.class);

        User user = Mockito.mock(User.class);
        Mockito.when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(user));
        Post post = Mockito.mock(Post.class);
        Mockito.when(postRepository.findById(any())).thenReturn(Optional.of(post));
        Mockito.when(user.getEmail()).thenReturn(EMAIL);

        Comment comment = new Comment(2L, "Username", 2L, "message");
        Mockito.when(modelMapper.map(commentDTO, Comment.class)).thenReturn(comment);

        Mockito.when(post.getId()).thenReturn(2L);

        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        // When
        Comment createdComment = commentService.saveComment(2L, commentDTO, principal);

        // Then
        Assertions.assertEquals("Username", createdComment.getUsername());
        Assertions.assertEquals("message", createdComment.getMessage());
    }
    @Test
    void getAllCommentsForPost(){

        // Given
        Post post = Mockito.mock(Post.class);
        Mockito.when(postRepository.findById(2L)).thenReturn(Optional.of(post));

        List<Comment> comments = new ArrayList(List.of(
                new Comment(2L, "Username1", 2L, "message1"),
                new Comment(3L, "Username2", 3L, "message2"),
                new Comment(4L, "Username3", 4L, "message3")
        ));
        Mockito.when(commentRepository.findAllByPost(post)).thenReturn(comments);

        // When
        List<Comment> commentsList = commentService.getAllCommentsForPost(2L);

        // Then
        Assertions.assertEquals("Username1", commentsList.get(0).getUsername());
    }
}
