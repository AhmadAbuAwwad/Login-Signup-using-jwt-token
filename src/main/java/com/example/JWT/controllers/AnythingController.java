package com.example.JWT.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secured")
public class AnythingController {
    @GetMapping("/anythingSecured")
    public ResponseEntity<String> getAllAnything() {
        return ResponseEntity.ok("Any thing secured");
    }
}
