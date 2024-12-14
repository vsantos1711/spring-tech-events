package com.vsantos.springtechevents.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vsantos.springtechevents.domain.address.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

}
