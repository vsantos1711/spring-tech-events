
package com.vsantos.springtechevents.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import com.amazonaws.services.s3.AmazonS3;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import com.vsantos.springtechevents.repositories.EventRepository;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DataJpaTest
@ActiveProfiles("test")
class EventServiceTest {

  @Mock
  private AmazonS3 s3Client;

  @Mock
  private EventRepository eventRepository;

  @Mock
  private AddressService addressService;

  @Mock
  private CouponService couponService;

  @InjectMocks
  private EventService eventService;

  @Test
  @DisplayName("Should throw IllegalArgumentException when image is null")
  void createEventWithoutImage() {
    EventRequestDTO eventDTO = EventRequestDTO.builder()
        .title("The best Java event!")
        .description("Java event how teach you how to code in Java with Spring")
        .eventUrl("https://example.com")
        .city("São Paulo")
        .uf("SP")
        .image(null)
        .date(OffsetDateTime.parse("2022-12-01T18:00:00Z"))
        .remote(false)
        .build();

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> eventService.createEvent(eventDTO));

    assertEquals("Event image is required", exception.getMessage());
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException when required fields are missing")
  void createEventWithoutRequiredFields() throws MalformedURLException {
    EventRequestDTO eventDTO = EventRequestDTO.builder()
        .title(null)
        .description("")
        .eventUrl(null)
        .city("")
        .uf("")
        .image(new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", new byte[0]))
        .date(OffsetDateTime.now().minusDays(1))
        .remote(null)
        .build();

    when(s3Client.getUrl(any(), any())).thenReturn(URI.create("https://example.com").toURL());

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    var violations = validator.validate(eventDTO);

    assertFalse(violations.isEmpty());
    assertEquals(7, violations.size());
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The title is required")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The description is required")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The remote flag is required")));
    assertTrue(
        violations.stream().anyMatch(v -> v.getMessage().equals("State abbreviation must consist of two letters")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The address is required")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The event URL is required")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Event date must be in the present or future")));
  }

}
