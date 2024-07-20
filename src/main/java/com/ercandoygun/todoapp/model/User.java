package com.ercandoygun.todoapp.model;

import com.ercandoygun.todoapp.service.validation.annotation.UniqueEmail;
import com.ercandoygun.todoapp.service.validation.annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class User {

    @Id
    private String id;

    @Field
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    @UniqueUsername
    private String name;

    @Field
    @Email(message = "Email should be valid")
    @UniqueEmail
    private String email;

    @Field
    @NotBlank
    private String password;

    @Field
    private Date createdDate = new Date();

    @Field
    private Date updatedDate = new Date();
}
