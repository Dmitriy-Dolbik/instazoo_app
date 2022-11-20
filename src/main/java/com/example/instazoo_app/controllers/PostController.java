package com.example.instazoo_app.controllers;

import com.example.instazoo_app.dto.PostDTO;
import com.example.instazoo_app.exceptions.InvalidRequestValuesException;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.payload.response.MessageResponse;
import com.example.instazoo_app.services.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.instazoo_app.util.ErrorUtil.createErrorMessageToClient;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
@RequiredArgsConstructor
public class PostController {
    private final ModelMapper modelMapper;
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        if (bindingResult.hasErrors()){
            String errorMsg = createErrorMessageToClient(bindingResult);
            throw new InvalidRequestValuesException(errorMsg);
        }

        Post post = postService.createPost(postDTO, principal);
        PostDTO createdPost = modelMapper.map(post, PostDTO.class);

        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsForUser(Principal principal) {
        List<PostDTO> postDTOList = postService.getAllPostForUser(principal)
                .stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostDTO> likePost(@PathVariable("postId") Long postId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postService.likePost(postId, user.getId());
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);

        return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") Long postId, Principal principal) {
        postService.deletePost(postId, principal);
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }
}
