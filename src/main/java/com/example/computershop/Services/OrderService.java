package com.example.computershop.Services;

import com.example.computershop.Dto.OrderDTO;

import java.util.List;

public interface OrderService {
     OrderDTO placeOrder(String email,Long cartId);
     void deletAllOrders();
     List<OrderDTO> getOrdersByUser(String emailId);
}
