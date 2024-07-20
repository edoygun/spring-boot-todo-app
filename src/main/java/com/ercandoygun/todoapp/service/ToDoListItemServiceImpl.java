package com.ercandoygun.todoapp.service;

import com.ercandoygun.todoapp.model.ToDoListItem;
import com.ercandoygun.todoapp.repository.ToDoListItemRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ToDoListItemServiceImpl implements ToDoListItemService {

    private final ToDoListItemRepository toDoListItemRepository;

    @Override
    public List<ToDoListItem> findAll() {
        return toDoListItemRepository.findAll();
    }

    @Override
    public List<ToDoListItem> findByUserId(Long userId) {
        return toDoListItemRepository.findByUserId(userId);
    }

    @Override
    public ToDoListItem save(ToDoListItem toDoListItem) {
        return toDoListItemRepository.save(toDoListItem);
    }
}
