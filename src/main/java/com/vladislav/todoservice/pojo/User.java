package com.vladislav.todoservice.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {

    private UUID id;

    private String username;

    private List<Role> roles;

    public enum Role {
        USER,
        ADMIN
    }
}
