package com.edsuuu.list.controller;

import com.edsuuu.list.dto.CreateListDTO;
import com.edsuuu.list.service.ListService;
import com.edsuuu.list.database.model.ListEntity;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @PostMapping
    public ResponseEntity<ListEntity> create(@RequestBody CreateListDTO body) {
        ListEntity createdList = listService.createList(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListEntity> update(@PathVariable Long id, @RequestBody CreateListDTO body) {
        ListEntity updatedList = listService.updateList(id, body);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        listService.deleteList(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ListEntity>> listAll() {
        List<ListEntity> allLists = this.listService.listAll();

        if (allLists.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(allLists);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public String search(@RequestParam(value = "name", required = false) String name) {
        if (name == null) {
            return "Nome não informado";
        }

        return "Hello World " + name;
    }
}
