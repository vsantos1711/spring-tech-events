package com.vsantos.springtechevents.domain.event;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.vsantos.springtechevents.domain.address.AddressResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EventResponseDTO(

        UUID id, 
        @Schema(example = "Spring Boot")
        String title, 
        @Schema(example = "A short event description")
        String description, 
        @Schema(example = "2024-12-18T22:26:24.649Z")
        OffsetDateTime date,
        @Schema(example = "true")
        Boolean remote, 
        @Schema(example = "https://javac.com.br")
        String eventUrl, 
        @Schema(example = "https://th.bing.com/th/id/OIP.2vUTawLy") 
        String imgUrl, @Schema()
        AddressResponseDTO address) {

}