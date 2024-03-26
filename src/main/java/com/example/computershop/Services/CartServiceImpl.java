package com.example.computershop.Services;

import com.example.computershop.Dto.CartDTO;
import com.example.computershop.Dto.ProductDTO;
import com.example.computershop.Entities.Cart;
import com.example.computershop.Entities.CartItem;
import com.example.computershop.Entities.Product;
import com.example.computershop.Repositories.CartItemRepository;
import com.example.computershop.Repositories.CartRepository;
import com.example.computershop.Repositories.ProductRepository;
import com.example.computershop.exceptions.APIException;
import com.example.computershop.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{
    private CartRepository cartRepo;
    private ProductRepository productRepo;
    private CartItemRepository cartItemRepo;
    private ModelMapper modelMapper;

    public CartServiceImpl(CartRepository cartRepo, ProductRepository productRepo, CartItemRepository cartItemRepo, ModelMapper modelMapper) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.cartItemRepo = cartItemRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart =cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        Product product=productRepo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        CartItem cartItem = cartItemRepo.findCartItemByProductProductIdAndCartCartId(cartId, productId);

        if (cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepo.save(newCartItem);

        product.setQuantity(product.getQuantity() - quantity);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<ProductDTO> productDTOs = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

        cartDTO.setProducts(productDTOs);

        return cartDTO;

    }

    @Override
    public List<CartDTO> getAllCarts() {
        return null;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        return null;
    }

    @Override
    public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
        return null;
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {

    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        return null;
    }
}
