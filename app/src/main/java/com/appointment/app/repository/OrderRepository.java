package com.appointment.app.repository;

import com.appointment.app.entity.Order;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepository {
    private final DynamoDbTable<Order> orderTable;

    public OrderRepository(DynamoDbEnhancedClient enhancedClient) {
        this.orderTable = enhancedClient.table("Orders", TableSchema.fromBean(Order.class));
    }

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(UUID.randomUUID().toString());
        }
        orderTable.putItem(order);
        return order;
    }

    public Optional<Order> findById(String id) {
        return Optional.ofNullable(orderTable.getItem(r -> r.key(k -> k.partitionValue(id))));
    }

    public List<Order> findAll() {
        PageIterable<Order> results = orderTable.scan();
        List<Order> orders = new ArrayList<>();
        results.items().forEach(orders::add);
        return orders;
    }

    public void deleteById(String id) {
        orderTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    public List<Order> saveAll(List<Order> orders) {
        orders.forEach(this::save);
        return orders;
    }

    public void deleteAll() {
        findAll().forEach(order -> deleteById(order.getId()));
    }

    public long count() {
        return orderTable.scan().items().stream().count();
    }
}
