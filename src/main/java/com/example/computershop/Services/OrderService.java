package com.example.computershop.Services;

import com.example.computershop.Dto.OrderDTO;

public interface OrderService {
     OrderDTO placeOrder(String email,Long cartId);
     void deletAllOrders();
}
