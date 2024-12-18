package com.vsantos.springtechevents.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vsantos.springtechevents.domain.event.EventDetailsDTO;
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

  @GetMapping("/{eventId}")
  public ResponseEntity<EventDetailsDTO> getEventDetails(@PathVariable UUID eventId) {

    EventDetailsDTO event = eventService.getEventById(eventId);

    return ResponseEntity.status(HttpStatus.OK).body(event);
  }

  @GetMapping("/filter")
  public ResponseEntity<List<EventResponseDTO>> getFilteredEvents(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String uf) {

    List<EventResponseDTO> events = eventService.getFilteredEvents(page, size, city, uf);

    return ResponseEntity.status(HttpStatus.OK).body(events);
  }

  @GetMapping("/search")
  public ResponseEntity<List<EventResponseDTO>> getSearchEvents(@RequestParam String title,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    List<EventResponseDTO> events = eventService.searchEventsByTitle(title, page, size);
    return ResponseEntity.ok(events);
  }

  @PostMapping
  public ResponseEntity<EventResponseDTO> createEvent(@ModelAttribute EventRequestDTO eventDTO) {

    EventResponseDTO createdEvent = eventService.createEvent(eventDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
  }

  @GetMapping
  public ResponseEntity<List<EventResponseDTO>> getEvents(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    List<EventResponseDTO> allEvents = eventService.getUpcomingEvents(page, size);
    return ResponseEntity.status(HttpStatus.OK).body(allEvents);
  }
}
