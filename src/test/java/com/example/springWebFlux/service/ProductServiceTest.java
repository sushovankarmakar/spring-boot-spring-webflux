package com.example.springWebFlux.service;

import com.example.springWebFlux.domain.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@DataMongoTest
@Import(value = ProductServiceImpl.class)
@ActiveProfiles(value = "test")
@Log4j2
public class ProductServiceTest {

    private ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @Test
    public void saveTest() {
        Product product = getProduct();

        Mono<Product> productMono = productService.save(product);
        StepVerifier.create(productMono)
                .expectNextMatches(p -> StringUtils.hasText(p.getName()))
                .verifyComplete();

        log.info("Save Test");
    }

    // https://stackoverflow.com/questions/57221204/retrieve-all-flux-elements-in-stepverifier

    @Test
    public void findAllTest() {
        Product product = getProduct();
        Flux<Product> productFlux = productService.save(product)
                .thenMany(productService.findAll());

        StepVerifier.create(productFlux.log())
                .expectNextCount(1);
                //.expectNext(product)
                //.expectNextMatches(p -> p.getName().equals("Nokia"))
                //.verifyComplete();

        log.info("FindAll Test");
    }

    @Test
    public void updateTest() {
        Product product = getProduct();

        Mono<Product> productMono = productService.save(product);
        Product savedProduct = productMono.block();
        savedProduct.setName("Microsoft");
        Mono<Product> updatedProduct = productService.save(product);

        StepVerifier.create(updatedProduct)
                .expectNextMatches(p -> p.getName().equals("Microsoft"))
                .verifyComplete();

        log.info("Update Test");
    }

    @Test
    public void deleteTest() {
        Mono<Product> productMono = productService.save(getProduct())
                .flatMap(product -> productService.deleteByProductId(product.getId()));

        StepVerifier.create(productMono)
                .expectNextMatches(product -> product.getName().equals("Nokia"))
                .verifyComplete();

        log.info("Delete Test");
    }

    @Test
    public void findByIdTest() {
        Mono<Product> productMono = productService.save(getProduct())
                .flatMap(product -> productService.findById(product.getId()));

        StepVerifier.create(productMono)
                .expectNextMatches(product -> product.getName().equals("Nokia"))
                .verifyComplete();

        log.info("FindById Test");
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
