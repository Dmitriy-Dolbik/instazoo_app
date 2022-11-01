package com.example.instazoo_app.controllers;

import com.example.instazoo_app.dto.UserDTO;
import com.example.instazoo_app.exceptions.UserWasNotUpdatedException;
import com.example.instazoo_app.facade.UserFacade;
import com.example.instazoo_app.models.User;
import com.example.instazoo_app.payload.response.ErrorResponse;
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

import static com.example.instazoo_app.util.ErrorUtil2.createErrorMessageToClient;

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
    @GetMapping("/")
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

        //Создали специальный класс для генерации сообщения об ошибке,
        //сообщение кладём в кастомный Exception, ловим Exception Handler'ом
        //и отправляем через кастомный Response (по Alishev'у)
        //Проще делается через ResponseErrorValidation, смотри PostController
        //Там мы сразу генерируем map c ошибками и отправляем клиенту
        //Без Exception'ов и Handler'ов

        User userToUpdate = userFacade.convertToUser(userDTO);
        userValidator.validate(userToUpdate, bindingResult);
        if (bindingResult.hasErrors()){
            String errorMsg = createErrorMessageToClient(bindingResult);
            throw new UserWasNotUpdatedException(errorMsg);
        }

        User userUpdated = userService.updateUser(userToUpdate, principal);

        UserDTO userUpdatedDTO = userFacade.convertToUserDTO(userUpdated);
        return new ResponseEntity<>(userUpdatedDTO, HttpStatus.OK);
    }
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(@NotNull final UserWasNotUpdatedException exc){
        ErrorResponse response = new ErrorResponse(exc.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
