package com.example.springWebFlux.controller;

import com.example.springWebFlux.domain.Product;
import com.example.springWebFlux.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest    // it is applied only on spring web flux component
@Log4j2
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    private WebTestClient webTestClient;

    @Autowired
    public ProductControllerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @Test
    public void test_getAllProducts() {
        when(productService.findAll())
                .thenReturn(
                        Flux.just(getProducts())
                );
        webTestClient.get()
                .uri("/api/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.[0].name").isEqualTo("Dell");

        log.info("Test get all products");
    }

    @Test
    public void test_saveProduct() {
        Product product = getProduct();
        when(productService.save(any(Product.class)))
                .thenReturn(
                        Mono.just(product)
                );
        webTestClient.post()
                .uri("/api/product")
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(product.getName())
                .jsonPath("$.price").isEqualTo(product.getPrice());

        log.info("Test save product");
    }

    @Test
    public void test_getProductById() {
        Product product = getProduct();
        when(productService.findById(product.getId()))
                .thenReturn(
                        Mono.just(product)
                );
        webTestClient.get()
                .uri("/api/product/" + product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(product.getName())
                .jsonPath("$.price").isEqualTo(product.getPrice());

        log.info("Test get product by id");
    }

    @Test
    public void test_updateProductById() {
        Product product = getProduct();
        when(productService.updateByProductId(anyString(), any(Product.class)))
                .thenReturn(
                        Mono.just(product)
                );
        webTestClient.put()
                .uri("/api/product/" + product.getId())
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(product.getName())
                .jsonPath("$.price").isEqualTo(product.getPrice());

        log.info("Test update product by id");
    }

    @Test
    public void test_deleteProductById() {
        Product product = getProduct();
        when(productService.deleteByProductId(product.getId()))
                .thenReturn(
                        Mono.just(product)
                );
        webTestClient.delete()
                .uri("/api/product/" + product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(product.getName())
                .jsonPath("$.price").isEqualTo(product.getPrice());

        log.info("Test get product by id");
    }


    private Product[] getProducts() {
        Product[] products = {
                new Product(UUID.randomUUID().toString(), "Dell",
                        ThreadLocalRandom.current().nextDouble(1000, 5000)),
                new Product(UUID.randomUUID().toString(), "Lenovo",
                        ThreadLocalRandom.current().nextDouble(1000, 5000)),
                new Product(UUID.randomUUID().toString(), "Asus",
                        ThreadLocalRandom.current().nextDouble(1000, 5000))
        };
        return products;
    }

    private Product getProduct() {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .name("Nokia")
                .price(ThreadLocalRandom.current().nextDouble(1000, 5000))
                .build();
        return product;
    }

}
