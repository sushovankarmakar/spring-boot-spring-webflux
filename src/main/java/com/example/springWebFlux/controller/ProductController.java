package com.example.springWebFlux.controller;

import com.example.springWebFlux.domain.Product;
import com.example.springWebFlux.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/product")
@Log4j2
public class ProductController {

    // https://stackoverflow.com/questions/39890849/what-exactly-is-field-injection-and-how-to-avoid-it
    //@Autowired    // Field injection is not recommended.
    private ProductService productService;

    // we can replace the below constructor by lombok.AllArgsConstructor annotation also.
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> getAllProducts() {
        log.info("Get all products");
        return productService.findAll();
    }

    @PostMapping
    public Mono<ResponseEntity<Product>> saveProduct(@RequestBody Product product) {
        log.info("Save product");
        return productService.save(product)
                .map(savedProduct -> new ResponseEntity<>(savedProduct, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable(value = "id") String productId) {
        log.info("Get product by id");
        return productService.findById(productId)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/{id}")
    public Mono<ResponseEntity<Product>> updateProductById(@PathVariable(value = "id") String productId,
                                                           @RequestBody Product toBeUpdatedProduct) {
        log.info("Update product by id");
        return productService.updateByProductId(productId, toBeUpdatedProduct)
                .map(updatedProduct -> new ResponseEntity<>(updatedProduct, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{id}")
    public Mono<ResponseEntity<Product>> deleteProductById(@PathVariable(value = "id") String productId) {
        log.info("Delete product by id");
        return productService.deleteByProductId(productId)
                .map(deletedProduct -> new ResponseEntity<>(deletedProduct, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
