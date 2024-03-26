package com.example.computershop.Services;

import com.example.computershop.Dto.UserDTO;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    String deleteUser(Long userId);
}