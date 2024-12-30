package com.vsantos.springtechevents.repositories;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import com.vsantos.springtechevents.domain.address.Address;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
class EventRepositoryTest {

  @Autowired
  EventRepository eventRepository;

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  EntityManager entityManager;

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
    // caso: falha (não achar o evento) -> retorna um array vazio

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
