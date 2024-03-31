package com.example.computershop.Services;

import com.example.computershop.Dto.OrderDTO;
import com.example.computershop.Dto.OrderItemDTO;
import com.example.computershop.Entities.*;
import com.example.computershop.Repositories.CartItemRepository;
import com.example.computershop.Repositories.CartRepository;
import com.example.computershop.Repositories.OrderRepository;
import com.example.computershop.Repositories.OrderItemRepository;
import com.example.computershop.exceptions.APIException;
import com.example.computershop.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private CartRepository cartRepo;
    private OrderRepository orderRepo;
    private CartItemRepository cartItemRepo;
    private CartService cartService;
    private OrderItemRepository orderItemRepo;
    private ModelMapper modelMapper;
    public OrderServiceImpl(CartRepository cartRepo,OrderRepository orderRepo,CartService cartService,
                            CartItemRepository  cartItemRepo,OrderItemRepository orderItemRepo,ModelMapper modelMapper){
        this.cartRepo=cartRepo;
        this.orderRepo=orderRepo;
       this.cartItemRepo=cartItemRepo;
       this.orderItemRepo=orderItemRepo;
       this.cartService=cartService;
       this.modelMapper=modelMapper;
    }
    @Override
    public OrderDTO placeOrder(String email, Long cartId) {
        Cart cart=cartRepo.findCartByEmailAndCartId(email,cartId);
        if (cart == null){

            throw new ResourceNotFoundException("cart","cartId",cartId);
        }
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("No cart items found. Cannot place an order without items.");
        }
        Order order=new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("order accepted !!!");
        Order savedorder=orderRepo.save(order);

        List<OrderItem> orderItems=new ArrayList<>();
        for (CartItem cartItem:cartItems){
               OrderItem orderItem=new OrderItem();
               orderItem.setOrder(savedorder);
               orderItem.setProduct(cartItem.getProduct());
               orderItem.setDiscount(cartItem.getDiscount());
               orderItem.setQuantity(cartItem.getQuantity());
               orderItem.setOrderedProductPrice(cartItem.getProductPrice());
               orderItems.add(orderItem);

        }
        if (orderItems.isEmpty()){
            throw new APIException("No order");
        }else {
            orderItemRepo.saveAll(orderItems);
        }
        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();

            Product product = item.getProduct();

            cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

            product.setQuantity(product.getQuantity() - quantity);
        });

        OrderDTO orderDTO = modelMapper.map(savedorder, OrderDTO.class);

        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        return orderDTO;
    }

    @Override
    public void deletAllOrders() {
        orderItemRepo.deleteAll();
    }
}
