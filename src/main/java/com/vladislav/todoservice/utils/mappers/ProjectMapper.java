package com.vladislav.todoservice.utils.mappers;

import com.proto.todo.Project;
import com.vladislav.todoservice.documents.ProjectDocument;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class ProjectMapper implements PojoMapper<ProjectDocument, Project> {

    @Override
    public ProjectDocument toDocument(Project project) {
        return new ProjectDocument()
                .setId(UUID.fromString(project.getId()))
                .setUserId(UUID.fromString(project.getUserId()))
                .setName(project.getName())
                .setIsDeleted(project.getIsDeleted())
                .setCreatedAt(Instant.ofEpochMilli(project.getCreatedAt()).atOffset(ZoneOffset.UTC).toLocalDateTime());
    }

    @Override
    public Project toDto(ProjectDocument document) {
        return Project.newBuilder()
                .setId(document.getId().toString())
                .setUserId(document.getUserId().toString())
                .setName(document.getName())
                .setIsDeleted(document.getIsDeleted())
                .setCreatedAt(document.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                .build();
    }
}
