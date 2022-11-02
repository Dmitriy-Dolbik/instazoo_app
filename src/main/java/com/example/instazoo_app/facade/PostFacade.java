package com.example.instazoo_app.facade;

import com.example.instazoo_app.dto.PostDTO;
import com.example.instazoo_app.models.Post;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {
    private final ModelMapper modelMapper;
    @Autowired
    public PostFacade(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public PostDTO convertToPostDTO(Post post){
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        postDTO.setUsername(post.getUser().getUsername());
        return postDTO;
    }

}
