package com.vsantos.springtechevents.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vsantos.springtechevents.domain.address.Address;
import com.vsantos.springtechevents.domain.event.Event;
import com.vsantos.springtechevents.domain.event.EventRequestDTO;
import com.vsantos.springtechevents.repositories.AddressRepository;

@Service
public class AddressService {

  private final AddressRepository addressRepository;

  @Autowired
  public AddressService(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
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
