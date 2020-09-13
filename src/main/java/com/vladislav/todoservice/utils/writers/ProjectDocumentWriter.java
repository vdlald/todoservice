package com.vladislav.todoservice.utils.writers;

import com.proto.todo.Project;
import com.vladislav.todoservice.documents.ProjectDocument;
import org.springframework.stereotype.Component;

@Component
public class ProjectDocumentWriter extends AbstractWriter<ProjectDocument, Project> {
    @Override
    public Project write(ProjectDocument document) {
        return Project.newBuilder()
                .setId(writeUuid(document.getId()))
                .setUserId(writeUuid(document.getUserId()))
                .setName(document.getName())
                .setIsDeleted(document.getIsDeleted())
                .setCreatedAt(writeLocalDateTime(document.getCreatedAt()))
                .build();
    }
}
