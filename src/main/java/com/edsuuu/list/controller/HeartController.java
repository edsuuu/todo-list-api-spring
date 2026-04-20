package com.edsuuu.list.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heart")
public class HeartController {

    @GetMapping
    public ResponseEntity<String> heart() {
        return new ResponseEntity<>("I'm alive", HttpStatus.OK);
    }
}
