package com.example.instazoo_app.controllers;

import com.example.instazoo_app.dto.UserDTO;
import com.example.instazoo_app.exceptions.InvalidRequestValuesException;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.example.instazoo_app.util.ErrorUtil.createErrorMessageToClient;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") Long userId){
        User user = userService.getUserById(userId);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        if (bindingResult.hasErrors()){
            String errorMsg = createErrorMessageToClient(bindingResult);
            throw new InvalidRequestValuesException(errorMsg);
        }
        User user = userService.updateUser(userDTO, principal);

        UserDTO userUpdated = modelMapper.map(user, UserDTO.class);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }
}
