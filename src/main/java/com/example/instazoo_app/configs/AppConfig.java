package com.example.instazoo_app.configs;

import com.example.instazoo_app.dto.PostDTO;
import com.example.instazoo_app.models.Post;
import com.example.instazoo_app.repositories.PostRepository;
import com.example.instazoo_app.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;


@Configuration
public class AppConfig {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppConfig(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Post.class, PostDTO.class)
                .setPostConverter(context -> {
                    context.getDestination().setUsername(context.getSource().getUser().getUsername());
                    context.getDestination().setLikes(postRepository.countLikes(context.getSource().getId()));
                    context.getDestination().setLikedUsers(context.getSource().getLikedUsers()
                            .stream()
                            .map(id -> userRepository.findById(id).get().getUsername())
                            .collect(Collectors.toSet()));
                    return context.getDestination();
                });

        return modelMapper;
    }
}
