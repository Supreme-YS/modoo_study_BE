package com.modoostudy.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class testController {
    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello!!!");
    }
}
