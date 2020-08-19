package com.vladislav.todoservice.utils.mappers;

public interface PojoMapper<DOC, DTO> {
    DOC toDocument(DTO dto);

    DTO toDto(DOC document);
}
