package com.example.instazoo_app.services;

import com.example.instazoo_app.dto.CommentDTO;
import com.example.instazoo_app.exceptions.PostNotFoundException;
import com.example.instazoo_app.models.Comment;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.repositories.CommentsRepository;
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
public class CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);
    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;
    private final UsersRepository usersRepository;
    @Autowired
    public CommentService(CommentsRepository commentsRepository, PostsRepository postsRepository, UsersRepository usersRepository) {
        this.commentsRepository = commentsRepository;
        this.postsRepository = postsRepository;
        this.usersRepository = usersRepository;
    }
    //попробовать потом переписать с Mapper TODO
    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal){
        User user = getUserByPrincipal(principal);
        Post post = postsRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("Post cannot be found for username : "+user.getEmail()));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment for Post: {}", post.getId());
        return commentsRepository.save(comment);
    }
    public List<Comment> getAllCommentsForPost(Long postId){
        Post post = postsRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("Post cannot be found"));
        List<Comment> comments = commentsRepository.findAllByPost(post);
        return comments;
    }
    public void deleteComment(Long commentId){
        Optional<Comment> comment = commentsRepository.findById(commentId);
        comment.ifPresent(commentsRepository::delete);
    }
    private User getUserByPrincipal(Principal principal){
        String username = principal.getName();
        return usersRepository.findUserByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found with username : "+username));
    }

}
