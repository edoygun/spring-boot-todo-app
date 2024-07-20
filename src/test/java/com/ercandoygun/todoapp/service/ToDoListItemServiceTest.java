package com.ercandoygun.todoapp.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ercandoygun.todoapp.model.ToDoListItem;
import com.ercandoygun.todoapp.repository.ToDoListItemRepository;

@ExtendWith(MockitoExtension.class)
public class ToDoListItemServiceTest {

    @Mock
    private ToDoListItemRepository toDoListItemRepository;

    @InjectMocks
    private ToDoListItemServiceImpl toDoListItemService;

    private ToDoListItem createToDoListItem(String description, boolean completed, Long userId) {
        ToDoListItem item = new ToDoListItem();
        item.setDescription(description);
        item.setCompleted(completed);
        item.setUserId(userId);
        return item;
    }

    @Test
    public void findAll_ReturnsAllItems() {
        List<ToDoListItem> expectedItems = Arrays.asList(
                createToDoListItem("Task 1", false, 101L),
                createToDoListItem( "Task 2", true, 102L)
        );

        when(toDoListItemRepository.findAll()).thenReturn(expectedItems);

        List<ToDoListItem> actualItems = toDoListItemService.findAll();

        assertNotNull(actualItems);
        assertEquals(2, actualItems.size());
        assertEquals("Task 1", actualItems.get(0).getDescription());
        assertTrue(actualItems.get(1).isCompleted());
    }

    @Test
    public void findByUserId_ReturnsItemsForUser() {
        Long userId = 101L;
        List<ToDoListItem> expectedItems = Arrays.asList(createToDoListItem("User 101 Task", false, userId));
        when(toDoListItemRepository.findByUserId(userId)).thenReturn(expectedItems);

        List<ToDoListItem> actualItems = toDoListItemService.findByUserId(userId);

        assertNotNull(actualItems);
        assertEquals(1, actualItems.size());
        assertEquals(userId, actualItems.get(0).getUserId());
    }

    @Test
    public void save_SavesAndReturnsItem() {
        ToDoListItem newItem = createToDoListItem( "New Task", false, 103L);
        ToDoListItem savedItem = createToDoListItem( "New Task", false, 103L);
        when(toDoListItemRepository.save(newItem)).thenReturn(savedItem);

        ToDoListItem result = toDoListItemService.save(newItem);

        assertNotNull(result);
        assertEquals("New Task", result.getDescription());
        assertEquals(103L, result.getUserId());
    }
}
