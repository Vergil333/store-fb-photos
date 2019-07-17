package com.machava.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Web {

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }

}
