package com.vsantos.springtechevents.domain.event;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public record EventRequestDTO(String title, String description,
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime date, String city, String uf, Boolean remote,
    String eventUrl, MultipartFile image) {

}
