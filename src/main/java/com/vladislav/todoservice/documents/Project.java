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
@Document(collection = "projects")
public class Project {

    @MongoId
    private UUID id;

    private UUID userId;

    private String name;

    private Boolean isDeleted;

    private LocalDateTime createdAt;

}
