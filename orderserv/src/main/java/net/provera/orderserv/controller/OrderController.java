package net.provera.orderserv.controller;

import net.provera.orderserv.dao.model.Order;
import net.provera.orderserv.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrdersByCustomerId(@RequestParam Optional<String> customerId){
        if(customerId.isPresent()){
            return ResponseEntity.ok(orderService.getAllOrdersByUserId(customerId.get()));
        }
        else
            return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }
}
