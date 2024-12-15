package com.vsantos.springtechevents.domain.event;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;

@Builder
public record EventResponseDTO(UUID id, String title, String description, Date date, String city, String uf,
    Boolean remote, String eventUrl, String imgUrl) {
}