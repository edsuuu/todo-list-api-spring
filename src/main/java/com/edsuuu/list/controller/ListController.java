package com.edsuuu.list.controller;

import com.edsuuu.list.dto.CreateListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("/list")
public class ListController {

    @PostMapping
    public String list() {



        return "list";
    }


    @PostMapping("/create")
    public ResponseEntity<CreateListDTO> create(@RequestBody CreateListDTO body) {

        System.out.println(body.getName());

        System.out.println(body.getDescription());

        return ResponseEntity.ok(body);
    }

//    @GetMapping
//    public ResponseEntity<String> heart() {
//        return new ResponseEntity<>("I'm alive", HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity<String> listAll() {
//        return ResponseEntity.noContent().build();
//    }


}
