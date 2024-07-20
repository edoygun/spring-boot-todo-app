package com.ercandoygun.todoapp.service;

import com.ercandoygun.todoapp.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private ToDoListItem createToDoListItem(String description, boolean completed, String userId) {
        ToDoListItem item = new ToDoListItem();
        item.setDescription(description);
        item.setCompleted(completed);
        item.setUserId(userId);
        return item;
    }

    @Test
    public void findAll_ReturnsAllItems() {
        List<ToDoListItem> expectedItems = Arrays.asList(
                createToDoListItem("Task 1", false, "101"),
                createToDoListItem( "Task 2", true, "102")
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
        String  userId = "101";
        List<ToDoListItem> expectedItems = Arrays.asList(createToDoListItem("User 101 Task", false, userId));
        when(toDoListItemRepository.findByUserId(userId)).thenReturn(expectedItems);

        List<ToDoListItem> actualItems = toDoListItemService.findByUserId(userId);

        assertNotNull(actualItems);
        assertEquals(1, actualItems.size());
        assertEquals(userId, actualItems.get(0).getUserId());
    }

    @Test
    public void save_SavesAndReturnsItem() {
        ToDoListItem newItem = createToDoListItem( "New Task", false, "103");
        ToDoListItem savedItem = createToDoListItem( "New Task", false, "103");
        when(toDoListItemRepository.save(newItem)).thenReturn(savedItem);

        ToDoListItem result = toDoListItemService.save(newItem);

        assertNotNull(result);
        assertEquals("New Task", result.getDescription());
        assertEquals("103", result.getUserId());
    }

    @Test
    void markAsCompleted_ItemExists_ItemMarkedCompleted() {
        ToDoListItem item = new ToDoListItem();
        item.setId("1");
        item.setCompleted(false);
        when(toDoListItemRepository.findById("1")).thenReturn(Optional.of(item));
        when(toDoListItemRepository.save(any(ToDoListItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ToDoListItem updatedItem = toDoListItemService.markAsCompleted("1");

        assertTrue(updatedItem.isCompleted());
        verify(toDoListItemRepository).save(item);
    }

    @Test
    void markAsCompleted_ItemDoesNotExist_ThrowsException() {
        when(toDoListItemRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> toDoListItemService.markAsCompleted("1"));
    }

    @Test
    void delete_ItemExists_ItemDeleted() {
        ToDoListItem item = new ToDoListItem();
        item.setId("1");

        toDoListItemService.delete("1");

        verify(toDoListItemRepository).deleteById("1");
    }
}
