package com.levi9.socialnetwork.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }
}
