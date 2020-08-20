package com.vladislav.todoservice.utils.mappers;

import com.proto.todo.Task;
import com.vladislav.todoservice.documents.TaskDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(collection = "tasks")
public class TaskMapper implements PojoMapper<TaskDocument, Task> {
    @Override
    public TaskDocument toDocument(Task task) {
        return new TaskDocument()
                .setId(UUID.fromString(task.getId()))
                .setUserId(UUID.fromString(task.getUserId()))
                .setProjectId(UUID.fromString(task.getProjectId()))
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
                .setProjectId(document.getProjectId().toString())
                .setTitle(document.getTitle())
                .setContent(document.getContent())
                .setCompleted(document.getCompleted())
                .setDeadline(document.getDeadline().toEpochSecond(ZoneOffset.UTC))
                .setCreatedAt(document.getCreatedAt().toEpochSecond(ZoneOffset.UTC))
                .setCompletedAt(document.getCompletedAt().toEpochSecond(ZoneOffset.UTC))
                .build();
    }
}
