package com.example.instazoo_app.facade;

import com.example.instazoo_app.dto.CommentDTO;
import com.example.instazoo_app.models.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {
    private final ModelMapper modelMapper;
    @Autowired
    public CommentFacade(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public CommentDTO convertToCommentDTO(Comment comment){
        return modelMapper.map(comment, CommentDTO.class);
    }

}
