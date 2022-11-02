package com.example.instazoo_app.facade;

import com.example.instazoo_app.dto.UserDTO;
import com.example.instazoo_app.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    private final ModelMapper modelMapper;

    @Autowired
    public UserFacade(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}

