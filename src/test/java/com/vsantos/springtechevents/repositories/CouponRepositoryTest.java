package com.vsantos.springtechevents.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import com.vsantos.springtechevents.domain.address.Address;
import com.vsantos.springtechevents.domain.coupon.Coupon;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@DataJpaTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class CouponRepositoryTest {

  private final CouponRepository couponRepository;
  private final EntityManager entityManager;

  @Test
  @DisplayName("Should find valid coupon by Event id")
  void findByEventIdAndValidAfter() {
    EventRequestDTO eventDTO = EventRequestDTO.builder()
        .title("The best Java event!")
        .description("Java event how teach you how to code in Java with Spring")
        .eventUrl("https://example.com")
        .city("São Paulo")
        .uf("SP")
        .date(OffsetDateTime.now().plusDays(2))
        .remote(false)
        .build();

    Event event = createEvent(eventDTO);

    Coupon invalidCoupon = Coupon.builder()
        .code("TECHOLD")
        .discount(10)
        .event(event)
        .valid(OffsetDateTime.now().minusDays(1))
        .build();

    Coupon validCoupon = Coupon.builder()
        .code("TECHNOW")
        .discount(10)
        .event(event)
        .valid(OffsetDateTime.now().plusDays(1))
        .build();

    entityManager.persist(invalidCoupon);
    entityManager.persist(validCoupon);

    List<Coupon> result = couponRepository.findByEventIdAndValidAfter(event.getId(), OffsetDateTime.now());

    assertThat(result)
        .isNotEmpty()
        .hasSize(1)
        .first()
        .satisfies(coupon -> {
          assertThat(coupon.getEvent()).isEqualTo(event);
          assertThat(coupon.getCode()).isEqualTo(validCoupon.getCode());
        });
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
