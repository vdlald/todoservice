package com.vladislav.todoservice.utils.writers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

abstract class AbstractWriter<DOC, DTO> implements DocumentWriter<DOC, DTO> {

    protected String writeUuid(UUID uuid) {
        return uuid.toString();
    }

    protected long writeLocalDateTime(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
