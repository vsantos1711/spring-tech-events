package com.vsantos.springtechevents.domain.event;

import java.time.OffsetDateTime;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public record EventRequestDTO(
        @Schema(description = "Event title", example = "Java Conference") String title,
        @Schema(description = "Event description", example = "The best Java conference in the world") String description,
        @Schema(description = "Event date", example = "2022-12-01T10:00:00") OffsetDateTime date,
        @Schema(description = "Event address", example = "Java Street") String city,
        @Schema(description = "Event state", example = "SP") String uf, Boolean remote,
        @Schema(description = "Event URL", example = "https://javac.com.br") String eventUrl,
        @Schema(description = "Upload an image", type = "string", format = "binary") MultipartFile image) {

}
