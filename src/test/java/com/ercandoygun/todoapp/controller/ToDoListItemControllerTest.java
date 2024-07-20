package com.ercandoygun.todoapp.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.ercandoygun.todoapp.auth.util.JwtUtil;
import com.ercandoygun.todoapp.model.ToDoListItem;
import com.ercandoygun.todoapp.service.ToDoListItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@WebMvcTest(value = ToDoListItemController.class)
public class ToDoListItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private ToDoListItemService toDoListItemService;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void getAllToDoListItems_ReturnsAllItems() throws Exception {
        ToDoListItem item1 = new ToDoListItem();
        item1.setTitle("Title 1");
        ToDoListItem item2 = new ToDoListItem();
        item2.setTitle("Title 2");
        List<ToDoListItem> items = Arrays.asList(item1, item2);

        when(toDoListItemService.findAll()).thenReturn(items);

        mockMvc.perform(get("/todo-items")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Title 1")))
                .andExpect(jsonPath("$[1].title", is("Title 2")));
    }

    @Test
    public void getAllToDoListItemsByUser_ReturnsItemsForUser() throws Exception {
        ToDoListItem item1 = new ToDoListItem();
        item1.setTitle("Title 1");
        item1.setCompleted(false);
        item1.setDescription("Description 1");
        item1.setUserId("1");
        List<ToDoListItem> items = Arrays.asList(item1);

        when(toDoListItemService.findByUserId("1")).thenReturn(items);

        mockMvc.perform(get("/todo-items/by-user")
                        .param("userId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Title 1")));
    }

    @Test
    public void createToDoListItem_CreatesNewItem() throws Exception {
        ToDoListItem newItem = new ToDoListItem();
        newItem.setTitle("new title");
        newItem.setCompleted(false);
        newItem.setUserId("1");
        ToDoListItem savedItem = new ToDoListItem();
        savedItem.setTitle("new title");
        savedItem.setCompleted(false);
        savedItem.setUserId("1");

        when(toDoListItemService.save(any(ToDoListItem.class))).thenReturn(savedItem);

        mockMvc.perform(post("/todo-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("new title")))
                .andExpect(jsonPath("$.completed", is(false)));
    }

    @Test
    public void createToDoListItem_WithInvalidData_ReturnsBadRequest() throws Exception {
        ToDoListItem newItem = new ToDoListItem();
        newItem.setTitle("");
        newItem.setUserId("1");

        MvcResult result = mockMvc.perform(post("/todo-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newItem)))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), "[\"Title must be between 1 and 100 characters.\"]");
    }

    @Test
    public void getAllToDoListItemsByUser_WithMissingUserId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/todo-items/by-user").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", is("Required parameter 'userId' is not present.")));
    }

    @Test
    void completeToDoListItem() throws Exception {
        ToDoListItem completedItem = new ToDoListItem("1", "Complete homework", "homework description", true, "1", new Date(), new Date());
        when(toDoListItemService.markAsCompleted(any(String.class))).thenReturn(completedItem);

        mockMvc.perform(put("/todo-items/complete")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteToDoListItem() throws Exception {
        doNothing().when(toDoListItemService).delete(any(String.class));

        mockMvc.perform(delete("/todo-items")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
