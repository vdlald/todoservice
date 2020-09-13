package com.vladislav.todoservice.utils.readers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

abstract class AbstractReader<DOC, DTO> implements DocumentReader<DOC, DTO> {

    protected UUID readUuid(String uuid) {
        return UUID.fromString(uuid);
    }

    protected LocalDateTime readLocalDateTime(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli).atOffset(ZoneOffset.UTC).toLocalDateTime();
    }
}
