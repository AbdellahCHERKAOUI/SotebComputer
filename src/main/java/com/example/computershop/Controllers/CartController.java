package com.example.computershop.Controllers;

import com.example.computershop.Dto.CartDTO;
import com.example.computershop.Services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping("/{cartId}/{productId}/{quantity}")
    @PreAuthorize("hasRole('USER') ")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(cartId, productId, quantity);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }
    @PutMapping("/carts/{cartId}/products/{productId}/quantity/{quantity}")
    @PreAuthorize("hasRole('USER') ")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }
    @DeleteMapping(value = "/deleteAllCartItems")
    @PreAuthorize("hasRole('USER') ")
    public void deleteAllCartitems(){
        cartService.deleteAllcartItems();
    }
}
