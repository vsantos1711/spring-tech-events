package com.vsantos.springtechevents.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import com.vsantos.springtechevents.domain.event.EventResponseDTO;
import com.vsantos.springtechevents.services.EventService;

@RestController
@RequestMapping("/events")
public class EventController {
  private final EventService eventService;

  @Autowired
  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping
  public ResponseEntity<Event> createEvent(
      @ModelAttribute EventRequestDTO eventDTO,
      @RequestParam("image") MultipartFile image) {
    Event createdEvent = eventService.createEvent(eventDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
  }

  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<EventResponseDTO> allEvents = eventService.getUpcomingEvents(page, size);
    return ResponseEntity.status(HttpStatus.OK).body(allEvents);
  }
}