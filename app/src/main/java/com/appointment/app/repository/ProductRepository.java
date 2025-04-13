package com.appointment.app.repository;

import com.appointment.app.entity.Product;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final DynamoDbTable<Product> productTable;

    public ProductRepository(DynamoDbEnhancedClient enhancedClient) {
        this.productTable = enhancedClient.table("Products", TableSchema.fromBean(Product.class));
    }

    public Product save(Product product) {
        productTable.putItem(product);
        return product;
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(productTable.getItem(r -> r.key(k -> k.partitionValue(id))));
    }

    public List<Product> findAll() {
        PageIterable<Product> results = productTable.scan();
        List<Product> products = new ArrayList<>();
        results.items().forEach(products::add);
        return products;
    }

    public void deleteById(String id) {
        productTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    public List<Product> saveAll(List<Product> products) {
        products.forEach(this::save);
        return products;
    }

    public void deleteAll() {
        findAll().forEach(product -> deleteById(product.getId()));
    }

    public long count() {
        return productTable.scan().items().stream().count();
    }
}
