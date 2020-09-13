package com.vladislav.todoservice.utils.readers;

import com.proto.todo.Project;
import com.vladislav.todoservice.documents.ProjectDocument;
import org.springframework.stereotype.Component;

@Component
public class ProjectDocumentReader extends AbstractReader<ProjectDocument, Project> {
    @Override
    public ProjectDocument read(Project project) {
        return new ProjectDocument()
                .setId(readUuid(project.getId()))
                .setUserId(readUuid(project.getUserId()))
                .setName(project.getName())
                .setIsDeleted(project.getIsDeleted())
                .setCreatedAt(readLocalDateTime(project.getCreatedAt()));
    }
}
