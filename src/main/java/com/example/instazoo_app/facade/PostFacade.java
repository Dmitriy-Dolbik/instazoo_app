package com.example.instazoo_app.facade;

import com.example.instazoo_app.dto.PostDTO;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.repositories.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    @Autowired
    public PostFacade(ModelMapper modelMapper, PostRepository postRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
    }
    public PostDTO convertToPostDTO(Post post){
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setLikes(postRepository.countLikes(post.getId()));
        return postDTO;
    }
    public Post convertToPost(PostDTO postDTO){
        Post post = modelMapper.map(postDTO, Post.class);
        return post;
    }

}
