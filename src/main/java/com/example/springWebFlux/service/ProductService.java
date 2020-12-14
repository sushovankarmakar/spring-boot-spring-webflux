package com.example.springWebFlux.service;

import com.example.springWebFlux.domain.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {

    Mono<Product> findById(String productId);
    Flux<Product> findAll();
    Mono<Product> save(Product product);
    Mono<Product> deleteByProductId(String productId);

}
