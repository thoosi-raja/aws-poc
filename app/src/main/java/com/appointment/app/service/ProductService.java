package com.appointment.app.service;

import com.appointment.app.entity.Product;
import com.appointment.app.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Value("${product.image.laptop}")
    private String laptopImageUrl;

    @Value("${product.image.smartphone}")
    private String smartphoneImageUrl;

    @Value("${product.image.headphones}")
    private String headphonesImageUrl;

    @Value("${product.image.tablet}")
    private String tabletImageUrl;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID().toString());
        }
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    // Method to initialize some sample products
    public void initializeSampleProducts() {
        if (productRepository.count() == 0) {
            Product laptop = new Product();
            laptop.setId(UUID.randomUUID().toString());
            laptop.setName("Laptop");
            laptop.setPrice(999.99);
            laptop.setImageUrl(laptopImageUrl);
            laptop.setDescription("High-performance laptop");
            productRepository.save(laptop);

            Product smartphone = new Product();
            smartphone.setId(UUID.randomUUID().toString());
            smartphone.setName("Smartphone");
            smartphone.setPrice(499.99);
            smartphone.setImageUrl(smartphoneImageUrl);
            smartphone.setDescription("Latest smartphone");
            productRepository.save(smartphone);

            Product headphones = new Product();
            headphones.setId(UUID.randomUUID().toString());
            headphones.setName("Headphones");
            headphones.setPrice(99.99);
            headphones.setImageUrl(headphonesImageUrl);
            headphones.setDescription("Wireless headphones");
            productRepository.save(headphones);

            Product tablet = new Product();
            tablet.setId(UUID.randomUUID().toString());
            tablet.setName("Tablet");
            tablet.setPrice(299.99);
            tablet.setImageUrl(tabletImageUrl);
            tablet.setDescription("Portable tablet");
            productRepository.save(tablet);
        }
    }
}
