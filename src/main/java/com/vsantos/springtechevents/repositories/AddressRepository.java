package com.vsantos.springtechevents.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vsantos.springtechevents.domain.address.Address;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
