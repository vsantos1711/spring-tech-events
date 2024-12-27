package com.vsantos.springtechevents.domain.address;

import java.util.UUID;

import com.vsantos.springtechevents.domain.event.Event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
  @Id
  @GeneratedValue
  private UUID id;

  private String city;
  private String uf;

  @ManyToOne
  @JoinColumn(name = "event_id")
  private Event event;
}
