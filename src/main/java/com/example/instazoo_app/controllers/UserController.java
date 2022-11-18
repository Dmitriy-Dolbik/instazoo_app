package com.example.instazoo_app.controllers;

import com.example.instazoo_app.dto.UserDTO;
import com.example.instazoo_app.exceptions.InvalidRequestValuesException;
import com.example.instazoo_app.facade.UserFacade;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.exceptions.ErrorResponse;
import com.example.instazoo_app.services.UserService;
import com.example.instazoo_app.validations.ResponseErrorValidation;
import com.example.instazoo_app.validations.UserValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;

import static com.example.instazoo_app.util.ErrorUtil.createErrorMessageToClient;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final UserFacade userFacade;
    private final ResponseErrorValidation responseErrorValidation;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    @Autowired
    public UserController(UserService userService, UserFacade userFacade, ResponseErrorValidation responseErrorValidation, ModelMapper modelMapper, UserValidator userValidator) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }
    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.convertToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") Long userId){
        User user = userService.getUserById(userId);
        UserDTO userDTO = userFacade.convertToUserDTO(user);

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

        UserDTO userUpdated = userFacade.convertToUserDTO(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }


}
