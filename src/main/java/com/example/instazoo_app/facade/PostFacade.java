package com.example.instazoo_app.facade;

import com.example.instazoo_app.dto.PostDTO;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.repositories.PostsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {
    private final ModelMapper modelMapper;
    private final PostsRepository postsRepository;
    @Autowired
    public PostFacade(ModelMapper modelMapper, PostsRepository postsRepository) {
        this.modelMapper = modelMapper;
        this.postsRepository = postsRepository;
    }
    public PostDTO convertToPostDTO(Post post){
        PostDTO postDTO = modelMapper.map(post, PostDTO.class);
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setLikes(postsRepository.countLikes(post.getId()));
        return postDTO;
    }
    public Post convertToPost(PostDTO postDTO){
        Post post = modelMapper.map(postDTO, Post.class);
        return post;
    }

}
