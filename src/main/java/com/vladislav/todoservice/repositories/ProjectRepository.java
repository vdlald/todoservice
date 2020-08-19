package com.vladislav.todoservice.repositories;

import com.vladislav.todoservice.documents.ProjectDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends MongoRepository<ProjectDocument, UUID> {
    List<ProjectDocument> findAllByUserId(UUID userId);
}
