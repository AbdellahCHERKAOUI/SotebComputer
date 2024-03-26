package com.example.computershop.Controllers;

import com.example.computershop.Dto.UserDTO;
import com.example.computershop.Entities.User;
import com.example.computershop.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;
    private ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @PostMapping(value = "/createUser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        UserDTO savedUser=userService.registerUser(userDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }
}
