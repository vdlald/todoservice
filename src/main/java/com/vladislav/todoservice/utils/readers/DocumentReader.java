package com.vladislav.todoservice.utils.readers;

public interface DocumentReader<DOC, DTO> {

    DOC read(DTO dto);
}
