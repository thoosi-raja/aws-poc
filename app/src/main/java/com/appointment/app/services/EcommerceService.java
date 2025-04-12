package com.appointment.app.services;

import com.appointment.app.entity.EcommerceItem;
import com.appointment.app.repository.EcommerceRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EcommerceService {

    private final EcommerceRepository repository;

    public EcommerceService(EcommerceRepository repository) {
        this.repository = repository;
    }

    // Product operations
    public EcommerceItem createProduct(String name, double price, int stock, String category) {
        String productId = UUID.randomUUID().toString();
        EcommerceItem item = new EcommerceItem();
        
        item.setPk("PRODUCT#" + productId);
        item.setSk("METADATA#" + productId);
        item.setGsi1pk("CATEGORY#" + category);
        item.setGsi1sk("PRODUCT#" + name);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", name);
        attributes.put("price", price);
        attributes.put("stock", stock);
        attributes.put("category", category);
        attributes.put("type", "product");
        item.setAttributes(attributes);

        return repository.save(item);
    }

    public List<EcommerceItem> getProductsByCategory(String category) {
        return repository.queryByGsi1("CATEGORY#" + category, "PRODUCT#");
    }

    // Order operations
    public EcommerceItem createOrder(String userId, Map<String, Integer> productQuantities, double totalAmount) {
        String orderId = UUID.randomUUID().toString();
        EcommerceItem orderItem = new EcommerceItem();
        
        orderItem.setPk("USER#" + userId);
        orderItem.setSk("ORDER#" + orderId);
        orderItem.setGsi1pk("ORDER#" + orderId);
        orderItem.setGsi1sk("STATUS#PENDING");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("orderId", orderId);
        attributes.put("userId", userId);
        attributes.put("products", productQuantities);
        attributes.put("totalAmount", totalAmount);
        attributes.put("status", "PENDING");
        attributes.put("type", "order");
        attributes.put("createdAt", System.currentTimeMillis());
        orderItem.setAttributes(attributes);

        return repository.save(orderItem);
    }

    public List<EcommerceItem> getUserOrders(String userId) {
        return repository.queryByPartitionKey("USER#" + userId);
    }

    // User operations
    public EcommerceItem createUser(String email, String name, String address) {
        String userId = UUID.randomUUID().toString();
        EcommerceItem userItem = new EcommerceItem();
        
        userItem.setPk("USER#" + userId);
        userItem.setSk("PROFILE#" + userId);
        userItem.setGsi1pk("EMAIL#" + email);
        userItem.setGsi1sk("USER#" + userId);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", userId);
        attributes.put("email", email);
        attributes.put("name", name);
        attributes.put("address", address);
        attributes.put("type", "user");
        attributes.put("createdAt", System.currentTimeMillis());
        userItem.setAttributes(attributes);

        return repository.save(userItem);
    }

    public EcommerceItem getUserByEmail(String email) {
        List<EcommerceItem> users = repository.queryByGsi1("EMAIL#" + email, null);
        return users.isEmpty() ? null : users.get(0);
    }

    public void updateOrderStatus(String orderId, String userId, String newStatus) {
        EcommerceItem order = repository.findById("USER#" + userId, "ORDER#" + orderId);
        if (order != null) {
            Map<String, Object> attributes = order.getAttributes();
            attributes.put("status", newStatus);
            order.setGsi1sk("STATUS#" + newStatus);
            repository.save(order);
        }
    }
}
