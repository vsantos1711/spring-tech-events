package com.vsantos.springtechevents.domain.coupon;

import java.time.LocalDateTime;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupon")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

  @Id
  @GeneratedValue
  private UUID id;

  private String code;
  private Integer discount;
  private LocalDateTime valid;

  @ManyToOne
  @JoinColumn(name = "event_id")
  private Event event;
}