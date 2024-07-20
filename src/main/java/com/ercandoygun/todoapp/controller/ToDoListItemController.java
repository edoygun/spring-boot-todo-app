package com.ercandoygun.todoapp.controller;

import com.ercandoygun.todoapp.model.ToDoListItem;
import com.ercandoygun.todoapp.service.ToDoListItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/todo-items")
@AllArgsConstructor
@Validated
public class ToDoListItemController {

    private final ToDoListItemService toDoListItemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ToDoListItem> getAllToDoListItems() {
        log.info("Fetching all ToDo list items");
        return toDoListItemService.findAll();
    }

    @GetMapping("/by-user")
    @ResponseStatus(HttpStatus.OK)
    public List<ToDoListItem> getAllToDoListItemsByUser(@RequestParam @NotNull(message = "User ID is mandatory") Long userId) {
        log.info("Fetching ToDo list items for user id: {}", userId);
        return toDoListItemService.findByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ToDoListItem> createToDoListItem(@RequestBody @Valid ToDoListItem toDoListItem) {
        log.info("Creating new ToDo list item: {}", toDoListItem);
        ToDoListItem createdItem = toDoListItemService.save(toDoListItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }
}
