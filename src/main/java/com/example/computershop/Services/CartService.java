package com.example.computershop.Services;

import com.example.computershop.Dto.CartDTO;

import java.util.List;

public interface CartService {

        CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);

        List<CartDTO> getAllCarts();

        CartDTO getCart(String emailId, Long cartId);

        CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);


        String deleteProductFromCart(Long cartId, Long productId);
        void deleteAllcartItems();



}
