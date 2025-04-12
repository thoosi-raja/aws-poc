package com.appointment.app.controller;

import com.appointment.app.entity.EcommerceItem;
import com.appointment.app.services.EcommerceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EcommerceController {

    private final EcommerceService ecommerceService;

    public EcommerceController(EcommerceService ecommerceService) {
        this.ecommerceService = ecommerceService;
    }

    // Product endpoints
    @PostMapping("/products")
    public ResponseEntity<EcommerceItem> createProduct(
            @RequestBody Map<String, Object> productRequest) {
        EcommerceItem product = ecommerceService.createProduct(
            (String) productRequest.get("name"),
            ((Number) productRequest.get("price")).doubleValue(),
            ((Number) productRequest.get("stock")).intValue(),
            (String) productRequest.get("category")
        );
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products/category/{category}")
    public ResponseEntity<List<EcommerceItem>> getProductsByCategory(
            @PathVariable String category) {
        List<EcommerceItem> products = ecommerceService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    // Order endpoints
    @PostMapping("/orders")
    public ResponseEntity<EcommerceItem> createOrder(
            @RequestBody Map<String, Object> orderRequest) {
        @SuppressWarnings("unchecked")
        Map<String, Integer> productQuantities = (Map<String, Integer>) orderRequest.get("products");
        
        EcommerceItem order = ecommerceService.createOrder(
            (String) orderRequest.get("userId"),
            productQuantities,
            ((Number) orderRequest.get("totalAmount")).doubleValue()
        );
        return ResponseEntity.ok(order);
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<List<EcommerceItem>> getUserOrders(
            @PathVariable String userId) {
        List<EcommerceItem> orders = ecommerceService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam String userId,
            @RequestBody Map<String, String> statusUpdate) {
        ecommerceService.updateOrderStatus(orderId, userId, statusUpdate.get("status"));
        return ResponseEntity.ok().build();
    }

    // User endpoints
    @PostMapping("/users")
    public ResponseEntity<EcommerceItem> createUser(
            @RequestBody Map<String, String> userRequest) {
        EcommerceItem user = ecommerceService.createUser(
            userRequest.get("email"),
            userRequest.get("name"),
            userRequest.get("address")
        );
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<EcommerceItem> getUserByEmail(
            @RequestParam String email) {
        EcommerceItem user = ecommerceService.getUserByEmail(email);
        return user != null ? 
            ResponseEntity.ok(user) : 
            ResponseEntity.notFound().build();
    }
}
