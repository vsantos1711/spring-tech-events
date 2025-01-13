package com.vsantos.springtechevents.repositories;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import com.vsantos.springtechevents.domain.address.Address;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@DataJpaTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class EventRepositoryTest {

  private final EventRepository eventRepository;
  private final EntityManager entityManager;

  @Test
  @DisplayName("Should returns list of Events when successful")
  void findByTitleContainingIgnoreCaseSuccess() {
    String title = "Java";

    EventRequestDTO data = EventRequestDTO.builder()
        .title("The best Java event!")
        .description("Java event how teach you how to code in Java with Spring")
        .eventUrl("https://example.com")
        .city("São Paulo")
        .uf("SP")
        .date(OffsetDateTime.parse("2022-12-01T18:00:00Z"))
        .remote(false)
        .build();

    createEvent(data);
    Page<Event> result = eventRepository.findByTitleContainingIgnoreCase(title, null);

    assertThat(result.getContent()).isNotEmpty();
    Event event = result.getContent().get(0);

    assertThat(event.getTitle()).contains(title);
  }

  @Test
  @DisplayName("Should returns empty list of Events when no event is found")
  void findByTitleContainingIgnoreCaseError() {
    String title = "Java";

    EventRequestDTO data = EventRequestDTO.builder()
        .title("The best NodeJS event!")
        .description("NodeJS event how teach you how to code in NodeJS with Express")
        .eventUrl("https://example-node.com")
        .date(OffsetDateTime.parse("2027-12-01T18:00:00Z"))
        .remote(true)
        .build();

    createEvent(data);
    Page<Event> result = eventRepository.findByTitleContainingIgnoreCase(title, null);

    assertThat(result.getContent()).isEmpty();
  }

  @Test
  @DisplayName("Should returns list of Events when successful")
  void findFilteredEventsSuccess() {
    String city = "São Paulo";
    String uf = "SP";

    EventRequestDTO eventTwo = EventRequestDTO.builder()
        .title("The best Node course!")
        .description("Node course how teach you how to code in Node with Express")
        .eventUrl("https://example-node.com")
        .city("Recife")
        .uf("PE")
        .date(OffsetDateTime.parse("2025-12-12T18:00:00Z"))
        .remote(false)
        .build();

    EventRequestDTO eventOne = EventRequestDTO.builder()
        .title("The best Java event!")
        .description("Java event how teach you how to code in Java with Spring")
        .eventUrl("https://example.com")
        .city("São Paulo")
        .uf("SP")
        .date(OffsetDateTime.parse("2025-12-01T18:00:00Z"))
        .remote(false)
        .build();

    createEvent(eventOne);
    createEvent(eventTwo);

    Page<Event> result = eventRepository.findFilteredEvents(city, uf, null);
    assertThat(result.getContent()).isNotEmpty();
    assertThat(result.getContent()).hasSize(1);

    Event event = result.getContent().get(0);
    assertThat(event.getAddress().getCity()).contains(city);
    assertThat(event.getAddress().getUf()).contains(uf);
  }

  @Test
  @DisplayName("Should returns empty list of Events when no event is found")
  void findFilteredEventsError() {
    String city = "São Paulo";
    String uf = "SP";

    EventRequestDTO eventTwo = EventRequestDTO.builder()
        .title("The best Node course!")
        .description("Node course how teach you how to code in Node with Express")
        .eventUrl("https://example-node.com")
        .city("Recife")
        .uf("PE")
        .date(OffsetDateTime.parse("2025-12-12T18:00:00Z"))
        .remote(false)
        .build();

    EventRequestDTO eventOne = EventRequestDTO.builder()
        .title("The best Java event!")
        .description("Java event how teach you how to code in Java with Spring")
        .eventUrl("https://example.com")
        .city("Recife")
        .uf("PE")
        .date(OffsetDateTime.parse("2025-12-01T18:00:00Z"))
        .remote(false)
        .build();

    createEvent(eventOne);
    createEvent(eventTwo);

    Page<Event> result = eventRepository.findFilteredEvents(city, uf, null);
    assertThat(result.getContent()).isEmpty();
  }

  private Event createEvent(EventRequestDTO data) {

    Event newEvent = Event.builder()
        .title(data.title())
        .description(data.description())
        .imgUrl("https://example.com/image.jpg")
        .eventUrl(data.eventUrl())
        .remote(data.remote())
        .date(data.date())
        .build();

    if (!data.remote()) {
      Address address = Address.builder()
          .city(data.city())
          .uf(data.uf())
          .event(newEvent)
          .build();

      newEvent.setAddress(address);
      entityManager.persist(address);
    }

    entityManager.persist(newEvent);

    return newEvent;
  }
}
