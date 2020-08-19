package com.vladislav.todoservice.repositories;

import com.vladislav.todoservice.documents.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends MongoRepository<Task, UUID> {
}
