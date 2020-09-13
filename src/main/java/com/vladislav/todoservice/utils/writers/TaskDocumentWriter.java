package com.vladislav.todoservice.utils.writers;

import com.proto.todo.Task;
import com.vladislav.todoservice.documents.TaskDocument;
import org.springframework.stereotype.Component;

@Component
public class TaskDocumentWriter extends AbstractWriter<TaskDocument, Task> {
    @Override
    public Task write(TaskDocument document) {
        return Task.newBuilder()
                .setId(writeUuid(document.getId()))
                .setUserId(writeUuid(document.getUserId()))
                .setProjectId(writeUuid(document.getProjectId()))
                .setTitle(document.getTitle())
                .setContent(document.getContent())
                .setCompleted(document.getCompleted())
                .setDeadline(writeLocalDateTime(document.getDeadline()))
                .setCreatedAt(writeLocalDateTime(document.getCreatedAt()))
                .setCompletedAt(writeLocalDateTime(document.getCompletedAt()))
                .build();
    }
}
