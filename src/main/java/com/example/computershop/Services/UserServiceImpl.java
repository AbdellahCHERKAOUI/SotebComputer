package com.example.computershop.Services;

import com.example.computershop.Dto.ProductDTO;
import com.example.computershop.Dto.UserDTO;
import com.example.computershop.Entities.Cart;
import com.example.computershop.Entities.User;
import com.example.computershop.Repositories.UserRepository;
import com.example.computershop.exceptions.APIException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
    private ModelMapper modelMapper;
    private UserRepository userRepo;

    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepo) {
        this.modelMapper = modelMapper;
        this.userRepo = userRepo;
    }

    @Override
    public User registerUser(User user) {
           // User user = modelMapper.map(userDTO, User.class);
            Cart cart = new Cart();
            user.setCart(cart);
            cart.setUser(user);
            User userSaved=userRepo.save(user);
       return user;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        return null;
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public String deleteUser(Long userId) {
        return null;
    }
}
