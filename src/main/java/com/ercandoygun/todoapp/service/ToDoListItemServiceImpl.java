package com.ercandoygun.todoapp.service;

import com.ercandoygun.todoapp.exception.ResourceNotFoundException;
import com.ercandoygun.todoapp.model.ToDoListItem;
import com.ercandoygun.todoapp.repository.ToDoListItemRepository;
import lombok.AllArgsConstructor;
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
    public List<ToDoListItem> findByUserId(String userId) {
        return toDoListItemRepository.findByUserId(userId);
    }

    @Override
    public ToDoListItem save(ToDoListItem toDoListItem) {
        return toDoListItemRepository.save(toDoListItem);
    }

    @Override
    public ToDoListItem markAsCompleted(String id) {
        ToDoListItem item = toDoListItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
        item.setCompleted(true);
        return toDoListItemRepository.save(item);
    }

    @Override
    public void delete(String id) {
        toDoListItemRepository.deleteById(id);
    }
}
