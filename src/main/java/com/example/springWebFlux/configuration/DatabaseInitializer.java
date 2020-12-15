package com.example.springWebFlux.configuration;

import com.example.springWebFlux.domain.Product;
import com.example.springWebFlux.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@Log4j2
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private ProductRepository productRepository;

    @Autowired
    public DatabaseInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        productRepository.deleteAll()
                .thenMany(
                        Flux.just(getData())
                        .map(
                                name -> new Product(
                                        UUID.randomUUID().toString(),
                                        name,
                                        ThreadLocalRandom.current().nextDouble(1000, 5000)
                                )
                        )
                        .flatMap(productRepository::save)
                )
                .thenMany(productRepository.findAll())
                .subscribe(product -> log.info("Saved product {}", product));
    }

    private String[] getData() {
        return new String[]{
                "Apple", "Samsung", "Huawei", "Oppo", "OnePlus"
        };
    }
}
