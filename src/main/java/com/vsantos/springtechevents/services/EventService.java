package com.vsantos.springtechevents.services;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.vsantos.springtechevents.domain.address.Address;
import com.vsantos.springtechevents.domain.address.AddressResponseDTO;
import com.vsantos.springtechevents.domain.coupon.Coupon;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventDetailsDTO;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import com.vsantos.springtechevents.domain.event.EventResponseDTO;
import com.vsantos.springtechevents.repositories.EventRepository;

import lombok.RequiredArgsConstructor;

import com.vsantos.springtechevents.exceptions.EventException;
import com.vsantos.springtechevents.exceptions.ImageUploadException;

@Service
@RequiredArgsConstructor
public class EventService {

  private final AmazonS3 s3Client;
  private final EventRepository eventRepository;
  private final AddressService addressService;
  private final CouponService couponService;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  public EventDetailsDTO getEventById(UUID eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new EventException("Event not found with id: " + eventId));

    Optional<Address> address = addressService.findByEventID(eventId);
    List<Coupon> coupons = couponService.consultCoupons(eventId, OffsetDateTime.now());

    List<EventDetailsDTO.CouponDTO> couponDTOs = coupons.stream()
        .map(coupon -> EventDetailsDTO.CouponDTO.builder()
            .code(coupon.getCode())
            .discount(coupon.getDiscount())
            .valid(coupon.getValid())
            .build())
        .toList();

    return EventDetailsDTO.builder()
        .id(event.getId())
        .title(event.getTitle())
        .description(event.getDescription())
        .date(event.getDate())
        .city(address.isPresent() ? address.get().getCity() : "")
        .state(address.isPresent() ? address.get().getUf() : "")
        .imgUrl(event.getImgUrl())
        .eventUrl(event.getEventUrl())
        .remote(event.getRemote())
        .coupons(couponDTOs)
        .build();
  }

  public List<EventResponseDTO> getFilteredEvents(int page, int size, String city, String uf) {
    Pageable pageable = PageRequest.of(page, size);

    city = (city != null) ? city : "";
    uf = (uf != null) ? uf : "";

    Page<Event> events = eventRepository.findFilteredEvents(city, uf, pageable);

    return events.stream().map(event -> EventResponseDTO.builder()
        .id(event.getId())
        .title(event.getTitle())
        .description(event.getDescription())
        .date(event.getDate())
        .address(event.getAddress() != null
            ? new AddressResponseDTO(event.getAddress().getCity(), event.getAddress().getUf())
            : null)
        .remote(event.getRemote())
        .eventUrl(event.getEventUrl())
        .imgUrl(event.getImgUrl())
        .build()).toList();
  }

  public List<EventResponseDTO> searchEventsByTitle(String title, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Event> events = eventRepository.findByTitleContainingIgnoreCase(title, pageable);

    return events.stream().map(event -> EventResponseDTO.builder()
        .id(event.getId())
        .title(event.getTitle())
        .description(event.getDescription())
        .date(event.getDate())
        .address(event.getAddress() != null
            ? new AddressResponseDTO(event.getAddress().getCity(), event.getAddress().getUf())
            : null)
        .remote(event.getRemote())
        .eventUrl(event.getEventUrl())
        .imgUrl(event.getImgUrl())
        .build()).toList();
  }

  public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Event> events = eventRepository.findUpcomingEvents(OffsetDateTime.now(), pageable);

    return events.map(event -> EventResponseDTO.builder()
        .id(event.getId())
        .title(event.getTitle())
        .description(event.getDescription())
        .date(event.getDate())
        .address(event.getAddress() != null
            ? new AddressResponseDTO(event.getAddress().getCity(), event.getAddress().getUf())
            : null)
        .remote(event.getRemote())
        .eventUrl(event.getEventUrl())
        .imgUrl(event.getImgUrl())
        .build()).stream().toList();
  }

  public EventResponseDTO createEvent(EventRequestDTO eventDTO) throws ImageUploadException {
    String imgUrl = null;

    if (eventDTO.image() == null) {
      throw new IllegalArgumentException("Event image is required");
    }
    imgUrl = uploadImage(eventDTO.image());

    Event newEvent = Event.builder()
        .title(eventDTO.title())
        .description(eventDTO.description())
        .imgUrl(imgUrl)
        .eventUrl(eventDTO.eventUrl())
        .remote(eventDTO.remote())
        .date(eventDTO.date())
        .build();

    eventRepository.save(newEvent);

    if (!eventDTO.remote()) {
      Address address = addressService.createAddress(eventDTO, newEvent);
      newEvent.setAddress(address);
    }

    return EventResponseDTO.builder()
        .id(newEvent.getId())
        .title(newEvent.getTitle())
        .description(newEvent.getDescription())
        .imgUrl(imgUrl)
        .eventUrl(newEvent.getEventUrl())
        .remote(newEvent.getRemote())
        .date(newEvent.getDate())
        .address(newEvent.getAddress() != null
            ? new AddressResponseDTO(newEvent.getAddress().getCity(), newEvent.getAddress().getUf())
            : null)
        .build();
  }

  private String uploadImage(MultipartFile image) throws ImageUploadException {
    String fileName = UUID.randomUUID().toString().substring(0, 8) + "-" + image.getOriginalFilename();

    try (InputStream inputStream = image.getInputStream()) {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(image.getSize());
      metadata.setContentType(image.getContentType());

      s3Client.putObject(bucketName, fileName, inputStream, metadata);

      return s3Client.getUrl(bucketName, fileName).toString();
    } catch (Exception e) {
      throw new ImageUploadException(e);
    }
  }
}
