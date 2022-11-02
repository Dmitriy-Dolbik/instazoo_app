package com.example.instazoo_app.controllers;

import com.example.instazoo_app.dto.CommentDTO;
import com.example.instazoo_app.facade.CommentFacade;
import com.example.instazoo_app.models.Comment;
import com.example.instazoo_app.payload.response.MessageResponse;
import com.example.instazoo_app.services.CommentService;
import com.example.instazoo_app.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {
    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(CommentService commentService, CommentFacade commentFacade, ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") Long postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Comment comment = commentService.saveComment(postId, commentDTO, principal);
        CommentDTO createdComment = commentFacade.convertToCommentDTO(comment);

        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }
    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsToPost(@PathVariable("postId") Long postId){
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(postId)
                .stream()
                .map(commentFacade::convertToCommentDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }
    @PostMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") Long commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.OK);
    }



}
