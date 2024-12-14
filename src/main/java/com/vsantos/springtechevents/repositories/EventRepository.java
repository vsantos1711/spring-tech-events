package com.vsantos.springtechevents.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vsantos.springtechevents.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

}