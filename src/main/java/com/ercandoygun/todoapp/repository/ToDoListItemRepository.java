package com.ercandoygun.todoapp.repository;

import com.ercandoygun.todoapp.model.ToDoListItem;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToDoListItemRepository extends CouchbaseRepository<ToDoListItem, String> {
    List<ToDoListItem> findByUserId(String userId);
}
