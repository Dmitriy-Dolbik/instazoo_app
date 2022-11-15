package com.example.instazoo_app.controllers;

import com.example.instazoo_app.dto.PostDTO;
import com.example.instazoo_app.facade.PostFacade;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.payload.response.MessageResponse;
import com.example.instazoo_app.services.PostService;
import com.example.instazoo_app.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
public class PostController {
    private final PostFacade postFacade;
    private final PostService postService;
    private final ResponseErrorValidation responseErrorValidation;
    @Autowired
    public PostController(PostFacade postFacade, PostService postService, ResponseErrorValidation responseErrorValidation) {
        this.postFacade = postFacade;
        this.postService = postService;
        this.responseErrorValidation = responseErrorValidation;
    }
    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO,
                                             BindingResult bindingResult,
                                             Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Post post = postService.createPost(postDTO, principal);
        PostDTO createdPost = postFacade.convertToPostDTO(post);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        List<PostDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::convertToPostDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }
    @GetMapping ("/user/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsForUser(Principal principal){
        List<PostDTO> postDTOList = postService.getAllPostForUser(principal)
                .stream()
                .map(postFacade::convertToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }
    @PostMapping("/{postId}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") Long postId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postService.likePost(postId, user.getId());
        PostDTO postDTO = postFacade.convertToPostDTO(post);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }
    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") Long postId, Principal principal){
        postService.deletePost(postId, principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
