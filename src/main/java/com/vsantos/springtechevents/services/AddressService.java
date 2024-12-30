package com.vsantos.springtechevents.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.vsantos.springtechevents.domain.address.Address;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import com.vsantos.springtechevents.repositories.AddressRepository;

@Service
public class AddressService {

  private final AddressRepository addressRepository;

  public AddressService(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
  }

  public Optional<Address> findByEventID(UUID eventId) {
    return this.addressRepository.findByEventId(eventId);
  }

  public Address createAddress(EventRequestDTO data, Event event) {
    Address address = Address.builder()
        .city(data.city())
        .uf(data.uf())
        .event(event)
        .build();

    return this.addressRepository.save(address);
  }
}
