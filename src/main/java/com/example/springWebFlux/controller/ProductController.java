package com.example.springWebFlux.controller;

import com.example.springWebFlux.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/product")
@Log4j2
public class ProductController {

    // https://stackoverflow.com/questions/39890849/what-exactly-is-field-injection-and-how-to-avoid-it
    //@Autowired    // Field injection is not recommended.
    private ProductService productService;


}
