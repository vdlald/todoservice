package com.vladislav.todoservice.utils.readers;

import com.proto.todo.Task;
import com.vladislav.todoservice.documents.TaskDocument;
import org.springframework.stereotype.Component;

@Component
public class TaskDocumentReader extends AbstractReader<TaskDocument, Task> {
    @Override
    public TaskDocument read(Task task) {
        return new TaskDocument()
                .setId(readUuid(task.getId()))
                .setUserId(readUuid(task.getUserId()))
                .setProjectId(readUuid(task.getProjectId()))
                .setTitle(task.getTitle())
                .setContent(task.getContent())
                .setCompleted(task.getCompleted())
                .setDeadline(readLocalDateTime(task.getDeadline()))
                .setCreatedAt(readLocalDateTime(task.getCreatedAt()))
                .setCompletedAt(readLocalDateTime(task.getCompletedAt()))
                .setIsDeleted(task.getIsDeleted());
    }
}
