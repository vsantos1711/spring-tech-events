package com.vsantos.springtechevents.domain.event;

import java.time.OffsetDateTime;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;


@Builder
public record EventRequestDTO(

    @NotBlank(message = "The title is required")
    @Schema(description = "Event title", example = "Java Conference")
    String title,

    @NotBlank(message = "The description is required")
    @Schema(description = "Event description", example = "The best Java conference in the world")
    String description,

    @FutureOrPresent(message = "Event date must be in the present or future")
    @Schema(description = "Event date", example = "2022-12-01T10:00:00")
    OffsetDateTime date,

    @Schema(description = "Event address", example = "Java Street")
    String city,

    @Size(min = 2, max = 2, message = "State abbreviation must be 2 characters")
    @Pattern(regexp = "[A-Za-z]{2}", message = "State abbreviation must consist of two letters")
    @Schema(description = "Event state", example = "SP")
    String uf,

    @NotNull(message = "The remote flag is required")
    @Schema(description = "Is the event remote?")
    Boolean remote,

    @URL(message = "Event URL must be a valid URL")
    @Schema(description = "Event URL", example = "https://javac.com.br")
    String eventUrl,

    @Schema(description = "Upload an image", type = "string", format = "binary")
    MultipartFile image) {
}
