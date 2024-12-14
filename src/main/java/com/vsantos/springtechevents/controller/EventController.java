package com.vsantos.springtechevents.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import com.vsantos.springtechevents.services.EventService;

@RestController
@RequestMapping("/events")
public class EventController {

  @Autowired
  private EventService eventService;

  @PostMapping
  public ResponseEntity<Event> createEvent(
      @ModelAttribute EventRequestDTO eventDTO,
      @RequestParam("image") MultipartFile image) {
    Event createdEvent = eventService.createEvent(eventDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
  }
}