package com.example.computershop.Services;

import com.example.computershop.Dto.UserDTO;
import com.example.computershop.Entities.User;

public interface UserService {
    User registerUser(User user);

    UserDTO getUserById(Long userId);

    UserDTO updateUser(Long userId, UserDTO userDTO);

    String deleteUser(Long userId);
}