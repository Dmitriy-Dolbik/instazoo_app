package com.example.instazoo_app.controllers;

import com.example.instazoo_app.dto.CommentDTO;
import com.example.instazoo_app.exceptions.InvalidRequestValuesException;
import com.example.instazoo_app.models.Comment;
import com.example.instazoo_app.payload.response.MessageResponse;
import com.example.instazoo_app.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.instazoo_app.util.ErrorUtil.createErrorMessageToClient;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentController(CommentService commentService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") Long postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        if (bindingResult.hasErrors()){
            String errorMsg = createErrorMessageToClient(bindingResult);
            throw new InvalidRequestValuesException(errorMsg);
        }

        Comment comment = commentService.saveComment(postId, commentDTO, principal);
        CommentDTO createdComment = modelMapper.map(comment, CommentDTO.class);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }
    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable("postId") Long postId){
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(postId)
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }
    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") Long commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.OK);
    }



}

