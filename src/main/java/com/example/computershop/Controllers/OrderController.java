package com.example.computershop.Controllers;

import com.example.computershop.Dto.OrderDTO;
import com.example.computershop.Services.OrderService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private OrderService  orderService;
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PostMapping(value = "/{email}/{cartId}")
    public OrderDTO placeOrder(@PathVariable String email,@PathVariable Long cartId){
        return orderService.placeOrder(email,cartId);
    }
    @DeleteMapping(value = "/deleteAllOrders")
    public void deleteAllOrders(){
        orderService.deletAllOrders();
    }

}
