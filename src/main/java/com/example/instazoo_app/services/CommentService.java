package com.example.instazoo_app.services;

import com.example.instazoo_app.dto.CommentDTO;
import com.example.instazoo_app.exceptions.NotFoundException;
import com.example.instazoo_app.models.Comment;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.CommentRepository;
import com.example.instazoo_app.repositories.PostRepository;
import com.example.instazoo_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new NotFoundException("Post cannot be found for username : "+user.getEmail()));
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(post);
        log.info("Saving comment for Post: {}", post.getId());
        return commentRepository.save(comment);
    }
    public List<Comment> getAllCommentsForPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new NotFoundException("Post cannot be found"));
        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments;
    }
    public void deleteComment(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }
    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found with username : "+username));
    }

}
