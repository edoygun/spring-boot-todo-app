package com.ercandoygun.todoapp.repository;

import com.ercandoygun.todoapp.model.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CouchbaseRepository<User, String> {
    User findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
