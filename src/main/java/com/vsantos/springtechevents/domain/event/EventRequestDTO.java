package com.vsantos.springtechevents.domain.event;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public record EventRequestDTO(String title, String description,
                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date date, String city, String uf, Boolean remote,
                String eventUrl, MultipartFile image) {

}
