package com.example.springWebFlux.service;

import com.example.springWebFlux.domain.Product;
import com.example.springWebFlux.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

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
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> deleteByProductId(String productId) {

        return productRepository.findById(productId)
                .flatMap(
                        product -> productRepository
                                .deleteById(product.getId())
                                .thenReturn(product)
                );
    }
}
