package com.vsantos.springtechevents.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import com.vsantos.springtechevents.domain.address.Address;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@DataJpaTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class AddressRepositoryTest {

  private final AddressRepository addressRepository;
  private final EntityManager entityManager;

  @Test
  @DisplayName("Should find address by event id")
  void findByEventIdSuccess() {
    EventRequestDTO eventDTO = EventRequestDTO.builder()
        .title("The best Java event!")
        .description("Java event how teach you how to code in Java with Spring")
        .eventUrl("https://example.com")
        .city("São Paulo")
        .uf("SP")
        .date(null)
        .remote(false)
        .build();

    Event event = createEvent(eventDTO);

    Optional<Address> foundAddress = addressRepository.findByEventId(event.getId());

    assertThat(foundAddress).isPresent();
    assertThat(foundAddress.get().getCity()).isEqualTo(eventDTO.city());
    assertThat(foundAddress.get().getUf()).isEqualTo(eventDTO.uf());
  }

  @Test
  @DisplayName("Should return empty when address not found by event id")
  void findByEventIdError() {
    UUID eventId = UUID.randomUUID();

    Optional<Address> foundAddress = addressRepository.findByEventId(eventId);

    assertThat(foundAddress).isNotPresent();
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
