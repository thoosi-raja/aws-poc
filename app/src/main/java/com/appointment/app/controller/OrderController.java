package com.appointment.app.controller;

import com.appointment.app.entity.Order;
import com.appointment.app.entity.Product;
import com.appointment.app.repository.OrderRepository;
import com.appointment.app.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @Autowired
    public OrderController(OrderRepository orderRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            // Save the order
            Order savedOrder = orderRepository.save(order);

            // Send confirmation email (this won't throw exceptions now)
            List<String> productNames = order.getProducts().stream()
                    .map(Product::getName)
                    .collect(Collectors.toList());

            emailService.sendOrderConfirmation(
                order.getCustomerEmail(),
                order.getCustomerName(),
                productNames,
                order.getTotalAmount()
            );

            return ResponseEntity.ok(Map.of(
                "message", "Order placed successfully",
                "orderId", savedOrder.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Failed to create order",
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
}
