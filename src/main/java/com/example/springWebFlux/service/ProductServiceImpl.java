package com.example.springWebFlux.service;

import com.example.springWebFlux.domain.Product;
import com.example.springWebFlux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Product> findById(String productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> save(Product product) {
        product.setId(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> deleteByProductId(String productId) {

        return productRepository.findById(productId)
                .flatMap(
                        toBeDeletedProduct -> productRepository
                                .deleteById(toBeDeletedProduct.getId())
                                .thenReturn(toBeDeletedProduct)
                );
    }

    @Override
    public Mono<Product> updateByProductId(String productId, Product toBeUpdatedProduct) {
        return productRepository.findById(productId)
                .flatMap(
                        savedProduct -> {
                            savedProduct.setName(toBeUpdatedProduct.getName());
                            savedProduct.setPrice(toBeUpdatedProduct.getPrice());
                            return productRepository.save(savedProduct);
                        }
                );
    }
}
