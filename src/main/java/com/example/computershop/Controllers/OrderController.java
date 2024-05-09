package com.example.computershop.Controllers;

import com.example.computershop.Dto.OrderDTO;
import com.example.computershop.Services.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ("/ord"))
public class OrderController {
    private OrderService  orderService;
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PostMapping(value = "/{email}/{cartId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public OrderDTO placeOrder(@PathVariable String email,@PathVariable Long cartId){
        return orderService.placeOrder(email,cartId);
    }
    @DeleteMapping(value = "/deleteAllOrders")
    public void deleteAllOrders(){
        orderService.deletAllOrders();
    }
    @GetMapping(value = "/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<OrderDTO> getOrdersByUser(@PathVariable String email){
        return orderService.getOrdersByUser(email);
    }
}
