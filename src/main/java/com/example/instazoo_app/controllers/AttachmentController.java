package com.example.instazoo_app.controllers;

import com.example.instazoo_app.models.Attachment;
import com.example.instazoo_app.payload.response.MessageResponse;
import com.example.instazoo_app.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class AttachmentController {
    private final AttachmentService attachmentService;
    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file")MultipartFile file,
                                                             Principal principal) throws IOException{
        attachmentService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }
    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") Long postId,
                                                             @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException{
        attachmentService.uploadImageToPost(file, principal, postId);
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }
    @GetMapping("/profileImage")
    public ResponseEntity<Attachment> getImageForUser(Principal principal){
        Attachment userImage = attachmentService.getImageToUser(principal);
        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }
    @GetMapping("/{postId}/image")
    public ResponseEntity<Attachment> getImageToPost(@PathVariable("postId") Long postId){
        Attachment postImage = attachmentService.getImageToPost(postId);
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }



}
