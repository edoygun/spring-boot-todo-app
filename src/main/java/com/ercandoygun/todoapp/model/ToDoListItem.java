package com.ercandoygun.todoapp.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ToDoListItem {

    @Id
    private String id;

    @Field
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters.")
    private String title;

    @Field
    @Size(min = 5, max = 500, message = "Description can be up to 500 characters.")
    private String description;

    @Field
    private boolean completed = false;

    @Field
    @NotNull(message = "User ID is mandatory.")
    private Long userId;

    @Field
    private Date createdDate = new Date();

    @Field
    private Date updatedDate = new Date();
}
