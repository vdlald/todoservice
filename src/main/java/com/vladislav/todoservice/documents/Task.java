package com.vladislav.todoservice.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(collection = "tasks")
public class Task {

    @MongoId
    private UUID id;

    private UUID userId;

    private UUID projectId;

    private String title;

    private String content;

    private Boolean completed;

    private LocalDateTime deadline;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private Boolean isDeleted;

}