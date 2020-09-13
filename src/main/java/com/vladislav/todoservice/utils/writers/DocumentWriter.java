package com.vladislav.todoservice.utils.writers;

public interface DocumentWriter<DOC, DTO> {

    DTO write(DOC document);
}
