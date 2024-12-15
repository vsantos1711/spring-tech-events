package com.vsantos.springtechevents.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import com.vsantos.springtechevents.repositories.EventRepository;

@Service
public class EventService {

  @Autowired
  private AmazonS3 s3Client;

  @Autowired
  private EventRepository eventRepository;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  public Event createEvent(EventRequestDTO eventDTO) {
    String imgUrl = null;

    if (eventDTO.image() != null) {
      imgUrl = this.uploadImage(eventDTO.image());
    }

    Event event = Event.builder()
        .title(eventDTO.title())
        .description(eventDTO.description())
        .imgUrl(imgUrl)
        .eventUrl(eventDTO.eventUrl())
        .remote(eventDTO.remote())
        .date(eventDTO.date())
        .build();

    return this.eventRepository.save(event);

  }

  private String uploadImage(MultipartFile image) {
    String fileName = UUID.randomUUID().toString().substring(0, 8) + "-" + image.getOriginalFilename();

    try {
      File file = this.convertMultiPartToFile(image);
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