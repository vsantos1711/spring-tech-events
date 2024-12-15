package com.vsantos.springtechevents.repositories;

import java.sql.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vsantos.springtechevents.domain.event.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

  public Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  @Query("SELECT e FROM Event e WHERE e.date >= CURRENT_DATE")
  public Page<Event> findUpcomingEvents(@Param("currentDate") Date currentDate, Pageable pageable);
}