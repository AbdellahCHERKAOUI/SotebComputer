package com.example.computershop.Services;

import com.example.computershop.Dto.OrderDTO;
import com.example.computershop.Dto.OrderItemDTO;
import com.example.computershop.Entities.*;
import com.example.computershop.Repositories.*;
import com.example.computershop.exceptions.APIException;
import com.example.computershop.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private CartRepository cartRepo;
    private OrderRepository orderRepo;
    private ProductService productService;
    private CartItemRepository cartItemRepo;
    private CartService cartService;
    private OrderItemRepository orderItemRepo;
    private ModelMapper modelMapper;
    private ProductRepository productRepo;

    public OrderServiceImpl(CartRepository cartRepo,OrderRepository orderRepo,CartService cartService,
                            CartItemRepository  cartItemRepo,OrderItemRepository orderItemRepo,
                            ModelMapper modelMapper,ProductRepository productRepo,ProductService productService){
        this.cartRepo=cartRepo;
        this.orderRepo=orderRepo;
       this.cartItemRepo=cartItemRepo;
       this.orderItemRepo=orderItemRepo;
       this.cartService=cartService;
       this.modelMapper=modelMapper;
       this.productRepo=productRepo;
       this.productRepo=productRepo;
    }
    @Override
    @Transactional
    public OrderDTO placeOrder(String email, Long cartId) {
        Cart cart = cartRepo.findCartByEmailAndCartId(email, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("cart", "cartId", cartId);
        }
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("No cart items found. Cannot place an order without items.");
        }
        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("order accepted !!!");
        Order savedOrder = orderRepo.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            if (product.getQuantity() < quantity) {
                throw new APIException("Not enough quantity available for product: " + product.getProductName());
            }
            product.setQuantity(product.getQuantity() - quantity);
            productRepo.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItems.add(orderItem);
        }

        if (orderItems.isEmpty()) {
            throw new APIException("No order");
        } else {
            orderItemRepo.saveAll(orderItems);
            // Delete products from cart only if order placement is successful
            for (CartItem cartItem : cartItems) {
                cartService.deleteProductFromCart(cartId, cartItem.getProduct().getProductId());
            }
        }

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));
        return orderDTO;
    }


    @Override
    public void deletAllOrders() {
        orderItemRepo.deleteAll();
    }
    @Override
    public List<OrderDTO> getOrdersByUser(String emailId) {
        List<Order> orders = orderRepo.findAllByEmail(emailId);

        List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        if (orderDTOs.size() == 0) {
            throw new APIException("No orders placed yet by the user with email: " + emailId);
        }

        return orderDTOs;
    }

}
