package com.vladislav.todoservice.utils.mappers;

import com.proto.todo.Task;
import com.vladislav.todoservice.documents.TaskDocument;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class TaskMapper implements PojoMapper<TaskDocument, Task> {
    @Override
    public TaskDocument toDocument(Task task) {
        final TaskDocument taskDocument = new TaskDocument();
        if (!task.getId().isEmpty()) {
            taskDocument.setId(UUID.fromString(task.getId()));
        }
        if (!task.getUserId().isEmpty()) {
            taskDocument.setUserId(UUID.fromString(task.getUserId()));
        }
        if (!task.getProjectId().isEmpty()) {
            taskDocument.setProjectId(UUID.fromString(task.getProjectId()));
        }
        return taskDocument
                .setTitle(task.getTitle())
                .setContent(task.getContent())
                .setCompleted(task.getCompleted())
                .setDeadline(Instant.ofEpochMilli(task.getDeadline()).atOffset(ZoneOffset.UTC).toLocalDateTime())
                .setCreatedAt(Instant.ofEpochMilli(task.getCreatedAt()).atOffset(ZoneOffset.UTC).toLocalDateTime())
                .setCompletedAt(Instant.ofEpochMilli(task.getCompletedAt()).atOffset(ZoneOffset.UTC).toLocalDateTime())
                .setIsDeleted(task.getIsDeleted());
    }

    @Override
    public Task toDto(TaskDocument document) {
        return Task.newBuilder()
                .setId(document.getId().toString())
                .setUserId(document.getUserId().toString())
                .setProjectId(uuidToString(document.getProjectId()))
                .setTitle(document.getTitle())
                .setContent(document.getContent())
                .setCompleted(document.getCompleted())
                .setDeadline(localDateTimeToEpochSecond(document.getDeadline()))
                .setCreatedAt(localDateTimeToEpochSecond(document.getCreatedAt()))
                .setCompletedAt(localDateTimeToEpochSecond(document.getCompletedAt()))
                .build();
    }

    private static String uuidToString(UUID uuid) {
        if (uuid == null) {
            return "";
        } else {
            return uuid.toString();
        }
    }

    private static long localDateTimeToEpochSecond(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return 0;
        } else {
            return localDateTime.toEpochSecond(ZoneOffset.UTC);
        }
    }
}
