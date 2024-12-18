package com.vsantos.springtechevents.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.vsantos.springtechevents.domain.address.Address;
import com.vsantos.springtechevents.domain.coupon.Coupon;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventDetailsDTO;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import com.vsantos.springtechevents.domain.event.EventResponseDTO;
import com.vsantos.springtechevents.repositories.EventRepository;

@Service
public class EventService {

  private final AmazonS3 s3Client;
  private final EventRepository eventRepository;
  private final AddressService addressService;
  private final CouponService couponService;

  @Autowired
  public EventService(AmazonS3 s3Client, EventRepository eventRepository, AddressService addressService,
      CouponService couponService) {
    this.s3Client = s3Client;
    this.eventRepository = eventRepository;
    this.addressService = addressService;
    this.couponService = couponService;
  }

  @Value("${aws.s3.bucket}")
  private String bucketName;

  public EventDetailsDTO getEventById(UUID eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

    Optional<Address> address = addressService.findByEventID(eventId);
    List<Coupon> coupons = couponService.consultCoupons(eventId, LocalDateTime.now());

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
        .city(event.getAddress() != null ? event.getAddress().getCity() : "")
        .uf(event.getAddress() != null ? event.getAddress().getUf() : "")
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
        .city(event.getAddress() != null ? event.getAddress().getCity() : "")
        .uf(event.getAddress() != null ? event.getAddress().getUf() : "")
        .remote(event.getRemote())
        .eventUrl(event.getEventUrl())
        .imgUrl(event.getImgUrl())
        .build()).toList();
  }

  public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Event> events = eventRepository.findUpcomingEvents(LocalDateTime.now(), pageable);

    return events.map(event -> EventResponseDTO.builder()
        .id(event.getId())
        .title(event.getTitle())
        .description(event.getDescription())
        .date(event.getDate())
        .city(event.getAddress() != null ? event.getAddress().getCity() : "")
        .uf(event.getAddress() != null ? event.getAddress().getUf() : "")
        .remote(event.getRemote())
        .eventUrl(event.getEventUrl())
        .imgUrl(event.getImgUrl())
        .build()).stream().toList();
  }

  public EventResponseDTO createEvent(EventRequestDTO eventDTO) {
    String imgUrl = null;

    if (eventDTO.image() != null) {
      imgUrl = uploadImage(eventDTO.image());
    }

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
      addressService.createAddress(eventDTO, newEvent);
    }

    return EventResponseDTO.builder()
        .title(eventDTO.title())
        .description(eventDTO.description())
        .imgUrl(imgUrl)
        .eventUrl(eventDTO.eventUrl())
        .remote(eventDTO.remote())
        .date(eventDTO.date())
        .city(eventDTO.city())
        .uf(eventDTO.uf())
        .build();
  }

  private String uploadImage(MultipartFile image) {
    String fileName = UUID.randomUUID().toString().substring(0, 8) + "-" + image.getOriginalFilename();

    try {
      File file = convertMultiPartToFile(image);
      s3Client.putObject(bucketName, fileName, file);
      file.delete();
      return s3Client.getUrl(bucketName, fileName).toString();
    } catch (Exception e) {
      throw new RuntimeException("Error uploading image to S3", e);
    }
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convertedFile = new File(file.getOriginalFilename());
    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
      fos.write(file.getBytes());
    }
    return convertedFile;
  }
}
