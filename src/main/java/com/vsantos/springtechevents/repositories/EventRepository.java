package com.vsantos.springtechevents.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vsantos.springtechevents.domain.event.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

}