package com.ercandoygun.todoapp.service;

import com.ercandoygun.todoapp.model.ToDoListItem;

import java.util.List;

public interface ToDoListItemService {
    List<ToDoListItem> findAll();
    List<ToDoListItem> findByUserId(Long userId);
    ToDoListItem save(ToDoListItem toDoListItem);
}
